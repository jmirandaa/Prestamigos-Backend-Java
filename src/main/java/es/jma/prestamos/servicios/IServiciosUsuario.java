/**
 * 
 */
package es.jma.prestamos.servicios;

import es.jma.prestamos.daos.IUsuarioDAO;
import es.jma.prestamos.dominio.Usuario;

/**
 * Servicio Usuario
 * @author jmiranda
 *
 */
public interface IServiciosUsuario extends IUsuarioDAO {

	/**
	 * Insertar un nuevo usuario
	 * @param usuario
	 * @return
	 * @throws Exception 
	 */
	public long nuevoUsuario(Usuario usuario) throws Exception;
	
	/**
	 * Actualizar los datos de un usuario
	 * @param usuario
	 * @throws Exception
	 */
	public void actualizarUsuario(Usuario usuario) throws Exception;
	
	/**
	 * Añadir usuario "sin registrar"
	 * @param usuario
	 * @param emailOrigen
	 * @return
	 * @throws Exception
	 */
	public long nuevoInvitado (Usuario usuario, String emailOrigen) throws Exception;
	
	/**
	 * Añadir usuario registrado
	 * @param emailDestino
	 * @param emailOrigen
	 * @throws Exception
	 */
	public long nuevoAmigo(String emailDestino, String emailOrigen) throws Exception;
}
