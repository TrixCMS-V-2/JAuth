package fr.antoineok.jauth.exception;

import java.io.IOException;

public class HttpException extends IOException {

	private static final long serialVersionUID = 1L;

	private String message;
	

	public HttpException(String message) {
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