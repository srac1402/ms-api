package com.ms.model.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "strLogin")
@Entity
@Table(name = "TB_LOGIN", schema = "SC_MS")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Login implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "STR_LOGIN", length = 20)
	private String strLogin;

	@Column(name = "STR_SENHA", length = 350)
	private String strSenha;

	@JsonProperty
	@OneToOne(mappedBy = "login", fetch = FetchType.LAZY)
	private Usuario usuario;

}
