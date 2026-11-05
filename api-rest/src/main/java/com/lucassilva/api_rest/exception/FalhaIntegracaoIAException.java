package com.lucassilva.api_rest.exception;

public class FalhaIntegracaoIAException extends RuntimeException {

    public FalhaIntegracaoIAException(String mensagem) {
        super(mensagem);
    }

    public FalhaIntegracaoIAException(
            String mensagem,
            Throwable causa
    ) {
        super(mensagem, causa);
    }
}
