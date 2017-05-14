package com.ibairuiz.examples.services.beans;

/**
 * Bean auxiliar con el cuerpo del correo de registro.
 * @author ibai.ruiz
 *
 */
public class CuerpoCorreoRegistro {

	/** Nombre del usuario registrado. **/
	private String nombre;
	
	/** Apellidos del usuario registrado. **/
	private String apellidos;
	
	/** Asunto del correo de registro. **/
	public static final String ASUNTO_CORREO_DEFECTO = "Gracias por registrarte en nuestro portlet";
	
	/** Cabecera del correo por defecto. **/
	public static final String CABECERA_CORREO_DEFECTO = "<h1>¡Te damos la bienvenida --NOMBRE--!</h1>";

	/** Cuerpo del correo por defecto. **/
	public static final String CUERPO_CORREO_DEFECTO = "<p>Es un placer tenerte a bordo de este <strong>ejemplo de portlet de registro</strong>, ¡Esperamos que te guste!</p><p>Recibe un saludo,<br/>El equipo del portlet</p>";

	/** Pie del correo por defecto. **/
	public static final String PIE_CORREO_DEFECTO = "<p>Este correo se ha mandado a --NOMBRE-- --APELLIDOS--, si no eres --NOMBRE--, olvida este correo.</p>";
	
	/** Constante con la cadena para sustituir el nombre en el cuerpo. **/
	private static final String CADENA_SUSTITUCION_NOMBRE = "--NOMBRE--";
	
	/** Constante con la cadena para sustituir los apellidos en el cuerpo. **/
	private static final String CADENA_SUSTITUCION_APELLIDOS = "--APELLIDOS--";

	/**
	 * Constructor público a partir de un nombre y apellidos de usuario.
	 * @param nombreUsuario Nombre del usuario registrado.
	 * @param apellidosUsuario Apellidos del usuario registrado.
	 */
	public CuerpoCorreoRegistro(final String nombreUsuario, String apellidosUsuario) {
		this.nombre = nombreUsuario;
		this.apellidos = apellidosUsuario;
	}

	/**
	 * Método auxiliar que construye el cuerpo del email
	 * @return
	 */
	public String getCuerpo() {
		StringBuilder builder = new StringBuilder(CABECERA_CORREO_DEFECTO.replace(CADENA_SUSTITUCION_NOMBRE, this.nombre));
		builder.append(CUERPO_CORREO_DEFECTO);
		builder.append(PIE_CORREO_DEFECTO
				.replaceAll(CADENA_SUSTITUCION_NOMBRE, this.nombre)
				.replaceAll(CADENA_SUSTITUCION_APELLIDOS, this.apellidos));
		return builder.toString();
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
}