package es.jma.email;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import es.jma.prestamos.dominio.DatosConexion;
import es.jma.prestamos.utils.UtilFicheros;

/**
 * Clase abstracta que contiene la configuración para el envió de emails
 * @author jmiranda
 *
 */
public abstract class AbstractEmail {	
	//Atributos
	private String to;
	private String subject;
	private String body;
	
	//Métodos
	public void sendMail() throws Exception
	{
		DatosConexion datosConexion = UtilFicheros.leerCorreo();
		
		//Get to, from and subject
		to = getToMail();
		subject = getSubjectMail();
		body = getBodyMail();
		
		if ((to != null) && (subject != null) && (body != null))
		{
		      // Get system properties
		      Properties properties = System.getProperties();

		      // Setup mail server
		      properties.put("mail.transport.protocol", "smtps");
		      properties.put("mail.smtps.host", datosConexion.getUrl());
		      properties.put("mail.smtps.auth", "true");
		      properties.put("mail.smtps.port", "465");

		      // Get the default Session object.
		      Authenticator auth = new javax.mail.Authenticator() {
	    			protected PasswordAuthentication getPasswordAuthentication() {
	    				return new PasswordAuthentication(datosConexion.getUser(), datosConexion.getPassword());
	    			}
	    		  };
		      Session session = Session.getInstance(properties,auth);
		      session.setDebug(true);
		      
		      try {
			        Transport transport = session.getTransport();	 
			      			        
			        // Create a default MimeMessage object.
			         MimeMessage message = new MimeMessage(session);

			         // Set From: header field of the header.
			         message.setFrom(new InternetAddress(datosConexion.getUser()));

			         // Set To: header field of the header.
			         message.addRecipient(Message.RecipientType.TO,
			                                  new InternetAddress(to));

			         // Set Subject: header field
			         message.setSubject(subject);

			         // Send the actual HTML message, as big as you like
			         message.setContent(body,
			                            "text/html" );
					
					transport.connect();
					transport.sendMessage(message,
				            message.getRecipients(Message.RecipientType.TO));
					transport.close();


				} catch (Exception e) {
					throw e;
				}
		}
	}
	
	/**
	 * Mail TO
	 * @return
	 */
	public abstract String getToMail();
	
	/**
	 * Mail SUBJECT
	 * @return
	 */
	public abstract String getSubjectMail();
	
	/**
	 * Mail BODY
	 * @return
	 */
	public abstract String getBodyMail();
}
