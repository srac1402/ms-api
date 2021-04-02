package com.ms.util;

import java.io.Serializable;

public class ResultObject<T> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 4479175676145209981L;
	private String chave;
	private T objeto;

	public ResultObject(String chave, T objeto) {
		super();
		this.chave = chave;
		this.objeto = objeto;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public T getObjeto() {
		return objeto;
	}

	public void setObjeto(T objeto) {
		this.objeto = objeto;
	}

}
