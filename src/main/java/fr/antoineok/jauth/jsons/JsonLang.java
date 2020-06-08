package fr.antoineok.jauth.jsons;

public class JsonLang
{
    private String loginP, loginC, decoP, decoC, incoURL, invalidIdent, errCo, compteInex, profileLoad, profileLoadSucc, bannedAcc, accNotVerif;

    public JsonLang(String loginP, String loginC, String incoURL, String invalidIdent, String errCo, String compteInex, String profileLoad, String profileLoadSucc, String bannedAcc, String accNotVerif, String decoP, String decoC)
    {
        this.loginP = loginP;
        this.loginC = loginC;
        this.decoP = decoP;
        this.decoC = decoC;
        this.incoURL = incoURL;
        this.invalidIdent = invalidIdent;
        this.errCo = errCo;
        this.compteInex = compteInex;
        this.profileLoad = profileLoad;
        this.profileLoadSucc = profileLoadSucc;
        this.bannedAcc = bannedAcc;
        this.accNotVerif = accNotVerif;
    }

    public String getDecoP()
    {
        return decoP;
    }

    public String getDecoC()
    {
        return decoC;
    }

    public String getLoginP()
    {
        return loginP;
    }

    public String getLoginC()
    {
        return loginC;
    }

    public String getIncoURL()
    {
        return incoURL;
    }

    public String getInvalidIdent()
    {
        return invalidIdent;
    }

    public String getErrCo()
    {
        return errCo;
    }

    public String getCompteInex()
    {
        return compteInex;
    }

    public String getProfileLoad()
    {
        return profileLoad;
    }

    public String getProfileLoadSucc()
    {
        return profileLoadSucc;
    }

    public String getBannedAcc()
    {
        return bannedAcc;
    }

    public String getAccNotVerif()
    {
        return accNotVerif;
    }
    
}
