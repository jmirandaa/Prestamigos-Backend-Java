/**
 * 
 */
package es.jma.prestamos.utils;

import org.apache.commons.validator.routines.EmailValidator;

/**
 * Clase de utilidad para validaciones
 * @author jmiranda
 *
 */
public class UtilValidador {
	public static final int TAM_MIN = 3;
	
	/**
	 * Validar email
	 * @param email
	 * @return
	 */
	public static boolean esEmail (String email)
	{
		EmailValidator validator = EmailValidator.getInstance();
		return validator.isValid(email);
	}
	
	/**
	 * Validar texto
	 * @param texto
	 * @return
	 */
	public static boolean esTexto (String texto)
	{
		boolean resultado = false;
		
		if ((texto != null) && (!texto.isEmpty()) && (texto.length() >= TAM_MIN))
		{
			resultado = true;
		}
		
		return resultado;
	}
	
	/**
	 * Validar nombre y apellido
	 * @param texto
	 * @return
	 */
	public static boolean esNombreyApellidos (String texto)
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
	 * Validar valor positivos
	 * @param valor
	 * @return
	 */
	public static boolean esPositivo (double valor)
	{
		boolean resultado = false;
		
		if (valor > 0)
		{
			resultado = true;
		}
		
		return resultado;
	}	
}
