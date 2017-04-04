/**
 * 
 */
package es.jma.prestamos.daos;


/**
 * Definición de operaciones genéricas para todos los objetos
 * @author jmiranda
 *
 */
public interface ICrudDAO{
	/**
	 * Añadir un objeto
	 * @param T
	 * @throws Exception
	 */
	public void nuevo (Object objeto) throws Exception;
	
	/**
	 * Actualizar un objeto
	 * @param T
	 * @throws Exception
	 */
	public void actualizar (Object objeto) throws Exception;
	
	/**
	 * Borrar un objeto
	 * @param T
	 * @throws Exception
	 */
	public void borrar (Object objeto) throws Exception;
	
}
