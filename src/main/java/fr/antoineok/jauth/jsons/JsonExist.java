package fr.antoineok.jauth.jsons;

public class JsonExist
{    
    public boolean exist;
    
    private JsonProfile profile;
    
    public JsonExist(boolean exist, JsonProfile profile)
    {
        this.exist = exist;
        this.profile = profile;
    }
    
    public boolean exist() {
        return exist;
    }
    
    public JsonProfile getProfile()
    {
        return profile;
    }
}
