package fr.antoineok.jauth.jsons;

public class JsonProfile
{
    
    String userName, userMail, userRank, token, uuid, created_at, updated_at;
    
    double money;
    
    boolean accountBanned, accountConfirmed;

    

    public JsonProfile(String userName, String userMail, String userRank, String token, String uuid, double money, boolean accountBanned, boolean accountConfirmed, String created_at, String updated_at)
    {
        this.userName = userName;
        this.userMail = userMail;
        this.userRank = userRank;
        this.token = token;
        this.uuid = uuid;
        this.money = money;
        this.accountBanned = accountBanned;
        this.accountConfirmed = accountConfirmed;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getUserName()
    {
        return userName;
    }

    public String getUserMail()
    {
        return userMail;
    }

    public String getUserRank()
    {
        return userRank;
    }

    public String getToken()
    {
        return token;
    }

    public String getUuid()
    {
        return uuid;
    }

    public double getMoney()
    {
        return money;
    }

    public boolean isAccountBanned()
    {
        return accountBanned;
    }

    public boolean isAccountConfirmed()
    {
        return accountConfirmed;
    }

    public String getCreated_at()
    {
        return created_at;
    }

    public String getUpdated_at()
    {
        return updated_at;
    }
    
    
    
}
