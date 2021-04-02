package com.ms.service;

import java.io.IOException;
import java.util.List;

import javax.mail.MessagingException;

import com.ms.model.entity.Cliente;

public interface EmailsService {

	void enviarEmails(final List<Cliente> clientes, final String caminhoAnexo, final String nomeAnexo)
			throws IOException, MessagingException;

}
