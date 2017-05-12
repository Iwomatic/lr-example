package com.ibairuiz.examples.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.bind.annotation.ActionMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import org.springframework.web.portlet.bind.annotation.ResourceMapping;

import com.ibairuiz.examples.persistence.RegistroDAO;
import com.ibairuiz.examples.persistence.beans.Registro;
import com.ibairuiz.examples.services.MailService;
import com.liferay.portal.kernel.captcha.CaptchaUtil;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Validator;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

@Controller("liferayExampleController")
@RequestMapping("VIEW")
public class LiferayExampleController {
	
	@Autowired
	ReCaptchaImpl reCaptcha;
	private static final Logger logger = LoggerFactory.getLogger(LiferayExampleController.class);
	
	private static final String INICIO = "inicio";
	
	private static final String RESULTADO = "resultado";
	
	@Autowired
	private RegistroDAO registroDao;
	
	@Autowired
	private MailService servicioCorreo;
	
	@ModelAttribute("registroBean")  
	public Registro inicializaRegistro () {  
		return new Registro();  
	}

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

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

	@RenderMapping(params = "render=resultado")
	public String resultadoRender(@ModelAttribute("registroBean") Registro registroBean, Map<String, Object> map) {
		logger.debug("Entramos en resultado");
		return RESULTADO;
	}
	
	@ActionMapping(params = "action=registro")
	public void registro(@ModelAttribute("registroBean") @Valid Registro registroBean, BindingResult result, ModelMap model,
            ActionRequest request, ActionResponse response) {

		//ReCaptchaResponse reCaptchaResponse = this.reCaptcha.checkAnswer(remoteAddress, challengeField, responseField);
		if(!checkCaptcha(request)){
			logger.error("Invalid Captcha. Throw some custom exception which you will catch in jsp and display error message");
			result.reject("captchaText", "El captcha es invalido");
		}

		if (!result.hasErrors()) {
			logger.debug("Registro {}", registroBean);
			registroBean.setFechaRegistro(new Date());
			long id = registroDao.crearRegistro(registroBean);
			logger.debug("Registro recogido de BBDD{}", registroDao.getRegistroPorId(id));
			servicioCorreo.enviaCorreoRegistro(registroBean.getEmail(), "usuariodecorreodepalo@gmail.com", registroBean.getNombre(), registroBean.getApellidos());
			response.setRenderParameter("render", "resultado");

	    } else {
	    	logger.debug("Hay errores en el action");
	        model.addAttribute("errors", result);
	        return;	    	
	    }
		
		
		

	    // Returns control to alternative view
		//request.setAttribute("texto", ParamUtil.getString(request, "texto-libre"));
		//response.setRenderParameter("render", "resultado");
	}
	
	@ResourceMapping
	public void serveResource(ResourceRequest  resourceRequest, ResourceResponse resourceResponse)  {
		try {
			CaptchaUtil.serveImage(resourceRequest, resourceResponse);
		} catch (IOException e) {
			logger.error("Error al intentar servir el captcha", e);
		}
	}
	
    private boolean checkCaptcha(PortletRequest request) {
    	String enteredCaptchaText = ParamUtil.getString(request, "captchaText");
    	boolean isValid = true;
    	PortletSession session = request.getPortletSession();
    	String captchaText = getCaptchaValueFromSession(session);
    	if (Validator.isNull(captchaText)) {
    		logger.error("Internal Error! Captcha text not found in session");
    		isValid = false;
    	}
    	if (!captchaText.equals(enteredCaptchaText)) {
    		logger.error("Invalid captcha text. Please reenter.");
    		isValid = false;
    	}
    	return isValid;
    }

    private String getCaptchaValueFromSession(PortletSession session) {
        Enumeration<String> atNames = session.getAttributeNames();
        while (atNames.hasMoreElements()) {
            String name = atNames.nextElement();
            if (name.contains("CAPTCHA_TEXT")) {
                return (String) session.getAttribute(name);
            }
        }
        return null;
    }
}
