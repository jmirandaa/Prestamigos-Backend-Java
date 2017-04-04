/**
 * 
 */
package es.jma.prestamos.servicios;

import es.jma.prestamos.daos.IDeudaDAO;

/**
 * Servicio Deuda
 * @author jmiranda
 *
 */
public interface IServiciosDeuda extends IDeudaDAO {

	/**
	 * Crear una nueva deuda
	 * @param emailOrigen
	 * @param idDestino
	 * @param cantidad
	 * @param concepto
	 * @param tipoDeuda
	 * @return
	 * @throws Exception 
	 */
	public long nuevaDeuda (long idUsuarioOrigen, long idDestino, double cantidad,
			String concepto) throws Exception;
	
	/**
	 * Actualizar el concepto de una deuda
	 * @param idDeuda
	 * @param concepto
	 * @throws Exception 
	 */
	public void actualizarConcepto (long idDeuda, String concepto) throws Exception;

	/**
	 * Borrar deuda
	 * @param idDeuda
	 * @throws Exception
	 */
	public void borrarDeuda(long idDeuda) throws Exception;
}
