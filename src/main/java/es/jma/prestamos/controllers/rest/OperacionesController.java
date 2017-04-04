/**
 * 
 */
package es.jma.prestamos.controllers.rest;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import es.jma.prestamos.constantes.KMensajes;
import es.jma.prestamos.dominio.Deuda;
import es.jma.prestamos.dominio.Operacion;
import es.jma.prestamos.dominio.RespuestaREST;
import es.jma.prestamos.dominio.Usuario;
import es.jma.prestamos.enums.TipoDeuda;
import es.jma.prestamos.enums.TipoOperacion;
import es.jma.prestamos.servicios.IServiciosDeuda;
import es.jma.prestamos.servicios.IServiciosOperacion;
import es.jma.prestamos.utils.UtilLogs;

/**
 * Controlador para las operaciones realizadas sobre una deuda
 * @author jmiranda
 *
 */
@Controller
public class OperacionesController {
	private static final Logger logger = LogManager.getLogger(OperacionesController.class);
	
	@Autowired
	IServiciosOperacion serviciosOperacion;
	@Autowired
	IServiciosDeuda serviciosDeuda;
	
	/**
	 * Consultar todas las operaciones de una deuda
	 * @param idDeuda
	 * @param tipo
	 * @param idUsuario
	 * @return
	 */
	 @RequestMapping(value = "operaciones/todas", method = RequestMethod.GET)
	 @ResponseBody
	 public RespuestaREST<List<Operacion>> todasOperaciones (@RequestParam("idDeuda") long idDeuda, @RequestParam("tipo") int tipo, @RequestParam("idUsuario") int idUsuario)
	 {
		 RespuestaREST<List<Operacion>> resp = new RespuestaREST<List<Operacion>> ();
		 List<Operacion> operaciones = null;
		 resp.setCodError(0);		 
		 
		 try
		 {
			 //Si el id del usuario no es negativo o 0, filtrar por usuario
			 Usuario usuario = null;
			 if (idUsuario > 0)
			 {
				 usuario = new Usuario();
				 usuario.setId(idUsuario);
			 }
			 
			 //Si el tipo de operación a buscar está especificado, filtrar
			 TipoOperacion tipoOperacion = TipoOperacion.fromInteger(tipo);
			 
			 //Realizar la consulta
			 operaciones = serviciosOperacion.consultarOperaciones(idDeuda, tipoOperacion, usuario);
			 resp.setMsgError(KMensajes.MSG_OK);
			 resp.setContenido(operaciones);
			 
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
	  * Nueva operación sobre una deuda.
	  * Se devuelve el listado de deudas actualizado
	  * @param operacion
	  * @param tipo
	  * @return
	  */
	 @RequestMapping(value = "operaciones/operacion", method = RequestMethod.POST)
	 @ResponseBody
	 public RespuestaREST<List<Deuda>> nuevaOperacion (@RequestBody Operacion operacion, @RequestParam("tipoDeuda") int tipo)
	 {
		 RespuestaREST<List<Deuda>> resp = new RespuestaREST<List<Deuda>> ();
		 resp.setCodError(0);		 
		 
		 try
		 {		 
			 //Realizar la insersión
			 serviciosOperacion.nuevaOperacion(operacion);
			 
			 //Actualizar deudas
			 long idUsuario = operacion.getUsuario().getId();
			 TipoDeuda tipoDeuda = TipoDeuda.fromInteger(tipo);
			 List<Deuda> deudas = serviciosDeuda.consultarDeudas(idUsuario, tipoDeuda, false);
			 
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
}
