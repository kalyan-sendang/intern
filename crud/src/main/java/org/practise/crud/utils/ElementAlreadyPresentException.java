package org.practise.crud.utils;

public class ElementAlreadyPresentException extends RuntimeException{
    public ElementAlreadyPresentException(String message){
        super(message);
    }

}
