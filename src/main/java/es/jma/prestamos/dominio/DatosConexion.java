/**
 * 
 */
package es.jma.prestamos.dominio;

/**
 * Clase para guardar los valores de conexión con la 
 * base de datos
 * @author jmiranda
 *
 */
public class DatosConexion {
	//Atributos
	private String url;
	private String password;
	private String user;
	
	//Constructores
	public DatosConexion()
	{
		
	}
	
	public DatosConexion(String url, String password, String user)
	{
		this.url = url;
		this.password = password;
		this.user = user;
	}

	
	//Getters y setters
	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @param url the url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}
	
	
}
