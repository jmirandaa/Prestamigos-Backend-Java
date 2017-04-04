package es.jma.email;

/**
 * Clase para enviar correos para cambiar la contraseña
 * @author jmiranda
 *
 */

public class EmailRecordar extends AbstractEmail{
	//Constantes
	private static final String SUBJECT = "Prestamigos - Recordar contraseña";
	
	//Atributos
	private String email;
	private String token;
	
	//Constructores
	public EmailRecordar(String email, String token)
	{
		this.email = email;
		this.token = token;
	}

	//Getters y setters
	public String getEmail() {
		return email;
	}
	
	public String getToken(){
		return token;
	}

	//Metodos
	@Override
	public String getToMail() {
		return this.email;
	}

	@Override
	public String getSubjectMail() {
		return SUBJECT;
	}

	@Override
	public String getBodyMail() {
		StringBuilder body = new StringBuilder();
		body.append("<p>Cambia tu contraseña usando el siguiente código de seguridad:</p>");
		body.append("<p>");
		body.append(this.token);
		body.append("</p>");
			
		return body.toString();
	}
	
}
