package fr.antoineok.jauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import fr.antoineok.jauth.exception.*;
import fr.antoineok.jauth.jsons.*;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class JAuth
{
    private String serverName, urlF;

    private AuthStatus authStatus;

    private boolean userConnected;

    private boolean rejectedBanned;

    protected final Gson GSON = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();

    private JsonProfile profile;

    public static JsonLang langDef;

    public JsonProfile getProfile()
    {
        return profile;
    }

    public AuthStatus getAuthStatus()
    {
        return authStatus;
    }

    private boolean neededConfirmed;

    private int userMaxChar;
    private int passwordMaxChar;

    public JAuth(String serverName, String url)
    {
        this(serverName, url, 25, 25);
    }

    public JAuth(String serverName, String url, int userMaxchar, int passMaxchar, boolean confirm, boolean ban)
    {
        if(url.endsWith("/"))
        {
            this.urlF = url.substring(0, url.length() - 1);
        }
        else
        {
            this.urlF = url;
        }
        this.authStatus = AuthStatus.DISCONNECTED;
        this.userConnected = false;
        this.serverName = serverName;
        this.userMaxChar = userMaxchar;
        this.passwordMaxChar = passMaxchar;
        this.neededConfirmed = confirm;
        this.rejectedBanned = ban;
        try
        {
            langDef = GSON.fromJson(readLang(), JsonLang.class);
        }
        catch(Exception e)
        {
            System.out.println("impossible de charger le fichier lang");
            e.printStackTrace();
            System.exit(1);
        }
    }

    String readLang() throws Exception
    {
        InputStream in = null;
        if(Locale.getDefault().getLanguage().equals(Locale.FRENCH.getLanguage()))
        {
            in = JAuth.class.getResourceAsStream("/fr.json");
        }
        else
        {
            in = JAuth.class.getResourceAsStream("/en.json");
        }
        //
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        final StringBuilder text = new StringBuilder();

        String line;

        while((line = reader.readLine()) != null)
        {
            text.append(line);
        }
        reader.close();
        return text.toString();
    }

    public JAuth(String serverName, String url, int userMaxchar, int passMaxchar)
    {
        this(serverName, url, userMaxchar, passMaxchar, false, false);
    }

    /**
     *
     * @param username the player username
     * @param password the player password
     * @throws ServerNotFoundException if the remote server don't exit
     * @throws UserEmptyException if the player don't exist
     * @throws UserNotConfirmedException if the player hasn't confirmed his account
     * @throws UserBannedException if the user is banned
     * @throws UserWrongException if the username or the password is invalid
     * @throws IOException if there is a problem with the connexion
     */
    public void connect(String username, String password) throws ServerNotFoundException, UserEmptyException, UserNotConfirmedException, UserBannedException, UserWrongException, IOException
    {
        System.out.println(TrixUtil.log(langDef.getLoginP()));
        long time = System.currentTimeMillis();
        try {
            this.connect(this.urlF, username, password);
        } catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

        if(this.authStatus != AuthStatus.CONNECTED)
        {
            return;
        }
        time = System.currentTimeMillis() - time;
        DecimalFormat form = new DecimalFormat("#.##");
        System.out.println(TrixUtil.log(langDef.getLoginP().replace("<time>", form.format(time))));
    }

    private void connect(String url, String username, String password) throws ServerNotFoundException, UserEmptyException, UserNotConfirmedException, UserBannedException, UserWrongException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException
    {

        this.authStatus = AuthStatus.CONNECTION;

        if(url == null)
        {

            this.userConnected = false;
            this.authStatus = AuthStatus.DISCONNECTED;

            throw new ServerNotFoundException(TrixUtil.log(langDef.getIncoURL()));
        }

        if(username.length() > this.userMaxChar || password.length() > this.passwordMaxChar || username.equals("") || password.equals("") || password == null || username.indexOf(' ') != -1 || password.indexOf(' ') != -1)
        {

            this.userConnected = false;
            this.authStatus = AuthStatus.DISCONNECTED;

            throw new UserEmptyException(TrixUtil.log(langDef.getIncoURL()));
        }

        int responseCode = TrixUtil.pingUrl(url);

        if(!(200 <= responseCode && responseCode <= 399))
        {
            this.userConnected = false;
            this.authStatus = AuthStatus.DISCONNECTED;
            throw new HttpException(TrixUtil.log(langDef.getErrCo().replace("<ServerName>", this.serverName)));
        }

        TrixProfileManager profileManager = new TrixProfileManager(this.urlF, username, password);

        /* *
         * Permet de vérifier si l'utilisateur est bon
         */

        if(!profileManager.isProfileExist())
        {
            this.userConnected = false;
            this.authStatus = AuthStatus.DISCONNECTED;

            throw new UserWrongException(TrixUtil.log(langDef.getCompteInex()));
        }

        System.out.println(TrixUtil.log(langDef.getProfileLoad()));
        long time = System.currentTimeMillis();
        /*
         * Permet de récupérer les informations du joueur
         */
        
		profileManager.loadProfile();
        this.profile = profileManager.getProfile();

        time = System.currentTimeMillis() - time;
        DecimalFormat form = new DecimalFormat("#.##");
        System.out.println(TrixUtil.log(langDef.getProfileLoadSucc().replace("<time>", form.format(time))));
        if(this.rejectedBanned && this.profile.isAccountBanned())
        {
            this.userConnected = false;
            this.authStatus = AuthStatus.DISCONNECTED;//
            throw new UserBannedException(TrixUtil.log(langDef.getBannedAcc().replace("<ServerName>", this.serverName).replace("<reason>", profile.getBanned_reason())));
        }
        if(this.neededConfirmed && !this.profile.isAccountConfirmed())
        {
            this.userConnected = false;
            this.authStatus = AuthStatus.DISCONNECTED;
            throw new UserNotConfirmedException(TrixUtil.log(langDef.getAccNotVerif()));
        }
        this.userConnected = true;

        this.authStatus = AuthStatus.CONNECTED;
    }

    /**
     *
     * @param username the player username
     * @param accessToken the player access token
     * @return the new token of player (if there is no exception)
     * @throws UserWrongException if the player doesn't exist (invalid username)
     * @throws InvalidTokenException if the token is wrong
     * @throws IOException if there is a problem with the connexion to the site
     */
    public String refresh(String username, String accessToken) throws UserWrongException, InvalidTokenException, IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        String dataJson = GSON.toJson(new JsonToken(TrixProfileManager.toBase64(username), TrixProfileManager.toBase64(accessToken)));
        HttpPost post = new HttpPost(this.urlF + "/api/auth/v1/refresh");
        String key = TrixUtil.getPublicKey(urlF);
        String eJson = new String(TrixUtil.encrypt(dataJson.getBytes(), key));
        List<NameValuePair> urlParameters2 = new ArrayList<>();
        urlParameters2.add(new BasicNameValuePair("data", eJson));
        post.setEntity(new UrlEncodedFormEntity(urlParameters2));

        try(CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse response = httpClient.execute(post))
        {
            String jsonE = EntityUtils.toString(response.getEntity());
            System.out.println(jsonE);
            JsonObject object = JsonParser.parseString(jsonE).getAsJsonObject();
            if(object.getAsJsonPrimitive("type").getAsString().equals("err")){
                switch(object.getAsJsonPrimitive("error").getAsString()){
                    case "accountNotFound":
                        throw new UserWrongException(TrixUtil.log(langDef.getCompteInex()));
                    case "accessTokenNotGood":
                        throw new InvalidTokenException();
                    default:
                        return  "";
                }
            }
            else{
                return object.getAsJsonPrimitive("token").getAsString();
            }
        }

    }

    /**
     *
     * @param username the player username
     * @param accessToken the player access token
     */
    public void validate(String username, String accessToken) throws IOException, UserWrongException, InvalidTokenException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, InvalidKeySpecException {
        String dataJson = GSON.toJson(new JsonToken(TrixProfileManager.toBase64(username), TrixProfileManager.toBase64(accessToken)));
        HttpPost post = new HttpPost(this.urlF + "/api/auth/v1/validate");
        String key = TrixUtil.getPublicKey(urlF);
        String eJson = new String(TrixUtil.encrypt(dataJson.getBytes(), key));
        List<NameValuePair> urlParameters2 = new ArrayList<>();
        urlParameters2.add(new BasicNameValuePair("data", eJson));
        post.setEntity(new UrlEncodedFormEntity(urlParameters2));

        try(CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse response = httpClient.execute(post))
        {
            String jsonE = EntityUtils.toString(response.getEntity());
            JsonObject object = JsonParser.parseString(jsonE).getAsJsonObject();
            if(object.getAsJsonPrimitive("type").getAsString().equals("err")){
                switch(object.getAsJsonPrimitive("error").getAsString()){
                    case "accountNotFound":
                        throw new UserWrongException(TrixUtil.log(langDef.getCompteInex()));
                    case "accessTokenNotGood":
                        throw new InvalidTokenException();
                }
            }
            else{
                String profileJson = object.getAsJsonObject("profile").toString();
                System.out.println(profileJson);
                this.profile =  GSON.fromJson(profileJson, JsonProfile.class);
            }
        }
    }
}
