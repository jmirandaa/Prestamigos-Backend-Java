/**
 * 
 */
package es.jma.prestamos.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.jma.prestamos.dominio.DatosConexion;

/**
 * Clase auxiliar para cargar los datos de conexión de la base de datos
 * leyendo de un fichero de propiedades
 * @author jmiranda
 *
 */
public class UtilFicheros {
	private static final Logger logger = LogManager.getLogger(UtilFicheros.class);
	
	//Fichero de configuración de conexión
	private static final String FICHERO_CONEXIÓN = "conexion.properties";
	
	/**
	 * Leer propiedades de la base de datos
	 * @return
	 */
	public static final DatosConexion leerBBDD()
	{
		DatosConexion datosConexion =null;
		
		try
		{
			//Abrir fichero
			ClassLoader classLoader = UtilFicheros.class.getClassLoader();
			File file = new File(classLoader.getResource(FICHERO_CONEXIÓN).getFile());
			
			//Leer propiedad
			FileInputStream fstream = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
	
			String strLine;
			datosConexion = new DatosConexion();
			while ((strLine = br.readLine()) != null)   {
			  //Separar el igual
				String[] token = strLine.split("=");
				if ((token != null) && (token.length >= 2))
				{
					String clave = token[0];
					
					if ("bbdd".equals(clave))
					{
						datosConexion.setUrl(token[1].replaceAll("-", ":"));
					}
					else if ("password".equals(clave))
					{
						datosConexion.setPassword(token[1]);
					}
					else if ("usuario".equals(clave))
					{
						datosConexion.setUser(token[1]);
					}
				}
			}
	
			//Close the input stream
			br.close();	
		}
		catch (Exception e)
		{
			RuntimeException ne = new RuntimeException("Datos de conexión con la bbdd no configurados. Debe existir un "+
					"fichero de configuración llamado \""+FICHERO_CONEXIÓN+"\" en \"src/main/resources\" con las "+
					"entradas \"bbdd\", \"password\" y \"usuario\"");
			UtilLogs.error(logger, ne);
		}
		
		return datosConexion;		
	}
	
	/**
	 * Leer datos del servidor de correos
	 * @return
	 */
	public static final DatosConexion leerCorreo()
	{
		DatosConexion datosConexion =null;
		
		try
		{
			//Abrir fichero
			ClassLoader classLoader = UtilFicheros.class.getClassLoader();
			File file = new File(classLoader.getResource(FICHERO_CONEXIÓN).getFile());
			
			//Leer propiedad
			FileInputStream fstream = new FileInputStream(file);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
	
			String strLine;
			datosConexion = new DatosConexion();
			while ((strLine = br.readLine()) != null)   {
			  //Separar el igual
				String[] token = strLine.split("=");
				if ((token != null) && (token.length >= 2))
				{
					String clave = token[0];
					
					if ("correo_servidor".equals(clave))
					{
						datosConexion.setUrl(token[1]);
					}
					else if ("correo_password".equals(clave))
					{
						datosConexion.setPassword(token[1]);
					}					
					else if ("correo_usuario".equals(clave))
					{						
						datosConexion.setUser(token[1]);
					}
				}
			}
	
			//Close the input stream
			br.close();	
		}
		catch (Exception e)
		{
			RuntimeException ne = new RuntimeException("Datos de conexión con el servidor de correos no configurados. Debe existir un "+
					"fichero de configuración llamado \""+FICHERO_CONEXIÓN+"\" en \"src/main/resources\" con las "+
					"entradas \"correo_servidor\", \"correo_password\" y \"correo_usuario\"");
			UtilLogs.error(logger, ne);
		}
		
		return datosConexion;		
	}	
}
