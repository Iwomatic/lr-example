package com.ibairuiz.examples.persistence.beans;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * Bean de Registro, es una entidad JPA, con sus restricciones y validaciones.
 * @author ibai.ruiz
 *
 */
@Entity
@Table(name = "registro")
public class Registro {
	
	/** Columna idRegistro. **/
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private long idRegistro;	

	/** Columna nombre. **/
	@NotBlank
	@Column(name = "nombre")
	private String nombre;

	/** Columna apellidos. **/
	@NotBlank
	@Column(name = "apellidos")
	private String apellidos;
	
	/** Columna fecha de nacimiento. **/
	@NotNull
	@Column(name = "fechaNacimiento")
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private Date fechaNacimiento;
	
	/** Columna email. **/
	@NotBlank
	@Email
	@Column(name = "email", unique= true)
	private String email;
	
	/** Columna fechaRegistro. **/
	@Column(name = "fechaRegistro")
	private Date fechaRegistro;

	/** Texto del captcha, no se persiste en BBDD, pero se utiliza en el bean para validaciones. **/
	private String captchaText;
	
	/**
	 * @return the idRegistro
	 */
	public long getIdRegistro() {
		return idRegistro;
	}

	/**
	 * @param idRegistro the idRegistro to set
	 */
	public void setIdRegistro(long idRegistro) {
		this.idRegistro = idRegistro;
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
	 * @return the fechaNacimiento
	 */
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * @param fechaNacimiento the fechaNacimiento to set
	 */
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the fechaRegistro
	 */
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	/**
	 * @param fechaRegistro the fechaRegistro to set
	 */
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	/**
	 * @return the captchaText
	 */
	public String getCaptchaText() {
		return captchaText;
	}

	/**
	 * @param captchaText the captchaText to set
	 */
	public void setCaptchaText(String captchaText) {
		this.captchaText = captchaText;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Registro [nombre=");
		builder.append(nombre);
		builder.append(", apellidos=");
		builder.append(apellidos);
		builder.append(", fechaNacimiento=");
		builder.append(fechaNacimiento);
		builder.append(", email=");
		builder.append(email);
		builder.append("]");
		return builder.toString();
	}
}
