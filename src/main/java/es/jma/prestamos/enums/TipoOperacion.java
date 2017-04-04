/**
 * 
 */
package es.jma.prestamos.enums;


/**
 * Enums para el tipo de operación
 * @author jmiranda
 *
 */
public enum TipoOperacion {
	PAGAR, AUMENTAR;
	
	public static TipoOperacion fromInteger(int x) {
		switch(x) 
		{
	        case 0:
	            return PAGAR;
	        case 1:
	            return AUMENTAR;
	        }
	        return null;
		}	
}
