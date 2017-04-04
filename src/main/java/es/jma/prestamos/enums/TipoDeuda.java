/**
 * 
 */
package es.jma.prestamos.enums;

/**
 * Enums para el tipo de deuda
 * @author jmiranda
 *
 */
public enum TipoDeuda {
	DEBEN, DEBO, TODAS;
	
	public static TipoDeuda getContrario(TipoDeuda tipoDeuda)
	{
		if (tipoDeuda == TipoDeuda.DEBO)
		{
			return TipoDeuda.DEBEN;
		}
		else if (tipoDeuda == TipoDeuda.DEBEN)
		{
			return TipoDeuda.DEBO;
		}
		else
		{
			return TipoDeuda.DEBEN;
		}
	}
	
	public static TipoDeuda fromInteger(int x) {
		switch(x) 
		{
	        case 0:
	            return TODAS;
	        case 1:
	            return DEBEN;
	        case 2:
	        	return DEBO;
	    }
	    return null;
	}
	
	public static int fromEnum(TipoDeuda x) {
		int valor = -1;
		if (x.equals(TipoDeuda.TODAS))
		{
			valor = 0;
		}
		else if (x.equals(TipoDeuda.DEBEN))
		{
			valor = 1;
		}
		else if (x.equals(TipoDeuda.DEBO))
		{
			valor = 2;
		}
	    return valor;
	}	
}
