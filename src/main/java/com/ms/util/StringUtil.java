package com.ms.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class StringUtil {

	private StringUtil() {
	}

	public static String encriptar(String str) {
		final String stringEncriptada = new BCryptPasswordEncoder().encode(str);
		return stringEncriptada;
	}

	public static org.springframework.security.crypto.password.PasswordEncoder retornarSenhaSemCriptografia() {
		return new org.springframework.security.crypto.password.PasswordEncoder() {
			@Override
			public String encode(CharSequence charSequence) {
				return charSequence.toString();
			}

			@Override
			public boolean matches(CharSequence charSequence, String s) {
				return charSequence.toString().equals(s);
			}
		};
	}

}
