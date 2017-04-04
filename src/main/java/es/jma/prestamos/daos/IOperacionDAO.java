/**
 * 
 */
package es.jma.prestamos.daos;

import java.util.List;

import es.jma.prestamos.dominio.Operacion;
import es.jma.prestamos.dominio.Usuario;
import es.jma.prestamos.enums.TipoOperacion;

/**
 * DAO de Operacion
 * @author jmiranda
 *
 */
public interface IOperacionDAO extends ICrudDAO {
	
	/**
	 * Consultar las operaciones de una deuda
	 * @param idDeuda
	 * @param tipoOperacion Si es null se buscan todas
	 * @param usuario Si es null se buscan de todos los usuarios
	 * @return
	 */
	public List<Operacion> consultarOperaciones (long idDeuda, TipoOperacion tipoOperacion, Usuario usuario);
	
	/**
	 * Borrar operaciones de una deuda
	 * @param idDeuda
	 */
	public void borrarOperaciones (long idDeuda);
}
