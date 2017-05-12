package com.ibairuiz.examples.services.beans;

public class CuerpoCorreoRegistro {

	private String nombre;
	
	private String apellidos;
	
	public static final String ASUNTO_CORREO_DEFECTO = "Gracias por registrarte en nuestro portlet";
	
	public static final String CABECERA_CORREO_DEFECTO = "<h1>¡Te damos la bienvenida --NOMBRE--!</h1>";

	public static final String CUERPO_CORREO_DEFECTO = "<p>Es un placer tenerte a bordo de este <strong>ejemplo de portlet de registro</strong>, ¡Esperamos que te guste!</p><p>Recibe un saludo,<br/>El equipo del portlet</p>";

	public static final String PIE_CORREO_DEFECTO = "<p>Este correo se ha mandado a --NOMBRE-- --APELLIDOS--, si no eres --NOMBRE--, olvida este correo.</p>";
	
	/**
	 * @param nombreUsuario
	 * @param apellidosUsuario
	 */
	public CuerpoCorreoRegistro(final String nombreUsuario, String apellidosUsuario) {
		this.nombre = nombreUsuario;
		this.apellidos = apellidosUsuario;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return the apellidos
	 */
	public String getApellidos() {
		return apellidos;
	}

	/**
	 * @param apellidos the apellidos to set
	 */
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	/**
	 * 
	 * @return
	 */
	public String getCuerpo() {
		StringBuilder builder = new StringBuilder(CABECERA_CORREO_DEFECTO.replace("--NOMBRE--", this.nombre));
		builder.append(CUERPO_CORREO_DEFECTO);
		builder.append(PIE_CORREO_DEFECTO
				.replaceAll("--NOMBRE--", this.nombre)
				.replaceAll("--APELLIDOS--", this.apellidos));
		return builder.toString();
	}
}
