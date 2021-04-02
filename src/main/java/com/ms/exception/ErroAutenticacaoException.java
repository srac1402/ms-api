package com.ms.exception;

@SuppressWarnings("serial")
public class ErroAutenticacaoException extends RuntimeException {
	public ErroAutenticacaoException(String mensagem) {
		super(mensagem);
	}
}
