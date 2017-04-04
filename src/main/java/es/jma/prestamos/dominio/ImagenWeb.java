/**
 * 
 */
package es.jma.prestamos.dominio;

import java.io.Serializable;

/**
 * @author jmiranda
 *
 */
public class ImagenWeb implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 845164404688764742L;
	private String base64;
	private String usuario;
	
	public ImagenWeb()
	{
		
	}

	/**
	 * @return the base64
	 */
	public String getBase64() {
		return base64;
	}

	/**
	 * @param base64 the base64 to set
	 */
	public void setBase64(String base64) {
		this.base64 = base64;
	}

	/**
	 * @return the user
	 */
	public String getUsuario() {
		return usuario;
	}

	/**
	 * @param user the user to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
		
}
