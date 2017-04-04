/**
 * 
 */
package es.jma.prestamos.controllers.rest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.jma.prestamos.dominio.RespuestaREST;
import es.jma.prestamos.dominio.Usuario;
import es.jma.prestamos.servicios.IServiciosRecordar;
import es.jma.prestamos.servicios.IServiciosUsuario;
import es.jma.prestamos.utils.UtilLogs;


/**
 * Controlador para servicios de recordar contraseña
 * @author jmiranda
 *
 */

@Controller
public class RecordarController {
	private static final Logger logger = LogManager.getLogger(RecordarController.class);
	
	@Autowired
	IServiciosUsuario serviciosUsuario;
	@Autowired
	IServiciosRecordar serviciosRecordar;
	
	
	/**
	 * Enviar correo con código de seguridad
	 * @param model
	 * @param email
	 * @return
	 */
	 @RequestMapping(value = "recordar/enviarEmail", method = RequestMethod.POST)
	 @ResponseBody
	 public RespuestaREST<Boolean> reset (ModelMap model, @RequestParam("email") String email)
	 {
		 RespuestaREST<Boolean> resp = new RespuestaREST<Boolean>();
		 resp.setCodError(0);
		 resp.setMsgError("OK");
		 resp.setContenido(true);
		 
		 try
		 {
			 serviciosRecordar.crearToken(email);
		 }
		 catch (Exception e)
		 {
			 UtilLogs.error(logger, e);
			 resp.setCodError(-1);
			 resp.setMsgError(e.getMessage());
			 resp.setContenido(false);
		 }
		
		 return resp;
	 }
	 
	 
	 /**
	  * Resetear la contraseña si el token es correcto
	  * @param model
	  * @param usuario
	  * @return
	  */
	 @RequestMapping(value = "recordar/resetearPassword", method = RequestMethod.POST)
	 @ResponseBody
	 public RespuestaREST<Boolean> resetearPassword (ModelMap model, @RequestBody Usuario usuario)
	 {
		 RespuestaREST<Boolean> resp = new RespuestaREST<Boolean>();
		 resp.setCodError(0);
		 resp.setMsgError("OK");
		 resp.setContenido(true);
		 
		 try
		 {
			 serviciosRecordar.reestablecerContraseña(usuario);
		 }
		 catch (Exception e)
		 {
			 UtilLogs.error(logger, e);
			 resp.setCodError(-1);
			 resp.setMsgError(e.getMessage());
			 resp.setContenido(false);
		 }
		
		 return resp;
	 }	 
}
