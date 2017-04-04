/**
 * 
 */
package es.jma.prestamos.junit.daos;

import static org.junit.Assert.*;

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
import es.jma.prestamos.daos.impl.DeudaDAO;
import es.jma.prestamos.daos.impl.UsuarioDAO;
import es.jma.prestamos.dominio.Deuda;
import es.jma.prestamos.dominio.ResumenDeuda;
import es.jma.prestamos.dominio.Usuario;
import es.jma.prestamos.enums.TipoDeuda;
import es.jma.prestamos.utils.UtilCifrado;

/**
 * JUnit para el DAO Deuda
 * @author jmiranda
 *
 */
@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfig.class)
public class TestDAODeuda extends AbstractJUnit4SpringContextTests{
	//Constantes
	
	//Deuda1
	public static final String DEUDA_CONCEPTO1 = "Billete avión";
	public static final double DEUDA_CANTIDAD1 = 60;
	public static final double DEUDA_SALDADO1 = 15;
	
	//Deuda2
	public static final String DEUDA_CONCEPTO2 = "Gasolina";
	public static final double DEUDA_CANTIDAD2 = 40;
	public static final double DEUDA_SALDADO2 = 35;	
	
	//Deuda3 saldada
	public static final String DEUDA_CONCEPTO3 = "Cena";
	public static final double DEUDA_CANTIDAD3 = 15;
	public static final double DEUDA_SALDADO3 = 15;		
	
	//Deuda4
	public static final String DEUDA_CONCEPTO4 = "Billete tren";
	public static final double DEUDA_CANTIDAD4 = 10;
	public static final double DEUDA_SALDADO4 = 0;		
	
	@Autowired
	SessionFactory sessionFactory;
	private DeudaDAO deudaDAO = DeudaDAO.getInstance();
	private UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
	private Transaction txSt;
	
	private Usuario usuario1;
	private Usuario usuario2;
	private Deuda deuda1;
	private Deuda deuda2;
	private Deuda deuda3;
	
	/**
	 * Empezar transacción
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		Session stSession = sessionFactory.getCurrentSession();
		txSt = stSession.beginTransaction();
		deudaDAO.setSession(stSession);		
		usuarioDAO.setSession(stSession);
		
		//Insertar usuario de prueba
		usuario1 = new Usuario(TestDAOUsuario.USUARIO_NOMBRE1, TestDAOUsuario.USUARIO_APELLIDO1, TestDAOUsuario.USUARIO_EMAIL1, UtilCifrado.cifrarTexto(TestDAOUsuario.USUARIO_PASSWORD1));
		usuarioDAO.nuevo(usuario1);	
		
		//Insertar usuario invitado
		usuario2 = new Usuario(TestDAOUsuario.USUARIO_NOMBRE_INV, TestDAOUsuario.USUARIO_APELLIDO_INV, TestDAOUsuario.USUARIO_EMAIL_INV, UtilCifrado.cifrarTexto(TestDAOUsuario.USUARIO_PASSWORD_INV));
		usuarioDAO.nuevo(usuario2);
		stSession.flush();
		
		//Insertar nueva deuda
		deuda1 = new Deuda (DEUDA_CONCEPTO1, DEUDA_CANTIDAD1, DEUDA_SALDADO1, false, usuario1, usuario2);
		deudaDAO.nuevo(deuda1);
		
		deuda2 = new Deuda (DEUDA_CONCEPTO2, DEUDA_CANTIDAD2, DEUDA_SALDADO2, false, usuario2, usuario1);
		deudaDAO.nuevo(deuda2);		
		
		deuda3 = new Deuda (DEUDA_CONCEPTO3, DEUDA_CANTIDAD3, DEUDA_SALDADO3, true, usuario1, usuario2);
		deudaDAO.nuevo(deuda3);
		stSession.flush();
		
	}

	/**
	 * Rollback de transacción
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		txSt.rollback();
	}

	/**
	 * Probar consulta deuda por id
	 */
	@Test
	public void testConsultarDeudaId() {
		try
		{
			Deuda deuda = deudaDAO.consultarDeuda(deuda1.getId());
			assertNotEquals(null, deuda);
			assertEquals(deuda.getCantidad(), DEUDA_CANTIDAD1,0);
			assertEquals(deuda.getConcepto(), DEUDA_CONCEPTO1);
			assertEquals(deuda.getSaldado(), DEUDA_SALDADO1, 0);
			assertEquals(deuda.isSaldada(), false);
			assertEquals(deuda.getUsuario(), usuario1);
			assertEquals(deuda.getUsuarioDestino(), usuario2);
		}
		catch(Exception e)
		{
			fail("Error consultar deuda id");
		}
	}
	
	/**
	 * Probar consulta deudas que deben al usuario 1
	 */
	@Test
	public void testConsultarDeben() {
		try
		{
			List<Deuda> deudas = deudaDAO.consultarDeudas (usuario1.getId(), TipoDeuda.DEBEN, false);
			assertNotEquals(null, deudas);
			assertEquals(1, deudas.size());
			
			Deuda deuda = deudas.get(0);
			assertEquals(deuda.getId(), deuda2.getId());
		}
		catch(Exception e)
		{
			fail("Error consultar deuda deben");
		}
	}
	
	/**
	 * Probar consulta deudas que debe el usuario 1
	 */
	@Test
	public void testConsultarDebo() {
		try
		{
			List<Deuda> deudas = deudaDAO.consultarDeudas (usuario1.getId(), TipoDeuda.DEBO, false);
			assertNotEquals(null, deudas);
			assertEquals(1, deudas.size());
			
			Deuda deuda = deudas.get(0);
			assertEquals(deuda.getId(), deuda1.getId());
		}
		catch(Exception e)
		{
			fail("Error consultar deuda debo");
		}
	}
	
	/**
	 * Probar consulta todas deudas usuario1
	 */
	@Test
	public void testConsultarTodas() {
		try
		{
			List<Deuda> deudas = deudaDAO.consultarDeudas (usuario1.getId(), TipoDeuda.TODAS, false);
			assertNotEquals(null, deudas);
			assertEquals(2, deudas.size());			
		}
		catch(Exception e)
		{
			fail("Error consultar todas");
		}
	}
	
	/**
	 * Probar consulta resumen
	 */
	@Test
	public void testResumen() {
		try
		{
			ResumenDeuda resumenDeuda = deudaDAO.consultarResumen(usuario1.getId());
			assertNotEquals(null, resumenDeuda);
			assertEquals(resumenDeuda.getTotal(), 40.0, 0);
			assertEquals(resumenDeuda.getTodalDebo(), 45.0,0);
			assertEquals(resumenDeuda.getTotalMeDeben(), 5.0,0);
			assertEquals(resumenDeuda.getNumDeudasDebo(),1);
			assertEquals(resumenDeuda.getNumDeudasMeDeben(),1);
		}
		catch(Exception e)
		{
			fail("Error consultar todas");
		}
	}	

}
