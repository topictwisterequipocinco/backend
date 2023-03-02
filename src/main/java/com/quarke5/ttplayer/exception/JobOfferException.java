package com.quarke5.ttplayer.exception;

@SuppressWarnings("serial")
public class JobOfferException extends Exception{

	protected int idError;
	protected String error;
	
	public JobOfferException() {}
	
	public JobOfferException(String message) {
        super(message);
    }

    public String getError(){
	    return this.error;
    }

    protected void setError(String message){
	    this.error = message;
    }
}
