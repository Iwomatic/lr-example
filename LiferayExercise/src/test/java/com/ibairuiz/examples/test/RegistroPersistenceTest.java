package com.ibairuiz.examples.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ibairuiz.examples.persistence.RegistroDAO;
import com.ibairuiz.examples.persistence.beans.Registro;

/**
 * Clase que testea el CRUD de la entidad Registro
 * @author ibai.ruiz
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-applicationContext.xml"})
public class RegistroPersistenceTest {
	
	/** Instancia del logger. **/
	Logger logger = LoggerFactory.getLogger(RegistroPersistenceTest.class);
			
	@Autowired
	private RegistroDAO registroDAO;
	
	/** Instancia de SimpleDateFormat con la fecha. **/
	private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	/**
	 * Método que prueba la inserción de un registro.
	 */
	@Test
	public void testInsercionRegistro() {
		Registro registro = new Registro();
		registro.setNombre("Usuario");
		registro.setApellidos("Apellidos");
		registro.setEmail("usuario@zzzzzz.com");
		try {
			registro.setFechaNacimiento(dateFormat.parse("01/01/1980"));
			registro.setFechaRegistro(new Date());
		} catch (ParseException e) {
			logger.error("Error de parseo de fecha");
		}

		long id = registroDAO.crearRegistro(registro);
		assertNotNull(id);
		assertNotEquals("El id es 0", 0, id);
	}
	
	/**
	 * Método que prueba la recuperación de un registro.
	 */
	@Test
	public void testRecuperacionRegistro() {
		Registro registro = new Registro();
		registro.setNombre("Usuario2");
		registro.setApellidos("Apellidos2");
		registro.setEmail("usuario@yyyyyy.com");
		try {
			registro.setFechaNacimiento(dateFormat.parse("01/01/1981"));
			registro.setFechaRegistro(new Date());
		} catch (ParseException e) {
			logger.error("Error de parseo de fecha");
		}
		
		long id = registroDAO.crearRegistro(registro);
		Registro registroRecuperado = registroDAO.getRegistroPorId(id);
		assertEquals("El nombre no coincide", registro.getNombre(), registroRecuperado.getNombre());
		assertEquals("Los apellidos no coinciden", registro.getApellidos(), registroRecuperado.getApellidos());
		assertEquals("El email no coincide", registro.getEmail(), registroRecuperado.getEmail());
		assertEquals("La fecha de nacimiento no coincide", registro.getFechaNacimiento(), registroRecuperado.getFechaNacimiento());
		assertEquals("La fecha de registro no coincide", registro.getFechaRegistro(), registroRecuperado.getFechaRegistro());
	}
	
	/**
	 * Método que prueba la actualización de un registro.
	 */
	@Test
	public void testActualizacionRegistro() {
		Registro registro = new Registro();
		registro.setNombre("Usuario3");
		registro.setApellidos("Apellidos3");
		registro.setEmail("usuario@xxxxx.com");
		try {
			registro.setFechaNacimiento(dateFormat.parse("01/01/1981"));
			registro.setFechaRegistro(new Date());
		} catch (ParseException e) {
			logger.error("Error de parseo de fecha");
		}
		
		long id = registroDAO.crearRegistro(registro);
		Registro registroAActualizar = registroDAO.getRegistroPorId(id);
		registroAActualizar.setNombre("Usuario4");
		registroAActualizar.setApellidos("Apellidos4");
		registroAActualizar.setEmail("usuario4@xxxxx.com");
		try {
			registroAActualizar.setFechaNacimiento(dateFormat.parse("02/02/1982"));
			registroAActualizar.setFechaRegistro(new Date());
		} catch (ParseException e) {
			logger.error("Error de parseo de fecha");
		}
		
		registroDAO.actualizarRegistro(registroAActualizar);
		Registro registroActualizado = registroDAO.getRegistroPorId(id);
		assertEquals("El nombre no coincide", registroAActualizar.getNombre(), registroActualizado.getNombre());
		assertEquals("Los apellidos no coinciden", registroAActualizar.getApellidos(), registroActualizado.getApellidos());
		assertEquals("El email no coincide", registroAActualizar.getEmail(), registroActualizado.getEmail());
		assertEquals("La fecha de nacimiento no coincide", registroAActualizar.getFechaNacimiento(), registroActualizado.getFechaNacimiento());
		assertEquals("La fecha de registro no coincide", registroAActualizar.getFechaRegistro(), registroActualizado.getFechaRegistro());
	}

	/**
	 * Método que prueba el borrado de un registro.
	 */
	@Test
	public void testBorradoRegistro() {
		Registro registro = new Registro();
		registro.setNombre("Usuario5");
		registro.setApellidos("Apellidos5");
		registro.setEmail("usuario@xxxxx.com");
		try {
			registro.setFechaNacimiento(dateFormat.parse("01/01/1985"));
			registro.setFechaRegistro(new Date());
		} catch (ParseException e) {
			logger.error("Error de parseo de fecha");
		}
		
		long id = registroDAO.crearRegistro(registro);
		registroDAO.borrarRegistro(id);
		Registro registroRecuperado = registroDAO.getRegistroPorId(id);
		assertNull(registroRecuperado);
	}
}
