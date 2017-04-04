/**
 * 
 */
package es.jma.prestamos.exceptiones;

/**
 * Excepción relacionada con Usuario
 * @author jmiranda
 *
 */
public class ExcepcionUsuario extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1533047181405106083L;
	private int codigo;
	
	public ExcepcionUsuario(int codigo, String msg)
	{
		super(msg);
		this.codigo = codigo;
	}
	
	public ExcepcionUsuario(String msg)
	{
		super(msg);
	}	

	/**
	 * @return the codigo
	 */
	public int getCodigo() {
		return codigo;
	}

	
}
