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
import es.jma.prestamos.daos.impl.OperacionDAO;
import es.jma.prestamos.daos.impl.UsuarioDAO;
import es.jma.prestamos.dominio.Deuda;
import es.jma.prestamos.dominio.Operacion;
import es.jma.prestamos.dominio.Usuario;
import es.jma.prestamos.enums.TipoOperacion;
import es.jma.prestamos.utils.UtilCifrado;

/**
 * JUnit para el DAO Operacion
 * @author jmiranda
 *
 */
@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfig.class)
public class TestDAOOperacion extends AbstractJUnit4SpringContextTests{
	//Constantes
	
	//Operacion1
	public static final double OP_CANTIDAD1 = 12.5;
	public static final TipoOperacion OP_TIPO1 = TipoOperacion.PAGAR;
	
	//Operacion2
	public static final double OP_CANTIDAD2 = 5;
	public static final TipoOperacion OP_TIPO2 = TipoOperacion.AUMENTAR;	
	
	@Autowired
	SessionFactory sessionFactory;
	private DeudaDAO deudaDAO = DeudaDAO.getInstance();
	private UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
	private OperacionDAO operacionDAO = OperacionDAO.getInstance();
	private Transaction txSt;
	
	private Usuario usuario1;
	private Usuario usuario2;
	private Deuda deuda1;	
	private Operacion op1;
	private Operacion op2;
	
	@Before
	public void setUp() throws Exception {
		Session stSession = sessionFactory.getCurrentSession();
		txSt = stSession.beginTransaction();
		deudaDAO.setSession(stSession);		
		usuarioDAO.setSession(stSession);
		operacionDAO.setSession(stSession);
		
		//Insertar usuario de prueba
		usuario1 = new Usuario(TestDAOUsuario.USUARIO_NOMBRE1, TestDAOUsuario.USUARIO_APELLIDO1, TestDAOUsuario.USUARIO_EMAIL1, UtilCifrado.cifrarTexto(TestDAOUsuario.USUARIO_PASSWORD1));
		usuarioDAO.nuevo(usuario1);	
		
		//Insertar usuario invitado
		usuario2 = new Usuario(TestDAOUsuario.USUARIO_NOMBRE_INV, TestDAOUsuario.USUARIO_APELLIDO_INV, TestDAOUsuario.USUARIO_EMAIL_INV, UtilCifrado.cifrarTexto(TestDAOUsuario.USUARIO_PASSWORD_INV));
		usuarioDAO.nuevo(usuario2);
		
		//Insertar nueva deuda
		deuda1 = new Deuda (TestDAODeuda.DEUDA_CONCEPTO1, TestDAODeuda.DEUDA_CANTIDAD1, TestDAODeuda.DEUDA_SALDADO1, false, usuario1, usuario2);
		deudaDAO.nuevo(deuda1);
		
		//Insertar nueva operación
		op1 = new Operacion(OP_CANTIDAD1, deuda1, OP_TIPO1, usuario1);
		operacionDAO.nuevo(op1);
		
		op2 = new Operacion(OP_CANTIDAD2, deuda1, OP_TIPO2, usuario1);
		operacionDAO.nuevo(op2);
		
		stSession.flush();
	}

	@After
	public void tearDown() throws Exception {
		txSt.rollback();
	}

	/**
	 * Probar listado operaciones de pagar
	 */
	@Test
	public void testOperacionesPagar() {
		try
		{
			List<Operacion> operaciones = operacionDAO.consultarOperaciones(deuda1.getId(), OP_TIPO1, usuario1);
			assertNotEquals(operaciones, null);
			assertNotEquals(0, operaciones.size());
			Operacion op = operaciones.get(0);
			
			assertEquals(op.getCantidad(), OP_CANTIDAD1,0);
			assertEquals(op.getDeuda().getId(), deuda1.getId());
			assertEquals(op.getId(), op1.getId());
			assertEquals(op.getTipo(), OP_TIPO1);
			assertEquals(op.getUsuario().getId(), usuario1.getId());
		}
		catch (Exception e)
		{
			fail("Error Operaciones Pagar");
		}
	}
	
	/**
	 * Probar listado operaciones de incrementar
	 */
	@Test
	public void testOperacionesIncrementar() {
		try
		{
			List<Operacion> operaciones = operacionDAO.consultarOperaciones(deuda1.getId(), OP_TIPO2, usuario1);
			assertNotEquals(operaciones, null);
			assertNotEquals(0, operaciones.size());
			Operacion op = operaciones.get(0);
			
			assertEquals(op.getCantidad(), OP_CANTIDAD2,0);
			assertEquals(op.getDeuda().getId(), deuda1.getId());
			assertEquals(op.getId(), op2.getId());
			assertEquals(op.getTipo(), OP_TIPO2);
			assertEquals(op.getUsuario().getId(), usuario1.getId());
		}
		catch (Exception e)
		{
			fail("Error Operaciones Incrementar");
		}
	}	

}
