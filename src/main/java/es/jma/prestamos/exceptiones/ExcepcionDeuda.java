/**
 * 
 */
package es.jma.prestamos.exceptiones;

/**
 * Excepción relacionada con Deuda
 * @author jmiranda
 *
 */
public class ExcepcionDeuda extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1533047181405106083L;
	private int codigo;
	
	public ExcepcionDeuda(String msg)
	{
		super(msg);
	}
	
	public ExcepcionDeuda(int codigo, String msg)
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
