package com.ibairuiz.examples.test;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.portlet.server.request.PortletMockMvcRequestBuilders.action;
import static org.springframework.test.web.portlet.server.request.PortletMockMvcRequestBuilders.render;
import static org.springframework.test.web.portlet.server.result.PortletMockMvcResultMatchers.model;
import static org.springframework.test.web.portlet.server.result.PortletMockMvcResultMatchers.view;
import static org.springframework.test.web.portlet.server.setup.PortletMockMvcBuilders.existingApplicationContext;

import javax.portlet.PortletMode;
import javax.portlet.WindowState;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.ibairuiz.examples.controller.LiferayExampleController;
import com.ibairuiz.examples.persistence.RegistroDAO;
import com.ibairuiz.examples.persistence.beans.Registro;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:test-applicationContext.xml"})
public class LiferayExampleControllerIntegrationTest {

	@Autowired
	private ApplicationContext applicationContext;
	
	@Autowired
	private RegistroDAO registroDAO;
	
	private Logger logger = LoggerFactory.getLogger(LiferayExampleControllerIntegrationTest.class);
	
	/** Constante para comprobación de entorno de test. **/
	private static final String IS_EJECUCION_TEST = "IS_EJECUCION_TEST";

	@BeforeClass
	public static void setUp() {
		System.setProperty(IS_EJECUCION_TEST, "true");
	}

	/**
	 *  Test para el render por defecto
	 * @throws Exception
	 */
	@Test
	public void testRenderForm() throws Exception {
		existingApplicationContext(applicationContext).build()
			.perform(render())
			.andExpect(view().name(LiferayExampleController.INICIO))
			.andReturn();
	}

	/**
	 * Test para envío de formulario sin rellenar.
	 * @throws Exception
	 */
    @Test
    public void testActionEnviarVacio() throws Exception {
        existingApplicationContext(applicationContext)
                .build()
                .perform(action()
                        .mode(PortletMode.VIEW)
                        .windowState(WindowState.NORMAL)
                        .param("action", "registro"))
                .andExpect(model().attributeExists("registroBean"))
                .andExpect(model().attributeHasErrors("registroBean"))
                .andExpect(model().attributeHasFieldErrors(
                		"registroBean",
                		"nombre",
                		"apellidos",
                		"fechaNacimiento",
                		"email",
                		"captchaText"))
                .andReturn();
    }

    /**
     * Test para envío de formulario completo.
     * @throws Exception
     */
    @Test
    public void testActionEnviarCompleto() throws Exception {
    	existingApplicationContext(applicationContext)
    			.build()
    			.perform(action()
                		.param("action", "registro")
                		.param("nombre", "Test")
                		.param("apellidos", "Test")
                		.param("fechaNacimiento", "01/01/1980")
                		.param("email", "test@test.com")
                		.param("captchaText", "0123"))
                .andExpect(model().attributeExists("registroBean"))
                .andExpect(model().attributeHasNoErrors("registroBean"))
                .andReturn();
        testInsercionCorrecta();
    }

    /**
     * Test de inserción correcta en BBDD
     */
	private void testInsercionCorrecta() {
		Registro registro = registroDAO.getRegistroPorId(1);
		logger.debug("Registro recibido: {}", registro);	
		assertEquals("El nombre no es igual", registro.getNombre(), "Test");
		assertEquals("Los apellidos no coinciden", registro.getApellidos(), "Test");
		assertEquals("La fecha no coincide", registro.getFechaNacimiento().toString(), "1980-01-01 00:00:00.0");		
		assertEquals("El email no coincide", registro.getEmail(), "test@test.com");
	}
}