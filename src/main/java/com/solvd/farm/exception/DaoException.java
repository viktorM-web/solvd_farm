package com.solvd.farm.exception;

public class DaoException extends RuntimeException{

    public DaoException(Throwable throwable){
        super(throwable);
    }
}
