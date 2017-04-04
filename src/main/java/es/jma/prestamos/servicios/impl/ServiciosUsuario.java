/**
 * 
 */
package es.jma.prestamos.servicios.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import es.jma.prestamos.constantes.KInvitados;
import es.jma.prestamos.constantes.KMensajes;
import es.jma.prestamos.daos.impl.DeudaDAO;
import es.jma.prestamos.daos.impl.OperacionDAO;
import es.jma.prestamos.daos.impl.UsuarioDAO;
import es.jma.prestamos.dominio.Deuda;
import es.jma.prestamos.dominio.Usuario;
import es.jma.prestamos.enums.TipoDeuda;
import es.jma.prestamos.exceptiones.ExcepcionUsuario;
import es.jma.prestamos.servicios.IServiciosUsuario;
import es.jma.prestamos.utils.UtilCifrado;

/**
 * Implementación del servicio Usuario
 * @author jmiranda
 *
 */
public class ServiciosUsuario extends ServiciosHibernate implements IServiciosUsuario {
	private static ServiciosUsuario instance;
	private UsuarioDAO usuarioDAO;
	
	private ServiciosUsuario(SessionFactory sessionFactory)
	{
		super(sessionFactory);
		usuarioDAO = UsuarioDAO.getInstance();
	}
	
	public static ServiciosUsuario getInstance(SessionFactory sessionFactory)
	{
		if (instance == null)
		{
			instance = new ServiciosUsuario(sessionFactory);
		}
		
		return instance;
	}

	@Override
	public boolean loguearse(String email, String password) {
		boolean resultado = false;
		Transaction tx = null;
		try
		{
			session = initSession();
			tx = initTx(session);
			
			//Borrar
			usuarioDAO.setSession(session);
			resultado = usuarioDAO.loguearse(email, password);
			
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
	public Usuario consultarUsuario(String email) {
		Usuario resultado = null;
		Transaction tx = null;
		
		try
		{
			session = initSession();
			tx = initTx(session);
			
			//Borrar
			usuarioDAO.setSession(session);
			resultado = usuarioDAO.consultarUsuario(email);
			
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
	public Usuario consultarUsuario(String nombre, String apellidos) {
		Usuario resultado = null;
		Transaction tx = null;
		
		try
		{
			session = initSession();
			tx = initTx(session);
			
			//Borrar
			usuarioDAO.setSession(session);
			resultado = usuarioDAO.consultarUsuario(nombre,apellidos);
			
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
	public long nuevoUsuario(Usuario usuario) throws Exception {
		long respuesta = -1;
		
		try
		{
			//Campos obligatorios
			if ((usuario == null) || (usuario.getNombre() == null) || (usuario.getApellidos() == null)
				|| (usuario.getEmail() == null) || (usuario.getPassword() == null))
			{
				throw new ExcepcionUsuario(-3, KMensajes.MSG_ERROR_NULL);
			}
			
			//Comprobar si ya existe
			Usuario usuarioBBDD = this.consultarUsuario(usuario.getEmail());
			if (usuarioBBDD != null)
			{
				throw new ExcepcionUsuario(-2, KMensajes.MSG_NUEVO_USUARIO_EXISTE);
			}
			//En caso de que no, registrarlo
			else
			{
				//Establecer fecha de registro
				usuario.setFechaRegistroN(LocalDateTime.now());
				
				//Cifrar password
				String password = usuario.getPassword();
				usuario.setPassword(UtilCifrado.cifrarTexto(password));
				usuario.setRegistrado(true);
				
				//Guardarlo en la base de datos
				this.nuevo(usuario);
				
				//Devolver el id
				respuesta = usuario.getId();
			}
		}
		catch (NullPointerException e)
		{
			throw new ExcepcionUsuario(-1,KMensajes.MSG_ERROR_NULL);
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return respuesta;
	}

	@Override
	public void actualizarUsuario(Usuario usuario) throws Exception {
		Session session = null;
		Transaction tx = null;
		
		try
		{
			//Datos de conexión
			session = initSession();
			tx = initTx(session);
			
			UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
			usuarioDAO.setSession(session);
			
			//Comprobar si ya existe
			long idUsuario = usuario.getId();
			Usuario usuarioBBDD = usuarioDAO.consultarUsuario(idUsuario);
			if (usuarioBBDD == null)
			{
				throw new ExcepcionUsuario(-2, KMensajes.MSG_ACTUALIZAR_USUARIO_NOEXISTE);
			}
			//En caso de que no, registrarlo
			else
			{
				//Actualizar campos permitidos
				usuarioBBDD.setNombre(usuario.getNombre());
				usuarioBBDD.setApellidos(usuario.getApellidos());
				usuarioBBDD.setAvatarBase64(usuario.getAvatarBase64());
				usuarioBBDD.setPassword(UtilCifrado.cifrarTexto(usuario.getPassword()));
				
				//Guardarlo en la base de datos
				usuarioDAO.actualizar(usuarioBBDD);
				commit(tx);
			}
		}
		catch (NullPointerException e)
		{
			throw new ExcepcionUsuario(-1,KMensajes.MSG_ERROR_NULL);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	@Override
	public long nuevoInvitado(Usuario usuario, String emailOrigen) throws Exception {
		long respuesta = -1;
		Session session = null;
		Transaction tx = null;
		
		try
		{
			//Datos de conexión
			session = initSession();
			tx = initTx(session);
			
			UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
			usuarioDAO.setSession(session);
			
			//Comprobar campos obligatorios
			String nombreUsuario = usuario.getNombre();
			String apellidosUsuario = usuario.getApellidos();
			
			if ((nombreUsuario == null) || (apellidosUsuario == null))
			{
				throw new ExcepcionUsuario(-1,KMensajes.MSG_ERROR_NULL);
			}
			
			//Comprobar que existe el usuario origen
			Usuario usuarioBBDD = usuarioDAO.consultarUsuario(emailOrigen);
			if (usuarioBBDD == null)
			{
				throw new ExcepcionUsuario(-2, KMensajes.MSG_ACTUALIZAR_USUARIO_NOEXISTE);
			}			
			else
			{
				//Si el destino existe, no añadir
				Usuario usuarioAmigo = usuarioDAO.consultarAmigo(nombreUsuario, apellidosUsuario, usuarioBBDD.getId());
				if (usuarioAmigo == null)
				{				
					//Establecer email
					usuario.setEmail(KInvitados.EMAIL_INVITADO);
					
					//Establecer fecha de registro
					usuario.setFechaRegistroN(LocalDateTime.now());
					usuario.setRegistrado(false);
						
					//Cifrar password
					String password = KInvitados.PASSWORD_INVITADO;
					usuario.setPassword(UtilCifrado.cifrarTexto(password));
						
					//Guardarlo en la base de datos
					usuarioDAO.nuevo(usuario);
					session.flush();
					session.clear();
						
					//Devolver el id
					respuesta = usuario.getId();
					
					//Añadir a amigos
					List<Usuario> amigos = usuarioBBDD.getAmigos();
					if (amigos == null)
					{
						amigos = new ArrayList<Usuario> ();
					}
					
					amigos.add(usuario);
					usuarioBBDD.setAmigos(amigos);
					
					usuarioDAO.actualizar(usuarioBBDD);
					session.flush();
				}
				else
				{
					respuesta = usuarioAmigo.getId();
				}
				//Commit
				commit(tx);
			}

		}
		catch (NullPointerException e)
		{
			rollback(tx);
			throw new ExcepcionUsuario(-1,KMensajes.MSG_ERROR_NULL);
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}
		
		return respuesta;
	}

	@Override
	public Usuario consultarUsuario(long id) {
		Usuario resultado = null;
		Transaction tx = null;
		
		try
		{
			session = initSession();
			tx = initTx(session);
			
			//Borrar
			usuarioDAO.setSession(session);
			resultado = usuarioDAO.consultarUsuario(id);
			
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
	public List<Usuario> consultarAmigos(String email) {
		List<Usuario> resultado = null;
		Transaction tx = null;
		
		try
		{
			session = initSession();
			tx = initTx(session);
			
			//Borrar
			usuarioDAO.setSession(session);
			resultado = usuarioDAO.consultarAmigos(email);
			
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
	public Usuario loginUsuario(String email, String password) {
		Usuario resultado = null;
		Transaction tx = null;
		
		try
		{
			session = initSession();
			tx = initTx(session);
			
			//Borrar
			usuarioDAO.setSession(session);
			resultado = usuarioDAO.loginUsuario(email, password);
			
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
	public Usuario consultarAmigo(String nombre, String apellidos, long id) {
		Usuario resultado = null;
		Transaction tx = null;
		
		try
		{
			session = initSession();
			tx = initTx(session);
			
			//Borrar
			usuarioDAO.setSession(session);
			resultado = usuarioDAO.consultarAmigo(nombre, apellidos, id);
			
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
	public long nuevoAmigo(String emailDestino, String emailOrigen)
			throws Exception {
		long resultado = -1;
		Session session = null;
		Transaction tx = null;
		
		try
		{
			//Datos de conexión
			session = initSession();
			tx = initTx(session);
			
			UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
			usuarioDAO.setSession(session);
			
			//Comprobar que existan el usuario origen y destino
			Usuario usuarioBBDD = usuarioDAO.consultarUsuario(emailOrigen);
			Usuario usuarioDestino = usuarioDAO.consultarUsuario(emailDestino);
			if ((usuarioBBDD == null) || (usuarioDestino == null))
			{
				throw new ExcepcionUsuario(-2, KMensajes.MSG_ACTUALIZAR_USUARIO_NOEXISTE);
			}			
			else
			{
				resultado = usuarioDestino.getId();
				//Si el destino está en amigos, no añadir
				Usuario usuarioAmigo = usuarioDAO.consultarAmigo(emailDestino, usuarioBBDD.getId());
				if (usuarioAmigo == null)
				{				
					session.flush();
					session.clear();
						
					//Añadir a amigos
					List<Usuario> amigos = usuarioBBDD.getAmigos();
					if (amigos == null)
					{
						amigos = new ArrayList<Usuario> ();
					}
					
					amigos.add(usuarioDestino);
					usuarioBBDD.setAmigos(amigos);
					
					usuarioDAO.actualizar(usuarioBBDD);
					session.flush();
				}
				
				Usuario usuarioAmigoDestino = usuarioDAO.consultarAmigo(emailOrigen, usuarioDestino.getId());
				if (usuarioAmigoDestino == null)
				{				
					session.flush();
					session.clear();
						
					//Añadir a amigos
					List<Usuario> amigos = usuarioDestino.getAmigos();
					if (amigos == null)
					{
						amigos = new ArrayList<Usuario> ();
					}
					
					amigos.add(usuarioBBDD);
					usuarioDestino.setAmigos(amigos);
					
					usuarioDAO.actualizar(usuarioDestino);
					session.flush();
				}				

				//Commit
				commit(tx);
			}

		}
		catch (NullPointerException e)
		{
			rollback(tx);
			throw new ExcepcionUsuario(-1,KMensajes.MSG_ERROR_NULL);
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}
		
		return resultado;
	}

	@Override
	public Usuario consultarAmigo(String email, long id) {
		Usuario resultado = null;
		Transaction tx = null;
		
		try
		{
			session = initSession();
			tx = initTx(session);
			
			//Borrar
			usuarioDAO.setSession(session);
			resultado = usuarioDAO.consultarAmigo(email, id);
			
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
	public Boolean borrarAmigo(String email, long idAmigo) throws Exception {
		Boolean resultado = null;
		Transaction tx = null;
		
		try
		{
			session = initSession();
			tx = initTx(session);
			
			usuarioDAO.setSession(session);
			DeudaDAO deudaDAO = DeudaDAO.getInstance();
			OperacionDAO operacionDAO = OperacionDAO.getInstance();
			operacionDAO.setSession(session);
			deudaDAO.setSession(session);
			
			//Consultar datos de amigo
			Usuario usuario = usuarioDAO.consultarUsuario(idAmigo);
			//Si no existe, excepción
			if (usuario == null)
			{
				throw new ExcepcionUsuario(-2, KMensajes.MSG_ACTUALIZAR_USUARIO_NOEXISTE);
			}
			
			//Si es un invitado, borrar sus deudas
			String emailAmigo = usuario.getEmail();
			boolean invitado = false;
			if (KInvitados.EMAIL_INVITADO.equals(emailAmigo))
			{
				invitado = true;
				//Consultar las deudas del invitado
				List<Deuda> deudasSaldadas = deudaDAO.consultarDeudas(idAmigo, TipoDeuda.TODAS, true);
				if (deudasSaldadas != null)
				{
					for (Deuda deuda : deudasSaldadas)
					{
						long idDeuda = deuda.getId();
						operacionDAO.borrarOperaciones(idDeuda);
						session.flush();
						deudaDAO.borrarDeuda(idDeuda);
						session.flush();
					}
				}
				
				List<Deuda> deudasNoSaldadas = deudaDAO.consultarDeudas(idAmigo, TipoDeuda.TODAS, false);
				if (deudasNoSaldadas != null)
				{
					for (Deuda deuda : deudasNoSaldadas)
					{
						long idDeuda = deuda.getId();
						operacionDAO.borrarOperaciones(idDeuda);
						session.flush();
						deudaDAO.borrarDeuda(idDeuda);
						session.flush();
					}					
				}				
				
			}			
			
			//Borrar de la lista
			resultado = usuarioDAO.borrarAmigo(email, idAmigo);
			session.flush();
			session.clear();
			
			//Si era invitado, borrar su usuario
			if (invitado)
			{
				session.delete(usuario);
				session.flush();
			}
			
			commit(tx);
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}
					
		return resultado;
	}

}
