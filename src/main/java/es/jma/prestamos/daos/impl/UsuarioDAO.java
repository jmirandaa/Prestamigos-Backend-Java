/**
 * 
 */
package es.jma.prestamos.daos.impl;

import java.util.List;

import org.hibernate.Query;

import es.jma.prestamos.constantes.KInvitados;
import es.jma.prestamos.daos.IUsuarioDAO;
import es.jma.prestamos.dominio.Usuario;
import es.jma.prestamos.utils.UtilCifrado;

/**
 * Implementación del DAO Usuario
 * @author jmiranda
 *
 */
public class UsuarioDAO extends CrudDAOHibImpl implements IUsuarioDAO {
	private static UsuarioDAO instance;
	
	private UsuarioDAO()
	{
		super();
	}
	
	public static UsuarioDAO getInstance()
	{
		if (instance == null)
		{
			instance = new UsuarioDAO();
		}
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see es.jma.prestamos.daos.IUsuarioDAO#loguearse(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean loguearse(String email, String password) {
		boolean respuesta = false;
		
		try
		{
			//Consultar bar por nombre
			String hql = "FROM Usuario WHERE email = :email AND password = :password AND email != :invitado";
			Query query = session.createQuery(hql);
			query.setString("email", email);
			query.setString("invitado", KInvitados.EMAIL_INVITADO);
			query.setString("password", UtilCifrado.cifrarTexto(password));
			@SuppressWarnings("unchecked")
			List<Usuario> results = query.list();
			
			if ((results != null ) && (results.size() > 0))
			{
				respuesta = true;
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return respuesta;
	}

	/* (non-Javadoc)
	 * @see es.jma.prestamos.daos.IUsuarioDAO#consultarUsuario(java.lang.String)
	 */
	@Override
	public Usuario consultarUsuario(String email) {
		Usuario respuesta = null;
		
		try
		{
			//Consultar bar por nombre
			String hql = "from Usuario where email = :email";
			Query query = session.createQuery(hql);
			query.setString("email", email);
			@SuppressWarnings("unchecked")
			List<Usuario> results = query.list();
			
			if ((results != null ) && (results.size() > 0))
			{
				respuesta = results.get(0);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return respuesta;
	}

	/* (non-Javadoc)
	 * @see es.jma.prestamos.daos.IUsuarioDAO#consultarUsuario(java.lang.String, java.lang.String)
	 */
	@Override
	public Usuario consultarUsuario(String nombre, String apellidos) {
		Usuario respuesta = null;
		
		try
		{
			//Consultar bar por nombre
			String hql = "from Usuario where nombre = :nombre and apellidos = :apellidos";
			Query query = session.createQuery(hql);
			query.setString("nombre", nombre);
			query.setString("apellidos", apellidos);
			@SuppressWarnings("unchecked")
			List<Usuario> results = query.list();
			
			if ((results != null ) && (results.size() > 0))
			{
				respuesta = results.get(0);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return respuesta;
	}

	@Override
	public Usuario consultarUsuario(long id) {
		Usuario respuesta = null;
		
		try
		{
			//Consultar bar por nombre
			String hql = "from Usuario where id = :id";
			Query query = session.createQuery(hql);
			query.setLong("id", id);
			@SuppressWarnings("unchecked")
			List<Usuario> results = query.list();
			
			if ((results != null ) && (results.size() > 0))
			{
				respuesta = results.get(0);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return respuesta;
	}

	@Override
	public List<Usuario> consultarAmigos(String email) {
		List<Usuario> respuesta = null;
		
		try
		{
			//Consultar bar por nombre
			String hql = "SELECT u.amigos FROM Usuario u WHERE u.email = :email";
			Query query = session.createQuery(hql);
			query.setString("email", email);
			@SuppressWarnings("unchecked")
			List<Usuario> results = query.list();
			
			if ((results != null ) && (results.size() > 0))
			{
				respuesta = results;
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return respuesta;
	}

	@Override
	public Usuario loginUsuario(String email, String password) {
		Usuario respuesta = null;
		
		try
		{
			//Consultar bar por nombre
			String hql = "FROM Usuario WHERE email = :email AND password = :password AND email != :invitado";
			Query query = session.createQuery(hql);
			query.setString("email", email);
			query.setString("invitado", KInvitados.EMAIL_INVITADO);
			query.setString("password", UtilCifrado.cifrarTexto(password));
			@SuppressWarnings("unchecked")
			List<Usuario> results = query.list();
			
			if ((results != null ) && (results.size() > 0))
			{
				respuesta = results.get(0);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return respuesta;
	
	}

	@Override
	public Usuario consultarAmigo(String nombre, String apellidos, long id) {
		Usuario respuesta = null;
		
		try
		{
			//Consultar bar por nombre
			String hql = "SELECT a from Usuario u INNER JOIN u.amigos a where a.nombre = :nombre and a.apellidos = :apellidos";
			Query query = session.createQuery(hql);
			query.setString("nombre", nombre);
			query.setString("apellidos", apellidos);
			@SuppressWarnings("unchecked")
			List<Usuario> results = query.list();
			
			if ((results != null ) && (results.size() > 0))
			{
				respuesta = results.get(0);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return respuesta;
	}

	@Override
	public Usuario consultarAmigo(String email, long id) {
		Usuario respuesta = null;
		
		try
		{
			//Consultar bar por nombre
			String hql = "SELECT a from Usuario u INNER JOIN u.amigos a WHERE u.id = :id AND a.email = :email";
			Query query = session.createQuery(hql);
			query.setString("email", email);
			query.setLong("id", id);
			@SuppressWarnings("unchecked")
			List<Usuario> results = query.list();
			
			if ((results != null ) && (results.size() > 0))
			{
				respuesta = results.get(0);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return respuesta;
	}

	@Override
	public Boolean borrarAmigo(String email, long idAmigo) {
		Boolean respuesta = false;
		
		try
		{
			//Borrar de amigos		
			String hql = "SELECT u FROM Usuario u WHERE u.email = :emailOrigen";
			Query query = session.createQuery(hql);
			query.setString("emailOrigen", email);
			@SuppressWarnings("unchecked")
			List<Usuario> usuario = (List<Usuario>)query.list();
			if ((usuario != null) && (!usuario.isEmpty()) && (usuario.get(0).getAmigos() != null))
			{
				Usuario usuarioU = usuario.get(0);
				int i=0;
				boolean encontrado = false;
				for (Usuario amigo : usuarioU.getAmigos())
				{
					if (amigo.getId() == idAmigo)
					{
						encontrado = true;
						break;
					}
					i++;
				}
				if (encontrado)
				{
					usuarioU.getAmigos().remove(i);
					session.saveOrUpdate(usuarioU);
					respuesta = true;
				}
				
			}

			
			
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return respuesta;
	}

}
