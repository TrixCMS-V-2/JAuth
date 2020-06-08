package fr.antoineok.jauth;

import java.io.UnsupportedEncodingException;

import fr.antoineok.jauth.exception.HttpException;
import fr.antoineok.jauth.exception.ServerNotFoundException;
import fr.antoineok.jauth.exception.UserBannedException;
import fr.antoineok.jauth.exception.UserEmptyException;
import fr.antoineok.jauth.exception.UserNotConfirmedException;
import fr.antoineok.jauth.exception.UserWrongException;
import fr.antoineok.jauth.jsons.JsonProfile;

public class TrixAuth
{
    private String serverName, urlF;

    private AuthStatus authStatus;

    private boolean userConnected;

    private boolean rejectedBanned;

    private JsonProfile profile;

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

    public TrixAuth(String serverName, String url, String username, String password)
    {
        new TrixAuth(serverName, url, username, password, 25, 25);
    }

    public TrixAuth(String serverName, String url, String username, String password, int userMaxchar, int passMaxchar, boolean confirm, boolean ban)
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
        
    }

    public TrixAuth(String serverName, String url, String username, String password, int userMaxchar, int passMaxchar)
    {
        new TrixAuth(serverName, url, username, password, userMaxchar, passMaxchar, false, false);
    }

    public void disconnect()
    {
        if(!this.userConnected || this.authStatus != AuthStatus.DISCONNECTED)
        {
            return;
        }
        TrixUtil.log("D�connexion en cours...");
        long time = System.currentTimeMillis();
        this.authStatus = AuthStatus.DISCONNECTED;
        this.userConnected = false;
        time = System.currentTimeMillis() - time;
        TrixUtil.log("Déconnexion(" + time + "ms)");
    }

    public void connect() throws ServerNotFoundException, UserEmptyException, UserNotConfirmedException, UserBannedException, UserWrongException, HttpException, UnsupportedEncodingException
    {
        TrixUtil.log("Connexion en cours...");
        long time = System.currentTimeMillis();
        try
        {
            this.connect(this.urlF);
        }
        catch(Exception e)
        {
            
            e.printStackTrace();
        }
        if(this.authStatus != AuthStatus.CONNECTED)
        {
            return;
        }
        time = System.currentTimeMillis() - time;
        TrixUtil.log("Connexion effectuée avec succ�s (" + time + "ms)");
    }

    private void connect(String url) throws Exception
    {

        this.authStatus = AuthStatus.CONNECTION;

        if(url == null)
        {

            this.userConnected = false;
            this.authStatus = AuthStatus.DISCONNECTED;

            throw new ServerNotFoundException("TrixAuth > Impossible de se connecter au serveur !\nURL incorrect.");
        }

        if(this.userName.length() > this.userMaxChar || this.userPassword.length() > this.passwordMaxChar || this.userName == "" || this.userPassword == "" || this.userName == null || this.userPassword == null || this.userName.indexOf(' ') != -1 || this.userPassword.indexOf(' ') != -1)
        {

            this.userConnected = false;
            this.authStatus = AuthStatus.DISCONNECTED;

            throw new UserEmptyException("TrixAuth > Impossible de se connecter au serveur !\nIdentifiants invalides.");
        }

        int responseCode = TrixUtil.pingUrl(url);

        if(!(200 <= responseCode && responseCode <= 399))
        {
            this.userConnected = false;
            this.authStatus = AuthStatus.DISCONNECTED;
            throw new HttpException("TrixAuth > Impossible de se connecter au serveur !\nCela peut etre lié à un problème de connexion/proxy internet ou à un probl�me au niveau du r�seau de " + this.serverName + ".\n\nD�sol� pour le d�sagr�ment.");
            

        }

        TrixProfileManager profileManager = new TrixProfileManager(this.urlF, this.userName, this.userPassword);

        /**
         * Permet de v�rifier si l'utilisateur est bon
         */

        if(!profileManager.isProfileExist())
        {
            this.userConnected = false;
            this.authStatus = AuthStatus.DISCONNECTED;

            throw new UserWrongException("TrixAuth > Impossible de se connecter au serveur !\nCompte Inexistant.");
        }

        TrixUtil.log("Chargement du profile en cours...");
        long time = System.currentTimeMillis();
        /*
         * Permet de r�cup�rer les informations du joueur
         */
        profileManager.loadProfile();
        this.profile = profileManager.getProfile();

        time = System.currentTimeMillis() - time;
        TrixUtil.log("Chargement du profile effectu�e avec succ�s. (" + time + "ms)");
        if(this.rejectedBanned && this.profile.isAccountBanned())
        {
            this.userConnected = false;
            this.authStatus = AuthStatus.DISCONNECTED;
            throw new UserBannedException("TrixAuth > Impossible de se connecter au serveur !\nVous avez �t� banni de " + this.serverName + ".");
        }
        if(this.neededConfirmed && !this.profile.isAccountConfirmed())
        {
            this.userConnected = false;
            this.authStatus = AuthStatus.DISCONNECTED;
            throw new UserNotConfirmedException("TrixAuth > Impossible de se connecter au serveur !\nVeuillez confirmer votre compte avant de vous connecter.");
        }
        this.userConnected = true;

        this.authStatus = AuthStatus.CONNECTED;
    }
}
