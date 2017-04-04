/**
 * 
 */
package es.jma.prestamos.servicios;

import es.jma.prestamos.daos.IOperacionDAO;
import es.jma.prestamos.dominio.Operacion;

/**
 * Servicio Operacion
 * @author jmiranda
 *
 */
public interface IServiciosOperacion extends IOperacionDAO {

	/**
	 * Realizar una operación sobre una deuda
	 * @param tipoOperacion
	 * @param cantidad
	 * @param idDeuda
	 * @param idUsuario
	 * @return
	 * @throws Exception 
	 */
	public long nuevaOperacion (Operacion operacion) throws Exception;
}
