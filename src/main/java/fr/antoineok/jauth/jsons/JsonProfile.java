package fr.antoineok.jauth.jsons;

import com.google.gson.JsonObject;

public class JsonProfile
{
    
    String userName, userMail, userRank, token, uuid, created_at, updated_at, banned_reason;
    
    double money;
    
    boolean accountBanned, accountConfirmed, has_avatar, hasTwoFactorAuth;

    

    public JsonProfile(String userName, String userMail, String userRank, String token, String uuid, String banned_reason, boolean hasTwoFactorAuth, double money, boolean accountBanned, boolean accountConfirmed, boolean has_avatar, String created_at, String updated_at)
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
        this.banned_reason = banned_reason;
        this.has_avatar = has_avatar;
        this.hasTwoFactorAuth = hasTwoFactorAuth;
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

    public String getBanned_reason() {
        return banned_reason;
    }

    public boolean isHas_avatar() {
        return has_avatar;
    }

    public boolean isHasTwoFactorAuth() {
        return hasTwoFactorAuth;
    }

    public String toString(){
        JsonObject profile = new JsonObject();
        profile.addProperty("username", getUserName());
        profile.addProperty("user mail", getUserMail());
        profile.addProperty("user rank", getUserRank());
        profile.addProperty("uuid", getUuid());
        profile.addProperty("token", getToken());
        profile.addProperty("user money", getMoney());
        profile.addProperty("isBanned", isAccountBanned());
        if(isAccountBanned())
            profile.addProperty("Ban Reason", getBanned_reason());
        profile.addProperty("2fa", isHasTwoFactorAuth());
        profile.addProperty("has custom skin", isHas_avatar());
        profile.addProperty("isConfirmed", isAccountConfirmed());
        profile.addProperty("creation date", getCreated_at());
        profile.addProperty("last update date", getUpdated_at());
        return profile.toString();
    }
    
}
