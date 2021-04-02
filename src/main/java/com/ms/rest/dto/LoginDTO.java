package com.ms.rest.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@SuppressWarnings("serial")
public class LoginDTO implements Serializable {

	private String login;
	private String senha;

}
