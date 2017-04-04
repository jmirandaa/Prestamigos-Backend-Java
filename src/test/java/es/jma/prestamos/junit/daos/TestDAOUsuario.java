/**
 * 
 */
package es.jma.prestamos.junit.daos;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;

import es.jma.prestamos.config.ApplicationContextConfig;
import es.jma.prestamos.constantes.KInvitados;
import es.jma.prestamos.daos.impl.UsuarioDAO;
import es.jma.prestamos.dominio.Usuario;
import es.jma.prestamos.utils.UtilCifrado;


/**
 * JUnit para el DAO Usuario
 * @author jmiranda
 *
 */
@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfig.class)
public class TestDAOUsuario extends AbstractJUnit4SpringContextTests{
	//Constantes
	
	//Usuario 1
	public static final String USUARIO_NOMBRE1 = "Daniel";
	public static final String USUARIO_APELLIDO1 = "Jackson";
	public static final String USUARIO_EMAIL1 = "danieljackson@sgc.mil";
	public static final String USUARIO_PASSWORD1 = "odiaréAApofis";
	//Usuario 2
	public static final String USUARIO_NOMBRE2 = "Kara";
	public static final String USUARIO_APELLIDO2 = "Trace";
	public static final String USUARIO_EMAIL2 = "karatrace@bsg.mil";
	public static final String USUARIO_PASSWORD2 = "viperMKIII";
	//Usuario invitado
	public static final String USUARIO_NOMBRE_INV = "Karl";
	public static final String USUARIO_APELLIDO_INV = "C. Agathon";
	public static final String USUARIO_EMAIL_INV = KInvitados.EMAIL_INVITADO;
	public static final String USUARIO_PASSWORD_INV = KInvitados.PASSWORD_INVITADO;
	
	@Autowired
	SessionFactory sessionFactory;
	private UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
	private Transaction txSt;
	private long idUsuario1 = -1;
	
	/**
	 * Empezar transacción
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		Session stSession = sessionFactory.getCurrentSession();
		txSt = stSession.beginTransaction();
		usuarioDAO.setSession(stSession);
		
		//Insertar usuario de prueba
		Usuario usuario1 = new Usuario(USUARIO_NOMBRE1, USUARIO_APELLIDO1, USUARIO_EMAIL1, UtilCifrado.cifrarTexto(USUARIO_PASSWORD1));
		usuarioDAO.nuevo(usuario1);
		
		//Insertar usuario invitado
		Usuario usuario2 = new Usuario(USUARIO_NOMBRE_INV, USUARIO_APELLIDO_INV, USUARIO_EMAIL_INV, UtilCifrado.cifrarTexto(USUARIO_PASSWORD_INV));
		usuarioDAO.nuevo(usuario2);
		stSession.flush();
		
		//Añadirlo como amigo
		List<Usuario> amigos = new ArrayList<Usuario> ();
		amigos.add(usuario2);	
		usuario1.setAmigos(amigos);
		usuarioDAO.actualizar(usuario1);
		stSession.flush();
		
		idUsuario1 = usuario1.getId();
	}

	/**
	 * Rollback de la transacción
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		txSt.rollback();		
	}

	/**
	 * Probar login con datos correctos
	 */
	@Test
	public void testLoginCorrecto() {
		try
		{
			boolean resultado = usuarioDAO.loguearse(USUARIO_EMAIL1, USUARIO_PASSWORD1);
			assertEquals(true, resultado);
		}
		catch (Exception e)
		{
			fail("Error Login Correcto");
		}
	}
	
	/**
	 * Probar login con datos incorrectos
	 */
	@Test
	public void testLoginIncorrecto() {
		try
		{
			boolean resultado = usuarioDAO.loguearse(USUARIO_EMAIL2, USUARIO_PASSWORD2);
			assertEquals(false, resultado);
		}
		catch (Exception e)
		{
			fail("Error Login Incorrecto");
		}
	}
	
	/**
	 * Probar login con usuario invitado
	 */
	@Test
	public void testLoginInvitado() {
		try
		{
			boolean resultado = usuarioDAO.loguearse(USUARIO_EMAIL_INV, USUARIO_PASSWORD_INV);
			assertEquals(false, resultado);
		}
		catch (Exception e)
		{
			fail("Error Login Invitado");
		}
	}	

	/**
	 * Probar consulta de datos de usuario
	 * por email y contraseña
	 */
	@Test
	public void testLoginDatosEmailPassword() {
		try
		{
			Usuario usuario = usuarioDAO.loginUsuario(USUARIO_EMAIL1, USUARIO_PASSWORD1);
			assertNotEquals(null, usuario);
			assertEquals(usuario.getNombre(), USUARIO_NOMBRE1);
			assertEquals(usuario.getApellidos(),USUARIO_APELLIDO1);
			assertEquals(usuario.getEmail(), USUARIO_EMAIL1);
			assertEquals(usuario.getPassword(), UtilCifrado.cifrarTexto(USUARIO_PASSWORD1));
		}
		catch (Exception e)
		{
			fail("Error Login Datos Email Password");
		}
	}
	
	/**
	 * Probar consulta de datos de usuario
	 * por email
	 */
	@Test
	public void testLoginDatosEmail() {
		try
		{
			Usuario usuario = usuarioDAO.consultarUsuario(USUARIO_EMAIL1);
			assertNotEquals(null, usuario);
			assertEquals(usuario.getNombre(), USUARIO_NOMBRE1);
			assertEquals(usuario.getApellidos(),USUARIO_APELLIDO1);
			assertEquals(usuario.getEmail(), USUARIO_EMAIL1);
			assertEquals(usuario.getPassword(), UtilCifrado.cifrarTexto(USUARIO_PASSWORD1));
		}
		catch (Exception e)
		{
			fail("Error Login Datos Email");
		}
	}	
	
	/**
	 * Probar consulta de datos de usuario
	 * por id
	 */
	@Test
	public void testLoginDatosId() {
		try
		{
			Usuario usuario = usuarioDAO.consultarUsuario(idUsuario1);
			assertNotEquals(null, usuario);
			assertEquals(usuario.getNombre(), USUARIO_NOMBRE1);
			assertEquals(usuario.getApellidos(),USUARIO_APELLIDO1);
			assertEquals(usuario.getEmail(), USUARIO_EMAIL1);
			assertEquals(usuario.getPassword(), UtilCifrado.cifrarTexto(USUARIO_PASSWORD1));
		}
		catch (Exception e)
		{
			fail("Error Login Datos Id");
		}
	}
	
	/**
	 * Probar consulta listado de amigos
	 */
	@Test
	public void testConsultarAmigos() {
		try
		{
			List<Usuario> amigos = usuarioDAO.consultarAmigos(USUARIO_EMAIL1);
			assertNotEquals(null, amigos);
			assertNotEquals(0,amigos.size());
			
			//Ver que el amigo se corresponde con el insertado
			Usuario amigo = amigos.get(0);
			assertEquals(amigo.getNombre(), USUARIO_NOMBRE_INV);
			assertEquals(amigo.getApellidos(),USUARIO_APELLIDO_INV);
			assertEquals(amigo.getEmail(), USUARIO_EMAIL_INV);
			assertEquals(amigo.getPassword(), UtilCifrado.cifrarTexto(USUARIO_PASSWORD_INV));
		}
		catch (Exception e)
		{
			fail("Error Consultar Amigos");
		}
	}
	
	/**
	 * Probar buscar amigo por nombre
	 */
	@Test
	public void testConsultarAmigo()
	{
		try
		{
			Usuario amigo = usuarioDAO.consultarAmigo(USUARIO_NOMBRE_INV, USUARIO_APELLIDO_INV, idUsuario1);
			assertNotEquals(amigo, null);
			assertEquals(amigo.getNombre(), USUARIO_NOMBRE_INV);
			assertEquals(amigo.getApellidos(), USUARIO_APELLIDO_INV);
		}
		catch (Exception e)
		{
			fail("Error Consultar Amigo");
		}
	}
}
