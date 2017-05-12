package com.ibairuiz.examples.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.servlet.ServletRequest;
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

import com.ibairuiz.examples.persistence.RegistroDAO;
import com.ibairuiz.examples.persistence.beans.Registro;

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
	
	@ModelAttribute("registroBean")  
	public Registro inicializaRegistro () {  
		return new Registro();  
	}
	
	@InitBinder
	protected void initBinder(WebDataBinder binder) {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
	    binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
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
	/*
	@RenderMapping
	public String defaultRender(@Valid @ModelAttribute("registroBean")Registro registroBean,Map<String, Object> map) {
		logger.debug("Entramos en defaultRender()");
		return INICIO;
	}*/
	
	@RenderMapping(params = "render=resultado")
	public String resultadoRender(@ModelAttribute("registroBean") Registro registroBean, Map<String, Object> map) {
		logger.debug("Entramos en resultado");
		return RESULTADO;
	}
	
	@ActionMapping(params = "action=registro")
	public void registro(@ModelAttribute("registroBean") @Valid Registro registroBean, BindingResult result, ModelMap model,
            ActionRequest request, ActionResponse response, @RequestParam("recaptcha_challenge_field") String challengeField,
			@RequestParam("recaptcha_response_field") String responseField) {
		//String remoteAddress = servletRequest.getRemoteAddr();
		/*ReCaptchaResponse reCaptchaResponse = this.reCaptcha.checkAnswer(remoteAddress, challengeField, responseField);
	    
		if(!reCaptchaResponse.isValid()) {
			result.rejectValue("invalidRecaptcha", "invalid.captcha");
			model.addAttribute("invalidRecaptcha", true);
		}*/
		
		if (!result.hasErrors()) {
			logger.debug("Registro {}", registroBean);
			registroBean.setFechaRegistro(new Date());
			long id = registroDao.crearRegistro(registroBean);
			logger.debug("Registro recogido de BBDD{}", registroDao.getRegistroPorId(id));
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
	
}
