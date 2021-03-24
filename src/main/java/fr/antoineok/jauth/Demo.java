package fr.antoineok.jauth;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import fr.antoineok.jauth.exception.*;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Demo
{
    public static void main(String[] args) {
        new Demo();
    }

    public Demo() {
        String site = "https://trixcms.inovaperf.me/";
        //JAuth auth = new JAuth("DemoSite", site, "antoineok", "test");  // <---- valeur par défaut : 25 chr max pour un mdp/username et les ban/non confirm� ne passe pas,
        //JAuth auth = new JAuth("DemoSite", site, "antoineok", "test", 16, 16);   <---- valeur par défaut : les ban/non confirmé ne passe pas,
        JAuth auth = new JAuth("DemoSite", site, 16, 16, false, true); //  <---- tout est choisi par l'utilisateur
        //(ordre: serverName, url, username, password, userMaxchar, passMaxchar, confirm, ban)

        try {
            System.out.printf("%s %s", "Here is the new Token: ", auth.refresh("test", "test"));
        } catch (UserWrongException | InvalidTokenException | IOException | NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        /*try {
            auth.connect("test", "test");
        } catch (ServerNotFoundException e) {
        	e.printStackTrace();
            System.err.println(e.getMessage()); // <---- URL == null
        } catch (UserEmptyException e) {
        	e.printStackTrace();
            System.err.println(e.getMessage()); // <---- user name || password == null
        } catch (UserNotConfirmedException e) {
        	e.printStackTrace();
            System.err.println(e.getMessage()); // <---- Email address of the user is not confirmed
        } catch (UserBannedException e) {
        	e.printStackTrace();
            System.err.println(e.getMessage()); // <---- User is banned
        } catch (UserWrongException e) {
        	e.printStackTrace();
            System.err.println(e.getMessage()); // <--- Invalid user name or password
        } catch (HttpException e) {
        	e.printStackTrace();
            System.err.println(e.getMessage()); // <--- Site timed out
        } catch(IOException e)
        {
            System.err.println(e.getMessage());
        }

        switch (auth.getAuthStatus()) {
        case CONNECTED:
            System.out.println("Token: " + auth.getProfile().getToken());
            System.out.println("Uuid: " + auth.getProfile().getUuid());
            System.out.println("Mail: " + auth.getProfile().getUserMail());
            break;
        case CONNECTION:
            // Connection in progress
            break;
        case DISCONNECTED:
            // Not connected
            break;
        }

        // Log out //

        auth.disconnect();
        
        
       */
    }
}
