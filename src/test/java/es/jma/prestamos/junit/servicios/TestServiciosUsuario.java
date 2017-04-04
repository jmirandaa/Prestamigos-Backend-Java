/**
 * 
 */
package es.jma.prestamos.junit.servicios;

import static org.junit.Assert.*;
import static es.jma.prestamos.junit.daos.TestDAOUsuario.*;

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
import es.jma.prestamos.constantes.KMensajes;
import es.jma.prestamos.dominio.Usuario;
import es.jma.prestamos.exceptiones.ExcepcionUsuario;
import es.jma.prestamos.servicios.impl.ServiciosUsuario;
import es.jma.prestamos.utils.UtilCifrado;

/**
 * Test para los servicios Usuario
 * @author jmiranda
 *
 */
@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfig.class)
public class TestServiciosUsuario extends AbstractJUnit4SpringContextTests{

	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	ServiciosUsuario serviciosUsuario;
	
	Transaction txSt;
	
	Usuario usuario;
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		Session stSession = sessionFactory.getCurrentSession();
		txSt = stSession.beginTransaction();
		//No hacer commit
		serviciosUsuario.setTxEnabled(false);
		serviciosUsuario.setSession(stSession);
		serviciosUsuario.setTx(txSt);
		
		//Nuevo usuario
		usuario = new Usuario(USUARIO_NOMBRE1, USUARIO_NOMBRE2, USUARIO_EMAIL1, USUARIO_PASSWORD1);
		serviciosUsuario.nuevoUsuario(usuario);
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		txSt.rollback();
		serviciosUsuario.setTxEnabled(true);
	}

	/**
	 * Probar nuevo usuario
	 */
	@Test
	public void testNuevoUsuario() {
		try
		{
			//Consultar el usuario insertado
			Usuario usuarioInsertado = serviciosUsuario.consultarUsuario(USUARIO_EMAIL1);
			assertEquals(usuarioInsertado.getId(), usuario.getId());
			assertEquals(usuarioInsertado.getNombre(), usuario.getNombre());
			assertEquals(usuarioInsertado.getApellidos(), usuario.getApellidos());
			assertEquals(usuarioInsertado.getEmail(), usuario.getEmail());
			assertEquals(usuarioInsertado.getPassword(), usuario.getPassword());
			assertNotEquals(usuarioInsertado.getFechaRegistro(), null);
			assertEquals(usuarioInsertado.isRegistrado(), true);
		}
		catch (Exception e)
		{
			fail("Error Nuevo Usuario");
		}
		
	}
	
	/**
	 * Probar a insertar un usuario ya existente
	 */
	@Test
	public void testNuevoUsuarioExistente() {
		try
		{
			//Consultar el usuario insertado
			serviciosUsuario.nuevoUsuario(usuario);
			fail("Usuario duplicado");
		}
		catch (ExcepcionUsuario e)
		{
			assertEquals(e.getMessage(), KMensajes.MSG_NUEVO_USUARIO_EXISTE);
		}
		catch (Exception e)
		{
			fail("Error Nuevo Usuario Existente");
		}
		
	}
	
	/**
	 * Probar con un usuario con campos obligatorios nulos
	 */
	@Test
	public void testUsuarioNoValido() {
		try
		{
			Usuario usuario = new Usuario();
			//Consultar el usuario insertado
			serviciosUsuario.nuevoUsuario(usuario);
			fail("Usuario con datos incompletos");
		}
		catch (ExcepcionUsuario e)
		{
			assertEquals(e.getMessage(), KMensajes.MSG_ERROR_NULL);
		}
		catch (Exception e)
		{
			fail("Error Usuario No Válido");
		}
		
	}
	
	/**
	 * Probar actualizar usuario
	 */
	@Test
	public void testActualizarUsuario() {
		try
		{
			//Actualizar campos
			Usuario usuarioExistente = serviciosUsuario.consultarUsuario(usuario.getId());
			usuarioExistente.setNombre(USUARIO_NOMBRE2);
			usuarioExistente.setApellidos(USUARIO_APELLIDO2);
			usuarioExistente.setPassword(USUARIO_PASSWORD2);
			
			//El email no se puede actualizar
			//usuarioExistente.setEmail(USUARIO_EMAIL2);
			
			serviciosUsuario.actualizarUsuario(usuarioExistente);
			
			//Consultar campos
			Usuario usuarioInsertado = serviciosUsuario.consultarUsuario(usuarioExistente.getId()); 
			assertEquals(usuarioInsertado.getId(), usuarioExistente.getId());
			assertEquals(usuarioInsertado.getNombre(), USUARIO_NOMBRE2);
			assertEquals(usuarioInsertado.getApellidos(), USUARIO_APELLIDO2);
			assertEquals(usuarioInsertado.getEmail(), USUARIO_EMAIL1);
			assertEquals(usuarioInsertado.getPassword(), UtilCifrado.cifrarTexto(USUARIO_PASSWORD2));
			assertNotEquals(usuarioInsertado.getFechaRegistro(), null);
		}
		catch (Exception e)
		{
			fail("Error Actualizar Usuario");
		}
		
	}
	
	/**
	 * Probar nuevo invitado
	 */
	@Test
	public void testNuevoInvitado() {
		try
		{
			//Crear invitado
			Usuario usuarioInvitado = new Usuario();
			usuarioInvitado.setNombre(USUARIO_NOMBRE_INV);
			usuarioInvitado.setApellidos(USUARIO_APELLIDO_INV);
			//Insertar
			long idInvitado = serviciosUsuario.nuevoInvitado(usuarioInvitado, USUARIO_EMAIL1);
			
			//Consultar invitado
			usuarioInvitado = serviciosUsuario.consultarUsuario(idInvitado);
			assertEquals(usuarioInvitado.getId(), idInvitado);
			assertEquals(usuarioInvitado.getNombre(), USUARIO_NOMBRE_INV);
			assertEquals(usuarioInvitado.getApellidos(), USUARIO_APELLIDO_INV);
			assertEquals(usuarioInvitado.getEmail(), KInvitados.EMAIL_INVITADO);
			assertEquals(usuarioInvitado.getPassword(), UtilCifrado.cifrarTexto(KInvitados.PASSWORD_INVITADO));
			assertNotEquals(usuarioInvitado.getFechaRegistro(), null);
			assertEquals(usuarioInvitado.isRegistrado(), false);
			
			//Comprobar que es amigo
			Usuario usuarioOrigen = serviciosUsuario.consultarUsuario(USUARIO_EMAIL1);
			assertNotEquals(usuarioOrigen, null);
			List<Usuario> amigos = usuarioOrigen.getAmigos();
			assertNotEquals(amigos, null);
			assertNotEquals(amigos.size(),0);
			Usuario amigo = amigos.get(0);
			assertEquals(amigo.getId(), idInvitado);
			
			//Intentar añadir el mismo
			Usuario usuarioInvitado2 = new Usuario();
			usuarioInvitado2.setNombre(USUARIO_NOMBRE_INV);
			usuarioInvitado2.setApellidos(USUARIO_APELLIDO_INV);
			long idInvitado2 = serviciosUsuario.nuevoInvitado(usuarioInvitado2, USUARIO_EMAIL1);
			assertEquals(idInvitado, idInvitado2);
		}
		catch (Exception e)
		{
			fail("Error Nuevo Invitado");
		}
		
	}
	
	/**
	 * Probar nuevo invitado sin todos
	 * los campos
	 */
	@Test
	public void testNuevoInvitadoSinCampos() {
		try
		{
			//Crear invitado
			Usuario usuarioInvitado = new Usuario();
			usuarioInvitado.setNombre(USUARIO_NOMBRE_INV);
			serviciosUsuario.nuevoInvitado(usuarioInvitado, USUARIO_EMAIL1);
			fail("Usuario con datos incompletos");
		}
		catch (ExcepcionUsuario e)
		{
			assertEquals(e.getMessage(), KMensajes.MSG_ERROR_NULL);
		}
		catch (Exception e)
		{
			fail("Error Nuevo Invitado sin campos");
		}
		
	}
	
	/**
	 * Probar a intentar añadir como invitado
	 * un email no existente
	 */
	@Test
	public void testNuevoInvitadoOrigenInvalido() {
		try
		{
			//Crear invitado
			Usuario usuarioInvitado = new Usuario();
			usuarioInvitado.setNombre(USUARIO_NOMBRE_INV);
			usuarioInvitado.setApellidos(USUARIO_APELLIDO_INV);
			serviciosUsuario.nuevoInvitado(usuarioInvitado, "");
			fail("Usuario origen invalido");
		}
		catch (ExcepcionUsuario e)
		{
			assertEquals(e.getMessage(), KMensajes.MSG_ACTUALIZAR_USUARIO_NOEXISTE);
		}
		catch (Exception e)
		{
			fail("Error Nuevo Invitado sin campos");
		}
		
	}
	
	/**
	 * Probar nuevo amigo
	 */
	@Test
	public void testNuevoAmigo() {
		try
		{
			//Nuevo usuario
			Usuario usuario2 = new Usuario(USUARIO_NOMBRE2, USUARIO_APELLIDO2, USUARIO_EMAIL2, USUARIO_PASSWORD2);
			serviciosUsuario.nuevoUsuario(usuario2);
			
			serviciosUsuario.nuevoAmigo(USUARIO_EMAIL2, USUARIO_EMAIL1);
			
			//Consultar amigo usuario 1
			Usuario amigos1 = serviciosUsuario.consultarAmigo(USUARIO_EMAIL2, usuario.getId());
			assertNotEquals(null,amigos1);
			assertEquals(amigos1.getId(), usuario2.getId());
			assertEquals(amigos1.getEmail(), USUARIO_EMAIL2);
			assertEquals(amigos1.getNombre(), USUARIO_NOMBRE2);
			assertEquals(amigos1.getApellidos(), USUARIO_APELLIDO2);
			
			//Consultar amigos usuario2
			Usuario amigos2 = serviciosUsuario.consultarAmigo(USUARIO_EMAIL1, usuario2.getId());
			assertNotEquals(null,amigos2);
			assertEquals(amigos2.getId(), usuario.getId());
			assertEquals(amigos2.getEmail(), USUARIO_EMAIL1);
			assertEquals(amigos2.getNombre(), USUARIO_NOMBRE1);
			assertEquals(amigos2.getApellidos(), USUARIO_NOMBRE2);
			
			//Intentar volver a añadir como amigo
			serviciosUsuario.nuevoAmigo(USUARIO_EMAIL2, USUARIO_EMAIL1);
			
		}
		catch (Exception e)
		{
			fail("Error Nuevo Amigo");
		}
		
	}	
	
	/**
	 * Probar consultar amigos
	 */
	@Test
	public void testConsultarAmigos() {
		try
		{
			//Crear invitado
			Usuario usuarioInvitado = new Usuario();
			usuarioInvitado.setNombre(USUARIO_NOMBRE_INV);
			usuarioInvitado.setApellidos(USUARIO_APELLIDO_INV);
			//Insertar
			long idInvitado = serviciosUsuario.nuevoInvitado(usuarioInvitado, USUARIO_EMAIL1);
			
			//Consultar amigos
			List<Usuario> amigos = serviciosUsuario.consultarAmigos(USUARIO_EMAIL1);
			assertNotEquals(amigos, null);
			assertNotEquals(amigos.size(),0);
			Usuario amigo = amigos.get(0);
			assertEquals(amigo.getId(), idInvitado);
		}
		catch (Exception e)
		{
			fail("Error Consultar Amigos");
		}
		
	}	

}
