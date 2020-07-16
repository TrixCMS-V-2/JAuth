package fr.antoineok.jauth;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.text.DecimalFormat;
import java.util.Locale;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.antoineok.jauth.exception.HttpException;
import fr.antoineok.jauth.exception.ServerNotFoundException;
import fr.antoineok.jauth.exception.UserBannedException;
import fr.antoineok.jauth.exception.UserEmptyException;
import fr.antoineok.jauth.exception.UserNotConfirmedException;
import fr.antoineok.jauth.exception.UserWrongException;
import fr.antoineok.jauth.jsons.JsonLang;
import fr.antoineok.jauth.jsons.JsonProfile;

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

    private String userName, userPassword;

    public JAuth(String serverName, String url, String username, String password)
    {
        this(serverName, url, username, password, 25, 25);
    }

    public JAuth(String serverName, String url, String username, String password, int userMaxchar, int passMaxchar, boolean confirm, boolean ban)
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
        this.userName = username;
        this.userPassword = password;
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
            in = JAuth.class.getResourceAsStream("/fr.lang");
        }
        else
        {
            in = JAuth.class.getResourceAsStream("/en.lang");
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

    public JAuth(String serverName, String url, String username, String password, int userMaxchar, int passMaxchar)
    {
        this(serverName, url, username, password, userMaxchar, passMaxchar, false, false);
    }

    public void disconnect()
    {
        if(!this.userConnected || this.authStatus != AuthStatus.DISCONNECTED)
        {
            return;
        }
        System.out.println(TrixUtil.log(langDef.getDecoP()));
        long time = System.currentTimeMillis();
        this.authStatus = AuthStatus.DISCONNECTED;
        this.userConnected = false;
        time = System.currentTimeMillis() - time;
        DecimalFormat form = new DecimalFormat("#.##");
        System.out.println(TrixUtil.log(langDef.getDecoP().replace("<time>", form.format(time))));
    }

    public void connect() throws ServerNotFoundException, UserEmptyException, UserNotConfirmedException, UserBannedException, UserWrongException, IOException
    {
        System.out.println(TrixUtil.log(langDef.getLoginP()));
        long time = System.currentTimeMillis();
        try {
			this.connect(this.urlF);
		} catch (InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | NoSuchPaddingException
				| IllegalBlockSizeException | BadPaddingException e) {
			
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

    private void connect(String url) throws ServerNotFoundException, UserEmptyException, UserNotConfirmedException, UserBannedException, UserWrongException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException
    {

        this.authStatus = AuthStatus.CONNECTION;

        if(url == null)
        {

            this.userConnected = false;
            this.authStatus = AuthStatus.DISCONNECTED;

            throw new ServerNotFoundException(TrixUtil.log(langDef.getIncoURL()));
        }

        if(this.userName.length() > this.userMaxChar || this.userPassword.length() > this.passwordMaxChar || this.userName == "" || this.userPassword == "" || this.userName == null || this.userPassword == null || this.userName.indexOf(' ') != -1 || this.userPassword.indexOf(' ') != -1)
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

        TrixProfileManager profileManager = new TrixProfileManager(this.urlF, this.userName, this.userPassword);

        /**
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
            this.authStatus = AuthStatus.DISCONNECTED;
            throw new UserBannedException(TrixUtil.log(langDef.getBannedAcc().replace("<ServerName>", this.serverName)));
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
}
