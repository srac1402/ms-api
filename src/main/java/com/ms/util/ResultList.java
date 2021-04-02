package com.ms.util;

import java.io.Serializable;
import java.util.Collection;

public class ResultList<T> implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4433583631417815165L;
	private Collection<T> lista;
	private Number tam;

	public ResultList(Collection<T> lista, Number tam) {
		super();
		this.lista = lista;
		this.tam = tam;
	}

	public Collection<T> getLista() {
		return lista;
	}

	public void setLista(Collection<T> lista) {
		this.lista = lista;
	}

	public Number getTam() {
		return tam;
	}

	public void setTam(Number tam) {
		this.tam = tam;
	}

}
