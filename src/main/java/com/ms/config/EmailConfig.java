package com.ms.config;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.ms.util.EmailUtil;

@Configuration
public class EmailConfig {

	@Bean
	public JavaMailSenderImpl mailSender() {
		final JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
		final Properties properties = System.getProperties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.host", EmailUtil.HOST);
		properties.put("mail.smtp.port", EmailUtil.PORTA);
		properties.put("mail.smtp.starttls.enable", "true");
		javaMailSender.setPassword(EmailUtil.SENHA);
		javaMailSender.setUsername(EmailUtil.USUARIO);
		javaMailSender.setJavaMailProperties(properties);
		return javaMailSender;
	}

}
