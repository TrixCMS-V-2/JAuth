package fr.antoineok.jauth;

import java.io.UnsupportedEncodingException;

import fr.antoineok.jauth.exception.HttpException;
import fr.antoineok.jauth.exception.ServerNotFoundException;
import fr.antoineok.jauth.exception.UserBannedException;
import fr.antoineok.jauth.exception.UserEmptyException;
import fr.antoineok.jauth.exception.UserNotConfirmedException;
import fr.antoineok.jauth.exception.UserWrongException;

public class Demo
{
    public static void main(String[] args) {
        new Demo();
    }

    public Demo() {
        //TrixAuth auth = new TrixAuth("DemoSite", site, "antoineok", "test");  // <---- valeur par défaut : 25 chr max pour un mdp/username et les ban/non confirm� ne passe pas,
        //TrixAuth auth = new TrixAuth("DemoSite", site, "antoineok", "test", 16, 16);   <---- valeur par défaut : les ban/non confirmé ne passe pas,
        TrixAuth auth = new TrixAuth("DemoSite", "http://trixcms.inovaperf.me/", "admin", "123456789", 16, 16, false, true); //  <---- tout est choisi par l'utilisateur 
        //(ordre: serverName, url, username, password, userMaxchar, passMaxchar, confirm, ban)
        try {
            auth.connect();
        } catch (ServerNotFoundException e) {
            System.err.println(e.getMessage()); // <---- URL == null
        } catch (UserEmptyException e) {
            System.err.println(e.getMessage()); // <---- user name || password == null
        } catch (UserNotConfirmedException e) {
            System.err.println(e.getMessage()); // <---- Email address of the user is not confirmed
        } catch (UserBannedException e) {
            System.err.println(e.getMessage()); // <---- User is banned
        } catch (UserWrongException e) {
            System.err.println(e.getMessage()); // <--- Invalid user name or password
        } catch (HttpException e) {
            System.err.println(e.getMessage()); // <--- Site timed out
        }
        catch(UnsupportedEncodingException e)
        {
            
            System.err.println(e.getMessage());
        }

        switch (auth.getAuthStatus()) {
        case CONNECTED:
            System.out.println(auth.getProfile().getToken());
            System.out.println(auth.getProfile().getUuid());
            System.out.println(auth.getProfile().getUserMail());
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
        
        
       
    }
}
