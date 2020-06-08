package fr.antoineok.jauth.jsons;

public class JsonData
{
    private String username;
    
    private String password;

    public JsonData(String username, String password)
    {
        super();
        this.username = username;
        this.password = password;
    }

    public String getUsername()
    {
        return username;
    }

    public String getPassword()
    {
        return password;
    }
    
    

}
