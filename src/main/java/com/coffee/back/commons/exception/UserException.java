package com.coffee.back.commons.exception;

/**
 *
 * @author mont
 */
public class UserException extends Exception{

    public UserException() {
        
    }
    
    public UserException(String userException) {
        super(userException);
    }
    
}