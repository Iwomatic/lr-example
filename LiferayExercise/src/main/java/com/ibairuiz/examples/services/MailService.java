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

/**
 * Servicio para el envío de correo.
 * @author ibai.ruiz
 *
 */
@Service("servicioCorreo")
public class MailService {
	
	/** Instancia del logger. **/
	Logger logger = LoggerFactory.getLogger(MailService.class);

	/** Gestor email. **/
	@Autowired
	private JavaMailSender gestorEmail; 

	/**
	 * Método que envía el correo de bienvenida de registro.
	 * @param para Dirección de correo de destino.
	 * @param de Dirección de correo de origen.
	 * @param asunto Asunto del correo.
	 * @param nombreUsuario Nombre del usuario que se ha registrado.
	 * @param apellidosUsuario Apellidos del usuario que se ha registrado.
	 */
	public void enviaCorreoRegistro(final String para, final String de, final String nombreUsuario, final String apellidosUsuario) {
		CuerpoCorreoRegistro cuerpoCorreo = new CuerpoCorreoRegistro(nombreUsuario, apellidosUsuario);
		enviaCorreoSimple(para, de, CuerpoCorreoRegistro.ASUNTO_CORREO_DEFECTO, cuerpoCorreo.getCuerpo());
	}
	
	/**
	 * Método que envía un correo simple.
	 * @param para Dirección de correo de destino.
	 * @param de Dirección de correo de origen.
	 * @param asunto Asunto del correo.
	 * @param cuerpo Cuerpo del email, al ser tipo MIME admite HTML.
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