package com.ibairuiz.examples.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ibairuiz.examples.services.beans.CuerpoCorreoRegistro;

@Service("servicioCorreo")
public class MailService {
	
	Logger logger = LoggerFactory.getLogger(MailService.class);

	@Autowired
	private JavaMailSender gestorEmail; 

	/**
	 * 
	 * @param para
	 * @param de
	 * @param asunto
	 * @param nombreUsuario
	 * @param apellidosUsuario
	 */
	public void enviaCorreoRegistro(final String para, final String de, final String nombreUsuario, final String apellidosUsuario) {
		CuerpoCorreoRegistro cuerpoCorreo = new CuerpoCorreoRegistro(nombreUsuario, apellidosUsuario);
		enviaCorreoSimple(para, de, CuerpoCorreoRegistro.ASUNTO_CORREO_DEFECTO, cuerpoCorreo.getCuerpo());
	}
	
	/**
	 * 
	 * @param para
	 * @param de
	 * @param asunto
	 * @param cuerpo
	 */
	public void enviaCorreoSimple(final String para, final String de, final String asunto, final String cuerpo) {
		try {
			MimeMessage mensajeMIME = gestorEmail.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(mensajeMIME, false, "utf-8");
			mensajeMIME.setContent(cuerpo, "text/html");
			helper.setTo(para);
			helper.setSubject(asunto);
			helper.setFrom(de);
			gestorEmail.send(mensajeMIME);
		} catch (MessagingException e) {
			logger.error("No ha sido posible enviar el correo");
		}
	}

}
