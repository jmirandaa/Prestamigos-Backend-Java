/**
 * 
 */
package es.jma.prestamos.validadores;

import static es.jma.prestamos.utils.UtilValidador.*;

/**
 * Clase abstracta con métodos para validar textos
 * @author jmiranda
 *
 */
public abstract class AbstractValidador {
	protected String msgError;
		
	/**
	 * Validar nombre y apellido
	 * @param texto
	 * @return
	 */
	protected boolean esNombreyApellidos (String texto)
	{
		boolean resultado = false;
		
		if (esTexto(texto))
		{
			String[] partes = texto.split(" ");
			if (partes.length >= 2)
			{
				resultado = true;
			}
		}
		
		return resultado;
	}
	
	
	/**
	 * Validar campos
	 * @return
	 */
	public abstract boolean validar();

	/**
	 * @return the msgError
	 */
	public String getMsgError() {
		return msgError;
	}
		
}
