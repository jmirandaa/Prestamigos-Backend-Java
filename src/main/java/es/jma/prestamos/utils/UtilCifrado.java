/**
 * 
 */
package es.jma.prestamos.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;


/**
 * Clase que contiene métodos útiles para manejar contraseñas
 * @author jmiranda
 *
 */
public class UtilCifrado {
	//Constantes
	private static final String ALGORITMO = "SHA-256";
	
	/**
	 * Convertir texto a SHA256
	 * @param texto
	 * @return "null" en caso de error, el texto en otro caso
	 */
	public static String cifrarTexto (String texto)
	{
		String resultado = null;
		
		if ((texto != null) && (!texto.isEmpty()))
		{
			MessageDigest md;
			try {
				//Convertir
				md = MessageDigest.getInstance(ALGORITMO);
				md.update(texto.getBytes()); 
				
				//Transformar a string legible
				byte byteData[] = md.digest();
				StringBuffer sb = new StringBuffer();
		        for (int i = 0; i < byteData.length; i++) {
		         sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
		        }								
				
				resultado = sb.toString();
				
			} catch (NoSuchAlgorithmException e) {
				resultado = null;
			} 
		}
		
		return resultado;
		
	}
	
}
