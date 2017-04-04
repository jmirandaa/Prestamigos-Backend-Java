/**
 * 
 */
package es.jma.prestamos.daos;

import java.util.List;

import es.jma.prestamos.dominio.Usuario;

/**
 * DAO de Usuario
 * @author jmiranda
 *
 */
public interface IUsuarioDAO extends ICrudDAO{
	
	/**
	 * Comprobar si con los parámetros pasados el usuario existe
	 * y puede iniciar sesión
	 * @param email
	 * @param password
	 * @return
	 */
	public boolean loguearse (String email, String password);
	
	/**
	 * Devolver los datos del usuario con el email y contraseña
	 * especificado
	 * @param email
	 * @param password
	 * @return
	 */
	public Usuario loginUsuario (String email, String password);
	
	/**
	 * Consultar un usuario por email
	 * @param email
	 * @return
	 */
	public Usuario consultarUsuario (String email);
	
	/**
	 * Consultar usuario por id
	 * @param id
	 * @return
	 */
	public Usuario consultarUsuario (long id);
	
	/**
	 * Consultar un usuario por nombre y apellido
	 * @param nombre
	 * @param apellidos
	 * @return
	 */
	public Usuario consultarUsuario (String nombre, String apellidos);
	
	/**
	 * Consultar los amigos por nombre y apellidos del usuario id
	 * @param nombre
	 * @param apellidos
	 * @param email
	 * @return
	 */
	public Usuario consultarAmigo (String nombre, String apellidos, long id);
	
	public Usuario consultarAmigo (String email, long id);
	
	/**
	 * Consultar listado de amigos
	 * @param email
	 * @return
	 */
	public List<Usuario> consultarAmigos (String email);
	
	/**
	 * Borrar un amigo
	 * @param email
	 * @param idAmigo
	 * @return
	 * @throws Exception 
	 */
	public Boolean borrarAmigo(String email, long idAmigo) throws Exception;

}
