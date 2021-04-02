package com.ms.service.impl;

import java.io.IOException;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.ms.model.entity.Cliente;
import com.ms.service.EmailsService;
import com.ms.util.EmailUtil;

@Service
public class EmailsServiceImpl implements EmailsService {

	@Autowired
	private JavaMailSender mailSender;

	@Override
	public void enviarEmails(final List<Cliente> clientes, final String caminhoAnexo, final String nomeAnexo)
			throws IOException, MessagingException {

		final MimeBodyPart corpoMensagemAnexos = new MimeBodyPart();
		final Multipart multipart = new MimeMultipart();
		if (StringUtils.hasText(caminhoAnexo)) {
			final DataSource source = new FileDataSource(caminhoAnexo);
			corpoMensagemAnexos.setDataHandler(new DataHandler(source));
			corpoMensagemAnexos.setFileName(nomeAnexo);
			multipart.addBodyPart(corpoMensagemAnexos);
		}

//		final Properties properties = System.getProperties();
//		properties.put("mail.smtp.auth", "true");
//		properties.put("mail.smtp.host", EmailUtil.HOST);
//		properties.put("mail.smtp.port", EmailUtil.PORTA);
//		properties.put("mail.smtp.starttls.enable", "true");
//		mailSender.setPassword(EmailUtil.SENHA);
//		mailSender.setUsername(EmailUtil.USUARIO);
//		mailSender.setJavaMailProperties(properties);

		final MimeMessage message = this.mailSender.createMimeMessage();
		int i = 0;
		final InternetAddress[] arrEnderecos = new InternetAddress[clientes.size()];
		for (final Cliente email : clientes) {
			final String emailDoDestinatario = email.getStrEmail();
			if (EmailUtil.validarEmail(emailDoDestinatario)) {
				arrEnderecos[i] = new InternetAddress(emailDoDestinatario);
				i++;
			}
			message.addRecipients(Message.RecipientType.TO, arrEnderecos);
			message.setSubject("Assunto");
			final BodyPart corpoDaMensagemPrincipal = new MimeBodyPart();
			corpoDaMensagemPrincipal.setContent("mensagens", EmailUtil.TIPO_MENSAGEM);
			multipart.addBodyPart(corpoDaMensagemPrincipal);
			message.setContent(multipart);
			this.mailSender.send(message);
		}
		System.out.println("mensagens enviadas...");

	}

//	public static void main(String[] args) {
//		final EmailsResource resource = new EmailsResource();
//
//		final List<Cliente> clientes = Arrays
//				.asList(Cliente.builder().strEmail("sergioricardo.abreucosta@gmail.com").build());
//
//		try {
//			resource.enviarEmails(null, null, clientes);
//			System.out.println("Enviou");
//		} catch (IOException | MessagingException e) {
//			System.out.println("Erro: " + e.getMessage());
//			e.printStackTrace();
//		}
//	}
}
