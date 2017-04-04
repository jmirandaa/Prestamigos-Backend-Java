/**
 * 
 */
package es.jma.prestamos.controllers.rest;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import net.coobird.thumbnailator.Thumbnails;

import org.apache.commons.codec.binary.Base64;
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
import es.jma.prestamos.dominio.ImagenWeb;
import es.jma.prestamos.dominio.RespuestaREST;
import es.jma.prestamos.dominio.Usuario;
import es.jma.prestamos.exceptiones.ExcepcionUsuario;
import es.jma.prestamos.servicios.IServiciosUsuario;
import es.jma.prestamos.utils.UtilLogs;

/**
 * Controlador para las operaciones relacionadas con los usuarios
 * @author jmiranda
 *
 */
@Controller
public class UsuariosController {
	private static final Logger logger = LogManager.getLogger(UsuariosController.class);
	
	@Autowired
	IServiciosUsuario serviciosUsuario;
	
	/**
	 * Comprobar los datos de acceso de un usuario
	 * @param email
	 * @param password
	 * @return
	 */
	 @RequestMapping(value = "usuarios/login", method = RequestMethod.GET)
	 @ResponseBody
	 public RespuestaREST<Usuario> login (@RequestParam("email") String email, @RequestParam("password") String password)
	 {
		 RespuestaREST<Usuario> resp = new RespuestaREST<Usuario> ();
		 Usuario resultado = null;
		 resp.setCodError(0);		 
		 
		 try
		 {
			 resultado = serviciosUsuario.loginUsuario(email, password);
			 if (resultado == null)
			 {
				 resp.setMsgError(KMensajes.MSG_LOGIN_INCORRECTO);
				 resp.setCodError(1);
			 }
			 else
			 {
				 resp.setMsgError(KMensajes.MSG_OK);
			 }
		 }
		 catch (Exception e)
		 {
			 UtilLogs.error(logger, e);
			 resp.setCodError(-1);
			 resp.setMsgError(e.getMessage());
		 }
		 
		 resp.setContenido(resultado);
		 
		 return resp;
	 }	

	 /**
	  * Obtener listado de amigos
	  * @param email
	  * @return
	  */
	 @RequestMapping(value = "usuarios/amigos", method = RequestMethod.GET)
	 @ResponseBody
	 public RespuestaREST<List<Usuario>> amigos (@RequestParam("email") String email)
	 {
		 RespuestaREST<List<Usuario>> resp = new RespuestaREST<List<Usuario>> ();
		 resp.setCodError(0);		 
		 
		 try
		 {
			 List<Usuario> resultado = serviciosUsuario.consultarAmigos(email);
			 resp.setMsgError(KMensajes.MSG_OK);
			 resp.setContenido(resultado);
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
	  * Obtener los datos de un usuario
	  * @param email
	  * @return
	  */
	 @RequestMapping(value = "usuarios/usuario", method = RequestMethod.GET)
	 @ResponseBody
	 public RespuestaREST<Usuario> usuarioGet (@RequestParam("email") String email)
	 {
		 RespuestaREST<Usuario> resp = new RespuestaREST<Usuario> ();
		 resp.setCodError(0);		 
		 
		 try
		 {
			 Usuario resultado = serviciosUsuario.consultarUsuario(email);
			 resp.setMsgError(KMensajes.MSG_OK);
			 resp.setContenido(resultado);
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
	  * Registrar usuario
	  * @param usuario
	  * @return
	  */
	 @RequestMapping(value = "usuarios/usuario", method = RequestMethod.POST)
	 @ResponseBody
	 public RespuestaREST<Boolean> nuevoUsuario (@RequestBody Usuario usuario)
	 {
		 RespuestaREST<Boolean> resp = new RespuestaREST<Boolean> ();		 
		 resp.setContenido(false);
		 resp.setCodError(0);
		 
		 try
		 {
			 serviciosUsuario.nuevoUsuario(usuario);
			 			 
			 //Usuario no existe
			 resp.setMsgError(KMensajes.MSG_OK);
			 resp.setContenido(true);		 
			 
		 }
		 catch (ExcepcionUsuario e)
		 {
			 resp.setCodError(e.getCodigo());
			 resp.setMsgError(e.getMessage());
		 }
		 catch (Exception e)
		 {
			 UtilLogs.error(logger, e);
			 resp.setCodError(-1);
			 resp.setMsgError(e.getMessage());
		 }
		 
		 return resp;
	 }
	 
	 /**
	  * Actualizar los datos de un usuario
	  * @param usuario
	  * @return
	  */
	 @RequestMapping(value = "usuarios/usuario", method = RequestMethod.PUT)
	 @ResponseBody
	 public RespuestaREST<Boolean> actualizarUsuario (@RequestBody Usuario usuario)
	 {
		 RespuestaREST<Boolean> resp = new RespuestaREST<Boolean> ();		 
		 resp.setContenido(false);
		 resp.setCodError(0);
		 
		 try
		 {
			 serviciosUsuario.actualizarUsuario(usuario);
			 			 
			 //Si el usuario existe 
			 resp.setMsgError(KMensajes.MSG_OK);
			 resp.setContenido(true);		 
			 
		 }
		 catch (ExcepcionUsuario e)
		 {
			 resp.setCodError(e.getCodigo());
			 resp.setMsgError(e.getMessage());
		 }
		 catch (Exception e)
		 {
			 UtilLogs.error(logger, e);
			 resp.setCodError(-1);
			 resp.setMsgError(e.getMessage());
		 }
		 
		 return resp;
	 }
	 
	 /**
	  * Añadir usuario invitado
	  * @param usuario
	  * @param emailOrigen
	  * @return
	  */
	 @RequestMapping(value = "usuarios/invitado", method = RequestMethod.POST)
	 @ResponseBody
	 public RespuestaREST<Long> nuevoInvitado (@RequestBody Usuario usuario, @RequestParam("emailOrigen") String emailOrigen)
	 {
		 RespuestaREST<Long> resp = new RespuestaREST<Long> ();		 
		 resp.setContenido(-1L);
		 resp.setCodError(0);
		 
		 try
		 {
			 long idInvitado = serviciosUsuario.nuevoInvitado(usuario, emailOrigen);
			 			 
			 //Si el usuario existe 
			 resp.setMsgError(KMensajes.MSG_OK);
			 resp.setContenido(idInvitado);		 
			 
		 }
		 catch (ExcepcionUsuario e)
		 {
			 resp.setCodError(e.getCodigo());
			 resp.setMsgError(e.getMessage());
		 }
		 catch (Exception e)
		 {
			 UtilLogs.error(logger, e);
			 resp.setCodError(-1);
			 resp.setMsgError(e.getMessage());
		 }
		 
		 return resp;
	 }
	 
	 /**
	  * Añadir nuevo amigo
	  * @param emailDestino
	  * @param emailOrigen
	  * @return
	  */
	 
	 @RequestMapping(value = "usuarios/amigo", method = RequestMethod.POST)
	 @ResponseBody	 
	 public RespuestaREST<Long> nuevoAmigo (@RequestParam("emailDestino") String emailDestino, @RequestParam("emailOrigen") String emailOrigen)
	 {
		 RespuestaREST<Long> resp = new RespuestaREST<Long> ();		 
		 resp.setContenido(-1L);
		 resp.setCodError(0);
		 
		 try
		 {
			 long idDestino = serviciosUsuario.nuevoAmigo(emailDestino, emailOrigen);
			 			 
			 //Si el usuario existe 
			 resp.setMsgError(KMensajes.MSG_OK);
			 resp.setContenido(idDestino);		 
			 
		 }
		 catch (ExcepcionUsuario e)
		 {
			 resp.setCodError(e.getCodigo());
			 resp.setMsgError(e.getMessage());
		 }
		 catch (Exception e)
		 {
			 UtilLogs.error(logger, e);
			 resp.setCodError(-1);
			 resp.setMsgError(e.getMessage());
		 }
		 
		 return resp;
	 }	 
	 
	 /**
	  * Borrar a un amigo
	  * @param emailDestino
	  * @param emailOrigen
	  * @return
	  */
	 @RequestMapping(value = "usuarios/amigo", method = RequestMethod.DELETE)
	 @ResponseBody	 
	 public RespuestaREST<Boolean> borrarAmigo (@RequestParam("emailOrigen") String emailOrigen, @RequestParam("idAmigo") long idAmigo)
	 {
		 RespuestaREST<Boolean> resp = new RespuestaREST<Boolean> ();		 
		 resp.setContenido(false);
		 resp.setCodError(0);
		 
		 try
		 {
			 boolean resultado = serviciosUsuario.borrarAmigo(emailOrigen, idAmigo);
			 			 
			 //Si el borrado se ha hecho
			 resp.setMsgError(KMensajes.MSG_OK);
			 resp.setContenido(resultado);		 
			 
		 }
		 catch (ExcepcionUsuario e)
		 {
			 resp.setCodError(e.getCodigo());
			 resp.setMsgError(e.getMessage());
		 }
		 catch (Exception e)
		 {
			 UtilLogs.error(logger, e);
			 resp.setCodError(-1);
			 resp.setMsgError(e.getMessage());
		 }
		 
		 return resp;
	 }		 

	 /**
	  * Reducir una imagen a 300x300
	  * @param imagen
	  * @return
	  */
	 @RequestMapping(value = "reducirImagen", method = RequestMethod.POST)
	 @ResponseBody
	 public RespuestaREST<String> subirImagen(@RequestBody ImagenWeb imagen) {
			RespuestaREST<String> respuesta = new RespuestaREST<String>();
			respuesta.setCodError(0);
			respuesta.setMsgError("OK");
			
			try
			{
					//Cargar imagen
					//String cabecera = imagen.getBase64().split(",")[0];
					//String base64 = imagen.getBase64().split(",")[1];
					byte[] decodeBase64 = Base64.decodeBase64(imagen.getBase64());
					InputStream in = new ByteArrayInputStream(decodeBase64);
					BufferedImage bImageFromConvert = ImageIO.read(in);
					
					//Redimensionar
					BufferedImage newImage = Thumbnails.of(bImageFromConvert)
	                        .size(300, 300)
	                        .asBufferedImage();
					//Volver a bytes
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					//ImageIO.write(newImage, cabecera.split(";")[0].split("/")[1], baos);
					ImageIO.write(newImage, "jpg", baos);
					baos.flush();
					byte[] imageInByte = baos.toByteArray();
					baos.close();
					
					//Base64
					String base64Redimension = Base64.encodeBase64String(imageInByte);
					
					//Actualizarlo
					respuesta.setContenido(base64Redimension);
			}
			catch (Exception e)
			{
				 e.printStackTrace();
				 respuesta.setCodError(-1);
				 respuesta.setMsgError("Sólo se aceptan JPG");
			}
			return respuesta;
		}	 
	 
	//@RequestBody PreguntaAJAX pregunta
}
