/**
 * 
 */
package es.jma.prestamos.controllers.rest;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.jma.prestamos.constantes.KMensajes;
import es.jma.prestamos.dominio.Deuda;
import es.jma.prestamos.dominio.RespuestaREST;
import es.jma.prestamos.dominio.ResumenDeuda;
import es.jma.prestamos.enums.TipoDeuda;
import es.jma.prestamos.servicios.IServiciosDeuda;
import es.jma.prestamos.utils.UtilLogs;

/**
 * Controlador para las operaciones relacionadas con las deudas
 * @author jmiranda
 *
 */

@Controller
public class DeudasController {
	private static final Logger logger = LogManager.getLogger(DeudasController.class);
	
	@Autowired
	IServiciosDeuda serviciosDeuda;
	
	/**
	 * Obtener las deudas de un usuario por su email
	 * @param email
	 * @param tipo
	 * @return
	 */
	 @RequestMapping(value = "deudas/todasEmail", method = RequestMethod.GET)
	 @ResponseBody
	 public RespuestaREST<List<Deuda>> todasDeudasEmail (@RequestParam("email") String email, @RequestParam("tipo") int tipo, @RequestParam("saldada") boolean saldada)
	 {
		 RespuestaREST<List<Deuda>> resp = new RespuestaREST<List<Deuda>> ();
		 List<Deuda> deudas = null;
		 resp.setCodError(0);		 
		 
		 try
		 {
			 TipoDeuda tipoDeuda = TipoDeuda.fromInteger(tipo);
			 deudas = serviciosDeuda.consultarDeudas(email, tipoDeuda, saldada);
			 resp.setMsgError(KMensajes.MSG_OK);
			 resp.setContenido(deudas);
			 
		 }
		 catch (Exception e)
		 {
			 UtilLogs.error(logger, e);
			 resp.setCodError(-1);
			 resp.setMsgError(e.getMessage());
			 resp.setContenido(null);
		 }
		 
		 return resp;
	 }
	 
	 /**
	  * Obtener las deudas de un usuario por su id
	  * @param idUsuario
	  * @param tipo
	  * @return
	  */
	 @RequestMapping(value = "deudas/todasId", method = RequestMethod.GET)
	 @ResponseBody
	 public RespuestaREST<List<Deuda>> todasDeudasId (@RequestParam("idUsuario") long idUsuario, @RequestParam("tipo") int tipo, @RequestParam("saldada") boolean saldada)
	 {
		 RespuestaREST<List<Deuda>> resp = new RespuestaREST<List<Deuda>> ();
		 List<Deuda> deudas = null;
		 resp.setCodError(0);		 
		 
		 try
		 {
			 TipoDeuda tipoDeuda = TipoDeuda.fromInteger(tipo);
			 deudas = serviciosDeuda.consultarDeudas(idUsuario, tipoDeuda, saldada);
			 resp.setMsgError(KMensajes.MSG_OK);
			 resp.setContenido(deudas);
			 
		 }
		 catch (Exception e)
		 {
			 UtilLogs.error(logger, e);
			 resp.setCodError(-1);
			 resp.setMsgError(e.getMessage());
			 resp.setContenido(null);
		 }
		 
		 return resp;
	 }	 
	 
	 /**
	  * Añadir nueva deuda
	  * @param emailOrigen
	  * @param idDestino
	  * @param cantidad
	  * @param concepto
	  * @param tipo
	  * @return
	  */
	 @RequestMapping(value = "deudas/deuda", method = RequestMethod.POST)
	 @ResponseBody
	 public RespuestaREST<List<Deuda>> nuevaDeuda (@RequestParam("idUsuarioOrigen") long idUsuarioOrigen, @RequestParam("idDestino") long idDestino, 
			 @RequestParam("cantidad") double cantidad, @RequestParam("concepto") String concepto, @RequestParam("tipo") int tipo)
	 {
		 RespuestaREST<List<Deuda>> resp = new RespuestaREST<List<Deuda>> ();
		 resp.setCodError(0);		 
		 
		 try
		 {
			 //Caso 1: Se introduce el email
			 //Si no está en la lista de amigos, se añade
			 //Después, se crea la nueva deuda
			 
			 //Caso 2: Se introduce nombre y apellido
			 //Si no está en la lista de amigos, se añade
			 //Después, se crea la nueva deuda
			 
			 
			 //Añadir deuda
			 serviciosDeuda.nuevaDeuda(idUsuarioOrigen, idDestino, cantidad, concepto);
			 
			 //Obtener listado actualizado
			 TipoDeuda tipoDeuda = TipoDeuda.fromInteger(tipo);
			 List<Deuda> deudas = null;
			 if (tipoDeuda == TipoDeuda.DEBO)
			 {
				 deudas = serviciosDeuda.consultarDeudas(idUsuarioOrigen, tipoDeuda, false);
			 }
			 else if (tipoDeuda == TipoDeuda.DEBEN)
			 {
				 deudas = serviciosDeuda.consultarDeudas(idDestino, tipoDeuda, false);
			 }
			 			 
			 resp.setMsgError(KMensajes.MSG_OK);
			 resp.setContenido(deudas);
			 
		 }
		 catch (Exception e)
		 {
			 UtilLogs.error(logger, e);
			 resp.setCodError(-1);
			 resp.setMsgError(e.getMessage());
			 resp.setContenido(null);
		 }
		 
		 return resp;
	 }	
	 
	 /**
	  * Actualizar una deuda
	  * @param idDeuda
	  * @param concepto
	  * @return
	  */
	 @RequestMapping(value = "deudas/deuda", method = RequestMethod.PUT)
	 @ResponseBody
	 public RespuestaREST<Boolean> actualizarDeuda (@RequestParam("idDeuda") long idDeuda, @RequestParam("concepto") String concepto)
	 {
		 RespuestaREST<Boolean> resp = new RespuestaREST<Boolean> ();
		 resp.setCodError(0);		 
		 
		 try
		 {
			 serviciosDeuda.actualizarConcepto(idDeuda, concepto);
			 resp.setMsgError(KMensajes.MSG_OK);
			 resp.setContenido(true);
			 
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
	  * Borrar deuda
	  * @param idDeuda
	  * @return
	  */
	 @RequestMapping(value = "deudas/deuda", method = RequestMethod.DELETE)
	 @ResponseBody
	 public RespuestaREST<Boolean> borrarDeuda (@RequestParam("idDeuda") long idDeuda)
	 {
		 RespuestaREST<Boolean> resp = new RespuestaREST<Boolean> ();
		 resp.setCodError(0);		 
		 
		 try
		 {
			 serviciosDeuda.borrarDeuda(idDeuda);
			 resp.setMsgError(KMensajes.MSG_OK);
			 resp.setContenido(true);
			 
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
	  * Obtener el resumen de las deudas de un usuario
	  * @param idUsuario
	  * @return
	  */
	 @RequestMapping(value = "deudas/resumen", method = RequestMethod.GET)
	 @ResponseBody
	 public RespuestaREST<ResumenDeuda> resumenDeudas (@RequestParam("idUsuario") long idUsuario)
	 {
		 RespuestaREST<ResumenDeuda> resp = new RespuestaREST<ResumenDeuda> ();
		 resp.setCodError(0);		 
		 
		 try
		 {
			 ResumenDeuda resumenDeuda = serviciosDeuda.consultarResumen(idUsuario);
			 resp.setMsgError(KMensajes.MSG_OK);
			 resp.setContenido(resumenDeuda);
			 
		 }
		 catch (Exception e)
		 {
			 UtilLogs.error(logger, e);
			 resp.setCodError(-1);
			 resp.setMsgError(e.getMessage());
			 resp.setContenido(null);
		 }
		 
		 return resp;
	 }	 	 
}
