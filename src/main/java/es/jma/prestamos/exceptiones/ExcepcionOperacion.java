/**
 * 
 */
package es.jma.prestamos.exceptiones;

/**
 * Excepción relacionada con Operacion
 * @author jmiranda
 *
 */
public class ExcepcionOperacion extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1533047181405106083L;
	private int codigo;
	
	public ExcepcionOperacion(String msg)
	{
		super(msg);
	}
	
	public ExcepcionOperacion(int codigo, String msg)
	{
		super(msg);
		this.codigo = codigo;
	}

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	
}
