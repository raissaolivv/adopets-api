package com.adopets.adopets_api.domain.exception;

public class EntidadeEmUsoException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public EntidadeEmUsoException(String mensagem){
        super(mensagem);
    }
}
