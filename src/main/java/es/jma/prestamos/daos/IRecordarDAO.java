/**
 * 
 */
package es.jma.prestamos.daos;

import es.jma.prestamos.dominio.Recordar;



/**
 * DAO de Recordar
 * @author jmiranda
 *
 */
public interface IRecordarDAO extends ICrudDAO {
	
	/**
	 * Consultar si un token existe y es válido
	 * @param email
	 * @return
	 */
	public Recordar consultarToken(String email);
	
	/**
	 * Consultar si un codigo numérico existe y es válido
	 * @param email
	 * @return
	 */
	public Recordar consultarCodigoNumerico(String email);
	
	public Recordar consultarCodigoNumerico(String email, String code);
}
