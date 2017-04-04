/**
 * 
 */
package es.jma.prestamos.controllers.web;

import static es.jma.prestamos.constantes.KJSP.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.jma.prestamos.constantes.KMensajes;
import es.jma.prestamos.dominio.Deuda;
import es.jma.prestamos.dominio.Operacion;
import es.jma.prestamos.dominio.Recordar;
import es.jma.prestamos.dominio.ResumenDeuda;
import es.jma.prestamos.dominio.Usuario;
import es.jma.prestamos.enums.TipoDeuda;
import es.jma.prestamos.enums.TipoOperacion;
import es.jma.prestamos.servicios.IServiciosDeuda;
import es.jma.prestamos.servicios.IServiciosOperacion;
import es.jma.prestamos.servicios.IServiciosRecordar;
import es.jma.prestamos.servicios.IServiciosUsuario;
import es.jma.prestamos.utils.UtilCifrado;
import es.jma.prestamos.utils.UtilLogs;
import es.jma.prestamos.validadores.AbstractValidador;
import es.jma.prestamos.validadores.ValidarNuevaDeuda;

/**
 * Controlador para la parte web usando JSTL
 * Se sustituirá por Angular 2
 * En caso de querer usarlo en un entorno de producción, es recomendable
 * crear varios controladores agrupados por funcionalidad 
 * @author jmiranda
 *
 */
@Controller
public class NavegacionController {
	private static final Logger logger = LogManager.getLogger(NavegacionController.class);
	
	@Autowired
	IServiciosUsuario serviciosUsuario;
	@Autowired
	IServiciosDeuda serviciosDeuda;
	@Autowired
	IServiciosOperacion serviciosOperacion;
	@Autowired
	IServiciosRecordar serviciosRecordar;
	
	/**
	 * Página de login
	 * @param model
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView inicioRaiz (Model model, HttpServletRequest req, HttpSession session)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_LOGIN);		
		return respuesta;
	}
	
	/**
	 * Comprobar si los datos son correctos
	 * @param model
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/login.do", method = RequestMethod.POST)
	public ModelAndView comprobarLogin (Model model, HttpServletRequest req, HttpSession session,
			@RequestParam("campo_email") String email, @RequestParam("campo_password") String password)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_LOGIN);
		
		try
		{
			//Comprobar datos
			Usuario usuario = serviciosUsuario.loginUsuario(email, password);
			
			//Si no son correctos, informar de ello
			if (usuario == null)
			{
				req.setAttribute(CLAVE_ERROR, KMensajes.MSG_LOGIN_INCORRECTO);
			}
			//En caso contrario, guardar en la sesión y avanzar
			else
			{
				String emailUsuario = usuario.getEmail();
				long id = usuario.getId();
				session.setAttribute(CLAVE_EMAIL, emailUsuario);
				session.setAttribute(CLAVE_ID_USUARIO, id);
				//La contraseña la guardo para poder cargarla, pero no es recomendable hacerlo
				session.setAttribute(CLAVE_PASSWORD, password);
				
				//Obtener las deudas que me deben
				List<Deuda> deudas = serviciosDeuda.consultarDeudas(id, TipoDeuda.DEBEN, false);
				
				//Pasarlas 
				req.setAttribute(CLAVE_TIPO, TipoDeuda.fromEnum(TipoDeuda.DEBEN));
				respuesta.addObject(CLAVE_DEUDAS, deudas);
				respuesta.setViewName(PAGINA_DEUDAS);
			}
		}
		catch (Exception e)
		{
			UtilLogs.error(logger, e);
			req.setAttribute(CLAVE_ERROR, e.getMessage());			
		}
		return respuesta;
	}
	
	@RequestMapping(value = "/detallesDeuda.do", method = RequestMethod.GET)
	public ModelAndView detallesDeuda (Model model, HttpServletRequest req, HttpSession session,
			@RequestParam("idDeuda") long idDeuda)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_LOGIN);
		
		try
		{
			//Comprobar sessión
			Object idObject = session.getAttribute(CLAVE_ID_USUARIO);
			
			//Si está iniciada, consultar deudas
			if (idObject != null)
			{
				long id = (long) idObject;
				
				//Obtener la deuda seleccionada
				Deuda deuda = serviciosDeuda.consultarDeuda(idDeuda);
				
				//Obtener las operaciones
				Usuario usuario = new Usuario();
				usuario.setId(id);
				List<Operacion> operaciones = serviciosOperacion.consultarOperaciones(idDeuda, TipoOperacion.PAGAR, usuario);
				
				//Pasarlas
				respuesta.addObject(CLAVE_OPERACIONES, operaciones);
				respuesta.addObject(CLAVE_DEUDA, deuda);
				respuesta.setViewName(PAGINA_DETALLES_DEUDA);
			}
		}
		catch (Exception e)
		{
			UtilLogs.error(logger, e);
			req.setAttribute(CLAVE_ERROR, e.getMessage());			
		}
		return respuesta;
	}	
	
	/**
	 * Deudas que me deben
	 * @param model
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deudasDeben.do", method = RequestMethod.GET)
	public ModelAndView deudasDeben (Model model, HttpServletRequest req, HttpSession session)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_LOGIN);
		
		try
		{
			//Comprobar sessión
			Object idObject = session.getAttribute(CLAVE_ID_USUARIO);
			
			//Si está iniciada, consultar deudas
			if (idObject != null)
			{
				long id = (long) idObject;
				
				//Obtener las deudas que me deben
				List<Deuda> deudas = serviciosDeuda.consultarDeudas(id, TipoDeuda.DEBEN, false);
				
				//Pasarlas 
				req.setAttribute(CLAVE_TIPO, TipoDeuda.fromEnum(TipoDeuda.DEBEN));
				respuesta.addObject(CLAVE_DEUDAS, deudas);
				respuesta.setViewName(PAGINA_DEUDAS);
			}
		}
		catch (Exception e)
		{
			UtilLogs.error(logger, e);
			req.setAttribute(CLAVE_ERROR, e.getMessage());			
		}
		return respuesta;
	}	
	
	/**
	 * Deudas que debo
	 * @param model
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/deudasDebo.do", method = RequestMethod.GET)
	public ModelAndView deudasDebo (Model model, HttpServletRequest req, HttpSession session)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_LOGIN);
		
		try
		{
			//Comprobar sessión
			Object idObject = session.getAttribute(CLAVE_ID_USUARIO);
			
			//Si está iniciada, consultar deudas
			if (idObject != null)
			{
				long id = (long) idObject;
				
				//Obtener las deudas que me deben
				List<Deuda> deudas = serviciosDeuda.consultarDeudas(id, TipoDeuda.DEBO, false);
				
				//Pasarlas 
				req.setAttribute(CLAVE_TIPO, TipoDeuda.fromEnum(TipoDeuda.DEBO));
				respuesta.addObject(CLAVE_DEUDAS, deudas);
				respuesta.setViewName(PAGINA_DEUDAS);
			}
		}
		catch (Exception e)
		{
			UtilLogs.error(logger, e);
			req.setAttribute(CLAVE_ERROR, e.getMessage());			
		}
		return respuesta;
	}
	
	/**
	 * Historial
	 * @param model
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/historial.do", method = RequestMethod.GET)
	public ModelAndView historial (Model model, HttpServletRequest req, HttpSession session)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_LOGIN);
		
		try
		{
			//Comprobar sessión
			Object idObject = session.getAttribute(CLAVE_ID_USUARIO);
			
			//Si está iniciada, consultar deudas
			if (idObject != null)
			{
				long id = (long) idObject;
				
				//Obtener las deudas que me deben
				List<Deuda> deudas = serviciosDeuda.consultarDeudas(id, TipoDeuda.TODAS, true);
				
				//Pasarlas 
				req.setAttribute(CLAVE_TIPO, TipoDeuda.fromEnum(TipoDeuda.TODAS));
				respuesta.addObject(CLAVE_DEUDAS, deudas);
				respuesta.setViewName(PAGINA_HISTORIAL);
			}
		}
		catch (Exception e)
		{
			UtilLogs.error(logger, e);
			req.setAttribute(CLAVE_ERROR, e.getMessage());			
		}
		return respuesta;
	}
	
	/**
	 * Resumen
	 * @param model
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/resumen.do", method = RequestMethod.GET)
	public ModelAndView resumen (Model model, HttpServletRequest req, HttpSession session)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_LOGIN);
		
		try
		{
			//Comprobar sessión
			Object idObject = session.getAttribute(CLAVE_ID_USUARIO);
			
			//Si está iniciada, consultar deudas
			if (idObject != null)
			{
				long id = (long) idObject;
				ResumenDeuda resumenDeuda = serviciosDeuda.consultarResumen(id);
				respuesta.addObject(CLAVE_RESUMEN, resumenDeuda);
				respuesta.setViewName(PAGINA_RESUMEN);
			}
		}
		catch (Exception e)
		{
			UtilLogs.error(logger, e);
			req.setAttribute(CLAVE_ERROR, e.getMessage());			
		}
		return respuesta;
	}
	
	/**
	 * Nueva deuda
	 * @param model
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/nuevaDeuda.do", method = RequestMethod.GET)
	public ModelAndView nuevaDeuda (Model model, HttpServletRequest req, HttpSession session, @RequestParam("tipo") int tipo)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_LOGIN);
		
		try
		{
			//Comprobar sessión
			Object idObject = session.getAttribute(CLAVE_ID_USUARIO);
			
			//Si está iniciada, consultar deudas
			if (idObject != null)
			{
				req.setAttribute(CLAVE_TIPO, tipo);
				respuesta.setViewName(PAGINA_NUEVA_DEUDA);
			}
		}
		catch (Exception e)
		{
			UtilLogs.error(logger, e);
			req.setAttribute(CLAVE_ERROR, e.getMessage());			
		}
		return respuesta;
	}
	
	/**
	 * Crear nueva deuda
	 * @param model
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/nuevaDeudaAccion.do", method = RequestMethod.POST)
	public ModelAndView nuevaDeudaAccion (Model model, HttpServletRequest req, HttpSession session,
			@RequestParam("campo_amigo") String nombreAmigo, @RequestParam("campo_concepto") String concepto,
			@RequestParam("campo_cantidad") double cantidad, @RequestParam("tipo") int tipo)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_NUEVA_DEUDA);
		
		try
		{
			//Comprobar sessión
			Object idObject = session.getAttribute(CLAVE_ID_USUARIO);
			
			//Si está iniciada, consultar deudas
			if (idObject != null)
			{
				long id = (long) idObject;
				String emailOrigen = (String) session.getAttribute(CLAVE_EMAIL);
				long idAmigo = -1;
				TipoDeuda tipoDeuda = TipoDeuda.fromInteger(tipo);
				
				//Validar
				AbstractValidador validador = new ValidarNuevaDeuda(nombreAmigo, concepto, cantidad);
				if (!validador.validar())
				{
					req.setAttribute(CLAVE_ERROR, validador.getMsgError());
				}
				else
				{
					//Si el nombre es una dirección de correo, añadir amigo
					if (nombreAmigo.contains("@"))
					{
						idAmigo = serviciosUsuario.nuevoAmigo(nombreAmigo, emailOrigen);
					}				
					//Si son nombre y apellidos, añadir invitado
					else
					{
						String[] partesNombre = nombreAmigo.split(" ");					
						String nombre = partesNombre[0];
						StringBuffer apellidos = new StringBuffer();
						for (int i=1;i<partesNombre.length;i++)
						{
							if (i != 1)
							{
								apellidos.append(" ");
							}
							apellidos.append(partesNombre[i]);
						}
						Usuario amigo = new Usuario(nombre, apellidos.toString());
						idAmigo = serviciosUsuario.nuevoInvitado(amigo, emailOrigen);
					}
					
					//Crear la deuda
					if (tipoDeuda.equals(TipoDeuda.DEBO))
					{
						serviciosDeuda.nuevaDeuda(id, idAmigo, cantidad, concepto);
					}
					else
					{
						serviciosDeuda.nuevaDeuda(idAmigo, id, cantidad, concepto);
					}
					
					//Obtener las deudas que me deben
					List<Deuda> deudas = serviciosDeuda.consultarDeudas(id, tipoDeuda, false);
					
					//Pasarlas 
					req.setAttribute(CLAVE_TIPO, tipo);
					respuesta.addObject(CLAVE_DEUDAS, deudas);
					respuesta.setViewName(PAGINA_DEUDAS);
				}
			}
		}
		catch (Exception e)
		{
			UtilLogs.error(logger, e);
			req.setAttribute(CLAVE_ERROR, e.getMessage());			
		}
		return respuesta;
	}	
	
	/**
	 * Amigos
	 * @param model
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/amigos.do", method = RequestMethod.GET)
	public ModelAndView amigos (Model model, HttpServletRequest req, HttpSession session)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_LOGIN);
		
		try
		{
			//Comprobar sessión
			Object emailObject = session.getAttribute(CLAVE_EMAIL);
			
			//Si está iniciada, consultar deudas
			if (emailObject != null)
			{
				String email = (String) emailObject;
				
				//Obtener los amigos
				List<Usuario> amigos = serviciosUsuario.consultarAmigos(email);
				
				//Pasarlos 
				respuesta.addObject(CLAVE_AMIGOS, amigos);
				respuesta.setViewName(PAGINA_AMIGOS);
			}
		}
		catch (Exception e)
		{
			UtilLogs.error(logger, e);
			req.setAttribute(CLAVE_ERROR, e.getMessage());			
		}
		return respuesta;
	}
	
	/**
	 * Detalles de amigo
	 * @param model
	 * @param req
	 * @param session
	 * @param idAmigo
	 * @return
	 */
	@RequestMapping(value = "/detallesAmigo.do", method = RequestMethod.GET)
	public ModelAndView detallesAmigo (Model model, HttpServletRequest req, HttpSession session,
			@RequestParam("idAmigo") long idAmigo)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_LOGIN);
		
		try
		{
			//Comprobar sessión
			Object emailObject = session.getAttribute(CLAVE_EMAIL);
			
			//Si está iniciada, consultar deudas
			if (emailObject != null)
			{	
				//Obtener el amigo
				Usuario amigo = serviciosUsuario.consultarUsuario(idAmigo);
				
				//Pasarlos 
				respuesta.addObject(CLAVE_AMIGO, amigo);
				respuesta.setViewName(PAGINA_DETALLES_AMIGO);
			}
		}
		catch (Exception e)
		{
			UtilLogs.error(logger, e);
			req.setAttribute(CLAVE_ERROR, e.getMessage());			
		}
		return respuesta;
	}	

	/**
	 * Editar perfil
	 * @param model
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/perfil.do", method = RequestMethod.GET)
	public ModelAndView perfil (Model model, HttpServletRequest req, HttpSession session)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_LOGIN);
		
		try
		{
			//Comprobar sessión
			Object idObject = session.getAttribute(CLAVE_ID_USUARIO);
			
			//Si está iniciada, consultar deudas
			if (idObject != null)
			{
				long id = (long) idObject;
				
				//Obtener los datos de usuario
				Usuario usuario = serviciosUsuario.consultarUsuario(id);
				
				//Pasarlos 
				respuesta.addObject(CLAVE_USUARIO, usuario);
				respuesta.setViewName(PAGINA_PERFIL);
			}
		}
		catch (Exception e)
		{
			UtilLogs.error(logger, e);
			req.setAttribute(CLAVE_ERROR, e.getMessage());			
		}
		return respuesta;
	}
	
	/**
	 * Página de registro
	 * @param model
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/registro.do", method = RequestMethod.POST)
	public ModelAndView registro (Model model, HttpServletRequest req, HttpSession session)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_REGISTRO);
		
		return respuesta;
	}
	
	/**
	 * Contraseña olvidada
	 * @param model
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/restablecer.do", method = RequestMethod.GET)
	public ModelAndView olvidado (Model model, HttpServletRequest req, HttpSession session)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_RESTABLECER);
		
		return respuesta;
	}	
	
	/**
	 * Cambiar contraseña
	 * @param model
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/cambiarPassword.do", method = RequestMethod.POST)
	public ModelAndView cambiarPassword (Model model, HttpServletRequest req, HttpSession session,
			@RequestParam("campo_email") String email, @RequestParam("campo_codigo") String codigo,
			@RequestParam("campo_password") String password)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_LOGIN);

		try
		{
			Recordar recordar = serviciosRecordar.consultarCodigoNumerico(email, codigo);
			//Si no existe, el código no es válido
			if (recordar == null)
			{
				req.setAttribute(CLAVE_ERROR, KMensajes.MSG_CODIGO_NOVALIDO);
				req.setAttribute(CLAVE_EMAIL, email);
				req.setAttribute(CLAVE_PASSWORD, password);
				respuesta.setViewName(PAGINA_RESTABLECER);				
			}
			else
			{
				Usuario usuario = new Usuario();
				usuario.setNombre(recordar.getToken());
				usuario.setEmail(email);
				usuario.setPassword(password);
				serviciosRecordar.reestablecerContraseña(usuario);
			}
		}
		catch (Exception e)
		{
			UtilLogs.error(logger, e);
			req.setAttribute(CLAVE_ERROR, e.getMessage());
			respuesta.setViewName(PAGINA_RESTABLECER);
		}		
		return respuesta;
	}	
	
	/**
	 * Crear nuevo usuario
	 * @param model
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/nuevoRegistro.do", method = RequestMethod.POST)
	public ModelAndView nuevoRegistro (Model model, HttpServletRequest req, HttpSession session,
			@RequestParam("campo_nombre") String nombre, @RequestParam("campo_apellidos") String apellidos, 
			 @RequestParam("campo_password") String password, @RequestParam("campo_email") String email)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_REGISTRO);
		
		try
		{
			Usuario usuario = new Usuario(nombre, apellidos, email, password);
			serviciosUsuario.nuevoUsuario(usuario);
			respuesta = comprobarLogin(model, req, session, email, password);
		}
		catch (Exception e)
		{
			UtilLogs.error(logger, e);
			req.setAttribute(CLAVE_ERROR, e.getMessage());			
		}
		return respuesta;
	}	
	
	/**
	 * Cerrar sesión
	 * @param model
	 * @param req
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/cerrarSesion.do", method = RequestMethod.GET)
	public ModelAndView cerrarSesion (Model model, HttpServletRequest req, HttpSession session)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_LOGIN);
		
		try
		{
			//Comprobar sessión
			Object emailObject = session.getAttribute(CLAVE_EMAIL);
			
			//Si está iniciada, consultar deudas
			if (emailObject != null)
			{
				session.invalidate();
			}
		}
		catch (Exception e)
		{
			UtilLogs.error(logger, e);
			req.setAttribute(CLAVE_ERROR, e.getMessage());			
		}
		return respuesta;
	}	
	
	/**
	 * Añadir nueva deuda
	 * @param model
	 * @param req
	 * @param session
	 * @param idUsuarioOrigen
	 * @param idDestino
	 * @param cantidad
	 * @param concepto
	 * @param tipo
	 * @return
	 */
	@RequestMapping(value = "/nuevaOp.do", method = RequestMethod.POST)
	public ModelAndView nuevaDeuda (Model model, HttpServletRequest req, HttpSession session,
			@RequestParam("idUsuarioOrigen") long idUsuarioOrigen, 
			 @RequestParam("idDeuda") long idDeuda, @RequestParam("cantidad") double cantidad, 
			 @RequestParam("tipo") int tipo, @RequestParam("tipoOp") int tipoOp)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_LOGIN);
		
		try
		{			
			//Nueva operación
			Usuario usuario = new Usuario();
			usuario.setId(idUsuarioOrigen);
			Deuda deuda = new Deuda();
			deuda.setId(idDeuda);
			Operacion operacion = new Operacion();
			operacion.setCantidad(cantidad);
			operacion.setTipo(TipoOperacion.fromInteger(tipoOp));
			operacion.setUsuario(usuario);
			operacion.setDeuda(deuda);
			serviciosOperacion.nuevaOperacion(operacion);
			
			//Recargar
			TipoDeuda tipoDeuda = TipoDeuda.fromInteger(tipo);
			List<Deuda> deudas = null;
			deudas = serviciosDeuda.consultarDeudas(idUsuarioOrigen, tipoDeuda, false);

			
			//Pasarlas 
			req.setAttribute(CLAVE_TIPO, TipoDeuda.fromEnum(tipoDeuda));
			respuesta.addObject(CLAVE_DEUDAS, deudas);
			respuesta.setViewName(PAGINA_DEUDAS);
		}
		catch (Exception e)
		{
			UtilLogs.error(logger, e);
			req.setAttribute(CLAVE_ERROR, e.getMessage());			
		}
		return respuesta;
	}
	
	/**
	 * Editar perfil
	 * @param model
	 * @param req
	 * @param session
	 * @param idUsuario
	 * @param nombre
	 * @param apellidos
	 * @param password
	 * @return
	 */
	@RequestMapping(value = "/editarPerfil.do", method = RequestMethod.POST)
	public ModelAndView editarPerfil (Model model, HttpServletRequest req, HttpSession session,
			@RequestParam("idUsuario") long idUsuario, 
			 @RequestParam("campo_nombre") String nombre, @RequestParam("campo_apellidos") String apellidos, 
			 @RequestParam("campo_password") String password)
	{
		ModelAndView respuesta = new ModelAndView(PAGINA_LOGIN);
		
		try
		{			
			//Consultar usuario
			Usuario usuario = serviciosUsuario.consultarUsuario(idUsuario);
			if (usuario != null)
			{
				//Editar los datos
				usuario.setNombre(nombre);
				usuario.setApellidos(apellidos);
				usuario.setPassword(UtilCifrado.cifrarTexto(password));
				serviciosUsuario.actualizar(usuario);
				
				//Volver al perfil
				respuesta.addObject(CLAVE_USUARIO, usuario);
				respuesta.setViewName(PAGINA_PERFIL);
			}
		}
		catch (Exception e)
		{
			UtilLogs.error(logger, e);
			req.setAttribute(CLAVE_ERROR, e.getMessage());
		}
		return respuesta;
	}	
}
