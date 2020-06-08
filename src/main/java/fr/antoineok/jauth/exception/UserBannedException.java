package fr.antoineok.jauth.exception;


public class UserBannedException extends Exception {

	private static final long serialVersionUID = 1L;

	private String message;

	public UserBannedException(String message) {
		this.setMessage(message);
	}

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }
}