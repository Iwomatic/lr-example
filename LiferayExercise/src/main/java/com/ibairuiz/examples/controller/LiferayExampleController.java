package com.ibairuiz.examples.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

import javax.persistence.PersistenceException;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import com.ibairuiz.examples.persistence.RegistroDAO;
import com.ibairuiz.examples.persistence.beans.Registro;
import com.ibairuiz.examples.services.MailService;
import com.ibairuiz.examples.test.LiferayExampleControllerIntegrationTest;
import com.liferay.portal.kernel.captcha.CaptchaUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

/**
 * Controller para el ejemplo. Tiene un handler para la fase de render por defecto,
 * otro para la fase de render del resultado y otro para la acción de envío de correo.
 * También define un handler para el manejo de peticiones "resource", en este caso para el captcha.
 * @author ibai.ruiz
 *
 */
@Controller("liferayExampleController")
@RequestMapping("VIEW")
public class LiferayExampleController {
	
	/** Instancia del logger. **/
	private static final Logger logger = LoggerFactory.getLogger(LiferayExampleController.class);
	
	/** Constante con la vista de inicio. **/
	public static final String INICIO = "inicio";
	
	/** Constante con la vista de resultado. **/
	public static final String RESULTADO = "resultado";
	
	@Autowired
	private RegistroDAO registroDao;
	
	@Autowired
	private MailService servicioCorreo;
	
	@ModelAttribute("registroBean")  
	public Registro inicializaRegistro () {  
		return new Registro();  
	}

	/**
	 * InitBinder para inicializar un formateador para los campos de fecha.
	 * @param binder WebDataBinder
	 */
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/**
	 * Método que maneja la fase de render por defecto.
	 * @param registroBean Instancia de Registro correspondiente al formulario.
	 * @param model Instancia del modelo.
	 * @return ModelAndView
	 */
	@RenderMapping
	public ModelAndView handleRenderRequest(@ModelAttribute("registroBean") Registro registroBean,Model model){
		logger.debug("Entramos en handleRenderRequest()");
		BindingResult errors = (BindingResult) model.asMap().get("errors");
	    if (errors != null) {
	    	logger.error("Hay errores en la fase de render");
	    	model.addAttribute("org.springframework.validation.BindingResult.registroBean", errors);
	    }

		return new ModelAndView(INICIO); 
	}

	/**
	 * Método que maneja la fase de render de "resultado".
	 * @param registroBean Instancia de Registro correspondiente al formulario.
	 * @return
	 */
	@RenderMapping(params = "render=resultado")
	public String resultadoRender(@ModelAttribute("registroBean") Registro registroBean) {
		logger.debug("Entramos en resultado");
		return RESULTADO;
	}
	
	/**
	 * Método que maneja la acción "registro" que corresponde al submit del formulario.
	 * @param registroBean Instancia de Registro con los datos introducidos por el usuario.
	 * @param result instancia BindingResult con el resultado de la validación del bean.
	 * @param model Instancia del modelo.
	 * @param request Instancia de ActionRequest.
	 * @param response Instancia de ActionResponse.
	 */
	@ActionMapping(params = "action=registro")
	public void registro(@ModelAttribute("registroBean") @Valid Registro registroBean, BindingResult result, ModelMap model,
            ActionRequest request, ActionResponse response) {

		//Comprobamos el captcha.
		if(!isCaptchaValido(request)){
			result.rejectValue("captchaText", "captcha.invalido");
		}

		//Si no hay errores, insertamos y mandamos el email.
		if (!result.hasErrors()) {
			logger.debug("Registro {}", registroBean);
			registroBean.setFechaRegistro(new Date());
			long id = 0;
			try {
				id = registroDao.crearRegistro(registroBean);
				model.addAttribute("idRegistro", id);
			} catch (PersistenceException constraintException) {
				result.reject("email.duplicado");
		        model.addAttribute("errors", result);
		        return;	
			}
			
			logger.debug("Registro recogido de BBDD{}", registroDao.getRegistroPorId(id));
			servicioCorreo.enviaCorreoRegistro(registroBean.getEmail(), "usuariodecorreodepalo@gmail.com", registroBean.getNombre(), registroBean.getApellidos());
			response.setRenderParameter("render", "resultado");

	    } else {
	    	logger.debug("Hay errores en el action {}", result.getAllErrors());
	        model.addAttribute("errors", result);
	        return;	    	
	    }
	}
	
	/**
	 * Método que maneja la url "resource" que en este caso sirve la imagen del captcha.
	 * @param resourceRequest Instancia de ResourceRequest.
	 * @param resourceResponse Instancia de ResourceResponse.
	 */
	@ResourceMapping
	public void serveResource(ResourceRequest  resourceRequest, ResourceResponse resourceResponse)  {
		try {
			CaptchaUtil.serveImage(resourceRequest, resourceResponse);
		} catch (IOException e) {
			logger.error("Error al intentar servir el captcha", e);
		}
	}

	/**
	 * Método que comprueba el captcha introducido por el usuario con el introducido en sesión
	 * a la hora de generar la imagen.
	 * @param request Instancia de PortletRequest
	 * @return true si el captcha es válido o false si no lo es.
	 */
    private boolean isCaptchaValido(PortletRequest request) {
    	String captchaIntroducidoUsuario = ParamUtil.getString(request, "captchaText");
    	boolean isValido = true;
    	PortletSession session = request.getPortletSession();
    	checkEntornoTest(session);
    	String captchaSession = getValorCaptchaSesion(session);
    	if (Validator.isNull(captchaSession)
    			|| (captchaSession != null
    			&& !captchaSession.equals(captchaIntroducidoUsuario))) {
    		logger.error("Error interno, el captcha no se encuentra en sesión");
    		isValido = false;
    	}
  
    	return isValido;
    }

    /**
     * Si estamos en entorno de test, añadimos el valor de captcha a la sesión.
     */
    private void checkEntornoTest(PortletSession session) {
    	if (System.getProperty(LiferayExampleControllerIntegrationTest.IS_EJECUCION_TEST) != null 
    			&& Boolean.parseBoolean(System.getProperty(LiferayExampleControllerIntegrationTest.IS_EJECUCION_TEST))) {
    		session.setAttribute("CAPTCHA_TEXT", "0123");
    	}
	}

	/**
     * Método que devuelve el valor del captcha generado.
     * @param session instancia de PortletSession
     * @return Una cadena con el valor del captcha.
     */
    private String getValorCaptchaSesion(PortletSession session) {
    	String valorCaptcha = null;
        Enumeration<String> nombresAtributos = session.getAttributeNames();
        while (nombresAtributos.hasMoreElements()) {
            String nombreAtributo = nombresAtributos.nextElement();
            if (nombreAtributo.contains("CAPTCHA_TEXT")) {
            	valorCaptcha =  (String) session.getAttribute(nombreAtributo);
            }
        }
        return valorCaptcha;
    }
}