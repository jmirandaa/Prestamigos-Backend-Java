/**
 * 
 */
package es.jma.prestamos.daos;

import java.util.List;

import es.jma.prestamos.dominio.Deuda;
import es.jma.prestamos.dominio.ResumenDeuda;
import es.jma.prestamos.enums.TipoDeuda;

/**
 * DAO de Deuda
 * @author jmiranda
 *
 */
public interface IDeudaDAO extends ICrudDAO {

	/**
	 * Consultar deuda por id
	 * @param idDeuda
	 * @return
	 */
	public Deuda consultarDeuda (long idDeuda);
	
	/**
	 * Consultar las deudas de un usuario por su email
	 * @param email
	 * @param tipo
	 * @return
	 */
	public List<Deuda> consultarDeudas (String email, TipoDeuda tipo, boolean saldada);
	
	/**
	 * Consultar las deudas de un usuario por su id
	 * @param idUsuario
	 * @param tipo
	 * @return
	 */
	public List<Deuda> consultarDeudas (long idUsuario, TipoDeuda tipo, boolean saldada);
	
	/**
	 * Borrar deuda
	 * @param idDeuda
	 * @throws Exception 
	 */
	public void borrarDeuda (long idDeuda) throws Exception;
	
	/**
	 * Consultar resumen de las deudas de un usuario
	 * @param idUsuario
	 * @return
	 */
	public ResumenDeuda consultarResumen (long idUsuario);
}
