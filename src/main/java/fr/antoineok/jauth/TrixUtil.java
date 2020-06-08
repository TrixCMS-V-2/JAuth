package fr.antoineok.jauth;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

public class TrixUtil
{

    public static Integer pingUrl(String url)
    {
        HttpGet post = new HttpGet(url);

        try(CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse response = httpClient.execute(post))
        {
            int responseCode = response.getStatusLine().getStatusCode();
            //System.out.println(responseCode);
            return(responseCode);
        }
        catch(IOException e)
        {
            
            e.printStackTrace();
        }
        return 500;

    }

    public static String log(String message)
    {
        return "JAuth > " + message;
    }
    
    public static byte[] encrypt(byte[] data, String publicKey) throws Exception {
        byte[] keyBytes = Base64.getDecoder().decode(publicKey.substring(26, publicKey.length() -25).replace(" ", "").replace("\n", ""));
        X509EncodedKeySpec x509KeySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        Key publicK = keyFactory.generatePublic(x509KeySpec);
         // Encrypt data
        Cipher cipher = Cipher.getInstance(keyFactory.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicK);
        int inputLen = data.length;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int offSet = 0;
        byte[] cache;
        int i = 0;
         // encrypt the data segmentation
        while (inputLen - offSet > 0) {
            if (inputLen - offSet > 117) {
                cache = cipher.doFinal(data, offSet, 117);
            } else {
                cache = cipher.doFinal(data, offSet, inputLen - offSet);
            }
            out.write(cache, 0, cache.length);
            i++;
            offSet = i * 117;
        }
        byte[] encryptedData = out.toByteArray();
        out.close();
        return Base64.getEncoder().encode(encryptedData);
    }
    static String getPublicKey(String url)
    {
        HttpPost post3 = new HttpPost(url +"/api/auth/v1/public/key");
        try(CloseableHttpClient httpClient = HttpClients.createDefault(); CloseableHttpResponse response = httpClient.execute(post3))
        {
            String resp = EntityUtils.toString(response.getEntity());
            //System.out.println(resp);
            return resp;
        }
        catch(IOException e)
        {
            return null; 
        }
    }
    

}
