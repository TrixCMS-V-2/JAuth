package fr.antoineok.jauth;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import fr.antoineok.jauth.exception.UserWrongException;
import fr.antoineok.jauth.jsons.JsonData;
import fr.antoineok.jauth.jsons.JsonExist;
import fr.antoineok.jauth.jsons.JsonProfile;

public class TrixProfileManager
{

    private String url, userName, userPassword, eUserName;
    

    private JsonProfile profile;
    public JsonProfile getProfile() {
        return profile;
    }

    public String getUrl()
    {
        return url;
    }
    
    public static String key;
            

    

    public String getUserName()
    {
        return userName;
    }



    public String getUserPassword()
    {
        return userPassword;
    }



    public TrixProfileManager(String url, String userName, String userPassword) throws Exception {
        TrixProfileManager.key = TrixUtil.getPublicKey(url);
        this.url = url;
        this.eUserName = new String(TrixUtil.encrypt(userName.getBytes(), key));
        this.userPassword = userPassword;
        this.userName = userName;
    }
    
    

    Gson gson = new GsonBuilder().disableHtmlEscaping().serializeNulls().create();



    public boolean isProfileExist() throws Exception
    {
        
        String url = this.url + "/api/auth/v1/check";
        HttpPost post = new HttpPost(url);
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("data", eUserName));
        
        post.setEntity(new UrlEncodedFormEntity(urlParameters));
        try(CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse response = httpClient.execute(post))
        {
            String jsonE = EntityUtils.toString(response.getEntity());
            JsonExist exist = gson.fromJson(jsonE, JsonExist.class);
            return exist.exist();
        }
        catch(IOException e)
        {
            return false; 
        }
        
    }



    



    public void loadProfile() throws Exception
    {
       if(!this.isProfileExist()) {
           return;
       }
       String json = gson.toJson(new JsonData(userName, userPassword));
       String eJson = new String(TrixUtil.encrypt(json.getBytes(), key));
       String url2 = this.url + "/api/auth/v1/get";
       HttpPost post2 = new HttpPost(url2);
       List<NameValuePair> urlParameters2 = new ArrayList<>();
       urlParameters2.add(new BasicNameValuePair("data", eJson));
       post2.setEntity(new UrlEncodedFormEntity(urlParameters2));
       try(CloseableHttpClient httpClient2 = HttpClients.createDefault(); CloseableHttpResponse response2 = httpClient2.execute(post2))
       {
           String jsonE2 = EntityUtils.toString(response2.getEntity());
           //System.out.println(jsonE2);
           JsonExist ext = gson.fromJson(jsonE2, JsonExist.class);
           if(!ext.exist()) {
               throw new UserWrongException("Identifients Incorrects");
           }
           profile = ext.getProfile();
       }
       catch(IOException e)
       {
           System.err.println("Impossible d'envoyer la requete: ");
           e.printStackTrace();
       }
    }

    
    
    
}
