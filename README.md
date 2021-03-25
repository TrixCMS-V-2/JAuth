# JAuth
JAuth is an library for using custom auth between java and TrixCMS. (For exemple : For your modded minecraft project...)

> For using, see the [Demo.java](https://github.com/TrixCMS-V-2/JAuth/blob/master/src/main/java/fr/antoineok/jauth/Demo.java)

#### Warning : You need install the "Auth" Plugin on your dashboard https://trixcms.eu/marketplace/resource/plugin/7

<hr>

> Login with password

Using with OpenLauncherLib:
in the auth function:
replace this:

```java
Authenticator authenticator = new Authenticator(Authenticator.MOJANG_AUTH_URL, AuthPoints.NORMAL_AUTH_POINTS);
AuthResponse response = authenticator.authenticate(AuthAgent.MINECRAFT, username, password, "");
authInfos = new AuthInfos(response.getSelectedProfile().getName(), response.getAccessToken(), response.getSelectedProfile().getId());
```

by this:

```java
JAuth auth = new JAuth("NomDuSite", "UrlDuSite");
auth.connect("pseudo", "mot de passe");
switch (auth.getAuthStatus()) {
    case CONNECTED:
        System.out.println("Token: " + auth.getProfile().getToken());
        System.out.println("Uuid: " + auth.getProfile().getUuid());
        System.out.println("Mail: " + auth.getProfile().getUserMail());
		authinfos = new AuthInfos(auth.getProfile().getUserName(), auth.getProfile().getToken(), auth.getProfile().getUuid());
        break;
    case CONNECTION:
        // Connection in progress
        break;
    case DISCONNECTED:
        // Not connected
        break;
}
```

<hr>

> Login with token

```java
JAuth auth = new JAuth("NomDuSite", "UrlDuSite");
auth.validate("pseudo", "token");
System.out.println("Token: " + auth.getProfile().getToken());
System.out.println("Uuid: " + auth.getProfile().getUuid());
System.out.println("Mail: " + auth.getProfile().getUserMail());
```


> refresh the token
```java
JAuth auth = new JAuth("NomDuSite", "UrlDuSite");
auth.refresh("pseudo", "token");
```

