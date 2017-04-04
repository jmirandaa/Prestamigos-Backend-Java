/**
 * 
 */
package es.jma.prestamos.utils;

import org.apache.logging.log4j.Logger;

/**
 * Clase de utilidad para imprimir los logs de errores
 * @author jmiranda
 *
 */
public class UtilLogs{

	/**
	 * Imprimir error
	 * @param logger
	 * @param e
	 */
	public static void error(Logger logger, Exception e)
	{
		if (logger.isErrorEnabled())
		{
			logger.error(e.getMessage(),e);
		}
	}
}
