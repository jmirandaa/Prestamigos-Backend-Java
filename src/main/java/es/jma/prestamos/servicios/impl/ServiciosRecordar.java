/**
 * 
 */
package es.jma.prestamos.servicios.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import org.hibernate.SessionFactory;

import es.jma.email.AbstractEmail;
import es.jma.email.EmailRecordar;
import es.jma.prestamos.constantes.KMensajes;
import es.jma.prestamos.daos.impl.RecordarDAO;
import es.jma.prestamos.daos.impl.UsuarioDAO;
import es.jma.prestamos.dominio.Recordar;
import es.jma.prestamos.dominio.Usuario;
import es.jma.prestamos.exceptiones.ExcepcionUsuario;
import es.jma.prestamos.servicios.IServiciosRecordar;
import es.jma.prestamos.utils.UtilCifrado;


/**
 * Implementación del servicio Recordar
 * @author jmiranda
 *
 */
public class ServiciosRecordar extends ServiciosHibernate implements
		IServiciosRecordar {
	private static ServiciosRecordar instance;
	private RecordarDAO recordarDAO;
	
	private ServiciosRecordar(SessionFactory sessionFactory)
	{
		super(sessionFactory);
		recordarDAO = RecordarDAO.getInstance();
	}
	
	public static ServiciosRecordar getInstance(SessionFactory sessionFactory)
	{
		if (instance == null)
		{
			instance = new ServiciosRecordar(sessionFactory);
		}
		
		return instance;
	}

	/* (non-Javadoc)
	 * @see es.lyxeon.witwoo.daos.IRememberDAO#consultarToken(java.lang.String)
	 */
	@Override
	public Recordar consultarToken(String email) {
		Recordar resultado = null;
		
		try
		{
			session = initSession();
			tx = initTx(session);
			recordarDAO.setSession(session);
			
			//Hacer la consulta
			resultado = recordarDAO.consultarToken(email);
			commit(tx);
			
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}
		
		return resultado;
	}

	/* (non-Javadoc)
	 * @see es.lyxeon.witwoo.services.IRememberService#crearToken(java.lang.String)
	 */
	@Override
	public String crearToken(String email) throws Exception {
		String token = null;
		try
		{
			UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
			session = initSession();
			tx = initTx(session);
			recordarDAO.setSession(session);			
			usuarioDAO.setSession(session);
			
			//Comprobar que el usuario existe
			Usuario usuarioBBDD = usuarioDAO.consultarUsuario(email);
			if (usuarioBBDD == null)
			{
				throw new ExcepcionUsuario(-2, KMensajes.MSG_ACTUALIZAR_USUARIO_NOEXISTE);
			}			
			
			//Crear token
			Recordar recordar = new Recordar();
			recordar.setEmail(email);
			
			//Fecha actual + 1 dia
			Calendar expireCalendar = Calendar.getInstance();
			expireCalendar.add(Calendar.DAY_OF_MONTH, 1);
			Date expire = expireCalendar.getTime();
			recordar.setExpire(expire);
			
			//Generar token aleatorio
			//token = UUID.randomUUID().toString().replaceAll("-", "");
			Random random = new Random();
			long code= (1000000 + random.nextInt(9000000));
			token = String.valueOf(code);
			recordar.setToken(token);
			
			recordarDAO.nuevo(recordar);
			session.flush();
			
			//Enviar email
			AbstractEmail emailService = new EmailRecordar(email, token);
			emailService.sendMail();		
			commit(tx);
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}
		
		return token;

	}


	@Override
	public String crearCodigoNumerico(String email) throws Exception {
		String token = null;
		try
		{
			Recordar recordar = new Recordar();
			recordar.setEmail(email);
			
			//Fecha actual + 1 dia
			Calendar expireCalendar = Calendar.getInstance();
			expireCalendar.add(Calendar.DAY_OF_MONTH, 1);
			Date expire = expireCalendar.getTime();
			recordar.setExpire(expire);
			
			//Generar número aleatorio
			Random random = new Random();
			long code= (100000 + random.nextInt(900000));
			token = String.valueOf(code);
			recordar.setToken(token);
			
			this.nuevo(recordar);
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return token;
	}

	@Override
	public Recordar consultarCodigoNumerico(String email) {
		Recordar resultado = null;
		
		try
		{
			session = initSession();
			tx = initTx(session);
			recordarDAO.setSession(session);
			
			//Hacer la consulta
			resultado = recordarDAO.consultarCodigoNumerico(email);
			commit(tx);
			
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}
		
		return resultado;
	}

	@Override
	public Recordar consultarCodigoNumerico(String email, String code) {
		Recordar resultado = null;
		
		try
		{
			session = initSession();
			tx = initTx(session);
			recordarDAO.setSession(session);
			
			//Hacer la consulta
			resultado = recordarDAO.consultarCodigoNumerico(email, code);
			commit(tx);
			
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}
		
		return resultado;
	}

	@Override
	public void reestablecerContraseña(Usuario usuario) throws Exception {
		try
		{
			UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
			session = initSession();
			tx = initTx(session);
			recordarDAO.setSession(session);
			usuarioDAO.setSession(session);
			
			//Recuperar datos del perfil
			if (usuario == null)
			{
				throw new ExcepcionUsuario(-2, KMensajes.MSG_ACTUALIZAR_USUARIO_NOEXISTE);
			}			
			
			 String token = usuario.getNombre(); //Usar el nombre como token
			 String email = usuario.getEmail();
			 String password = usuario.getPassword();		
			
			//Consultar si el token es válido
			 Recordar recordar = recordarDAO.consultarToken(email);
			 session.flush();
			 session.clear();
			 if ((recordar == null) || (!recordar.getToken().equals(token)))
			 {
				 throw new ExcepcionUsuario(-4, KMensajes.MSG_CODIGO_NOVALIDO);
			 }
			
			//Si lo es, buscar usuario
			 Usuario usuarioBBDD = usuarioDAO.consultarUsuario(email);
			 session.flush();
			 session.clear();
			 if (usuarioBBDD == null)
			 {
				 throw new ExcepcionUsuario(-5, KMensajes.MSG_ACTUALIZAR_USUARIO_NOEXISTE);
			 }
			 
			 //Actualizar contraseña
			 usuarioBBDD.setPassword(UtilCifrado.cifrarTexto(password));
			 usuarioDAO.actualizar(usuarioBBDD);
			 session.flush();

			 commit(tx);
			
		}
		catch (NullPointerException e)
		{
			throw new ExcepcionUsuario(-3, KMensajes.MSG_ERROR_NULL);
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}
		
	}

}
