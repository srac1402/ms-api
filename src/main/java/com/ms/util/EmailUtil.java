package com.ms.util;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.util.StringUtils;

import com.ms.model.entity.Cliente;

public class EmailUtil {

	public static final String USUARIO = "sergioricardo.abreucosta@gmail.com";
	public static final String SENHA = "saga1402";
	public static final String HOST = "smtp.gmail.com";
	public static final String PORTA = "587";

	public static final String TIPO_MENSAGEM = "text/html; charset=UTF-8";

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	private static final Pattern PATTERN = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);

	@Deprecated
	public static void enviarEmails(String caminhoAnexo, String nomeAnexo, List<Cliente> clientes)
			throws IOException, MessagingException {
		final Properties properties = System.getProperties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.host", HOST);
		properties.put("mail.smtp.port", PORTA);
		properties.put("mail.smtp.starttls.enable", "true");
		final Session session = Session.getInstance(properties);
		final Transport transport = session.getTransport("smtp");
		transport.connect(HOST, USUARIO, SENHA);

		final Multipart multipart = new MimeMultipart();
		final MimeBodyPart corpoMensagemAnexos = new MimeBodyPart();
		if (StringUtils.hasText(caminhoAnexo)) {
			final DataSource source = new FileDataSource(caminhoAnexo);
			corpoMensagemAnexos.setDataHandler(new DataHandler(source));
			corpoMensagemAnexos.setFileName(nomeAnexo);
			multipart.addBodyPart(corpoMensagemAnexos);
		}
		final MimeMessage message = new MimeMessage(session);
		int i = 0;
		final InternetAddress[] arrEnderecos = new InternetAddress[clientes.size()];
		for (final Cliente email : clientes) {
			final String emailDoDestinatario = email.getStrEmail();
			message.setFrom(new InternetAddress(USUARIO));
			if (validarEmail(emailDoDestinatario)) {
				arrEnderecos[i] = new InternetAddress(emailDoDestinatario);
				i++;
			}
			message.addRecipients(Message.RecipientType.TO, arrEnderecos);
			message.setSubject("Assunto");
			final BodyPart corpoDaMensagemPrincipal = new MimeBodyPart();
			corpoDaMensagemPrincipal.setContent("mensagens", TIPO_MENSAGEM);
			multipart.addBodyPart(corpoDaMensagemPrincipal);
			message.setContent(multipart);
			transport.sendMessage(message, arrEnderecos);
		}
		System.out.println("mensagens enviadas...");

	}

	public static boolean validarEmail(String email) {
		final Matcher matcher = PATTERN.matcher(email);
		return matcher.matches();
	}

}
