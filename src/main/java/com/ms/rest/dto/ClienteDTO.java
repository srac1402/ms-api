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
public class ClienteDTO implements Serializable {

	private Long id;
	private String nome;
	private String email;

}
