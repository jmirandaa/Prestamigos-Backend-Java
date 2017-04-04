/**
 * 
 */
package es.jma.prestamos.servicios;

import es.jma.prestamos.daos.IRecordarDAO;
import es.jma.prestamos.dominio.Usuario;

/**
 * Servicio recordar
 * @author jmiranda
 *
 */
public interface IServiciosRecordar extends IRecordarDAO {
	
	/**
	 * Crear un nuevo token
	 * @param email
	 * @return 
	 */
	public String crearToken(String email) throws Exception;
	
	/**
	 * Crear código numérico para registro
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public String crearCodigoNumerico(String email) throws Exception;
	
	/**
	 * Si el token es válido, cambia la contraseña del usuario
	 * @param usuario
	 * @throws Exception 
	 */
	public void reestablecerContraseña(Usuario usuario) throws Exception;
}
