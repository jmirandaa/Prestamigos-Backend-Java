/**
 * 
 */
package es.jma.prestamos.junit.servicios;

import static es.jma.prestamos.junit.daos.TestDAOUsuario.*;
import static es.jma.prestamos.junit.daos.TestDAODeuda.*;
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
import es.jma.prestamos.constantes.KMensajes;
import es.jma.prestamos.dominio.Deuda;
import es.jma.prestamos.dominio.Usuario;
import es.jma.prestamos.enums.TipoDeuda;
import es.jma.prestamos.exceptiones.ExcepcionDeuda;
import es.jma.prestamos.servicios.impl.ServiciosDeuda;
import es.jma.prestamos.servicios.impl.ServiciosUsuario;

/**
 * @author jmiranda
 *
 */
@WebAppConfiguration
@ContextConfiguration(classes = ApplicationContextConfig.class)
public class TestServiciosDeuda extends AbstractJUnit4SpringContextTests{
	
	@Autowired
	SessionFactory sessionFactory;
	@Autowired
	ServiciosUsuario serviciosUsuario;
	@Autowired
	ServiciosDeuda serviciosDeuda;
	
	Transaction txSt;
	Usuario usuario1;
	Usuario usuario2;
	
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
		serviciosDeuda.setTxEnabled(false);
		serviciosDeuda.setSession(stSession);
		serviciosDeuda.setTx(txSt);	
		
		//Nuevo usuario
		usuario1 = new Usuario(USUARIO_NOMBRE1, USUARIO_APELLIDO1, USUARIO_EMAIL1, USUARIO_PASSWORD1);
		serviciosUsuario.nuevoUsuario(usuario1);	
		usuario2 = new Usuario(USUARIO_NOMBRE2, USUARIO_APELLIDO2, USUARIO_EMAIL2, USUARIO_PASSWORD2);
		serviciosUsuario.nuevoUsuario(usuario2);		
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		txSt.rollback();
		serviciosUsuario.setTxEnabled(true);
		serviciosDeuda.setTxEnabled(true);
	}

	/**
	 * Probar nueva deuda
	 */
	@Test
	public void testNuevaDeuda() {
		try
		{
			//Insertar deuda
			long idNuevaDeuda = serviciosDeuda.nuevaDeuda(usuario1.getId(), usuario2.getId(), DEUDA_CANTIDAD1, DEUDA_CONCEPTO1);
			//Consultarla
			Deuda deuda = serviciosDeuda.consultarDeuda(idNuevaDeuda);
			assertNotEquals(deuda, null);
			assertEquals(deuda.getCantidad(), DEUDA_CANTIDAD1,0);
			assertEquals(deuda.getConcepto(), DEUDA_CONCEPTO1);
			assertNotEquals(deuda.getFechaRegistro(), null);
			assertEquals(deuda.getId(), idNuevaDeuda);
			assertNotEquals(deuda.getIdDeuda(), null);
			assertEquals(deuda.getSaldado(),0,0);
			assertEquals(deuda.isSaldada(),false);
			assertEquals(deuda.getUsuario().getId(), usuario1.getId());
			assertEquals(deuda.getUsuarioDestino().getId(), usuario2.getId());
		}
		catch (Exception e)
		{
			fail("Error Nueva Deuda");
		}
	}
	
	/**
	 * Probar deuda con origen no existente
	 */
	@Test
	public void testNuevaDeudaOrigenDesconocido() {
		try
		{
			//Insertar deuda
			serviciosDeuda.nuevaDeuda(0, usuario2.getId(), DEUDA_CANTIDAD1, DEUDA_CONCEPTO1);
			fail("Usuario origen");
		}
		catch (ExcepcionDeuda e)
		{
			assertEquals(e.getMessage(), KMensajes.MSG_DEUDA_USUARIO1_NOEXISTE);
		}
		catch (Exception e)
		{
			fail("Error Nueva Deuda");
		}
	}

	/**
	 * Probar deuda con destino no existente
	 */	
	@Test
	public void testNuevaDeudaDestinoDesconocido() {
		try
		{
			//Insertar deuda
			serviciosDeuda.nuevaDeuda(usuario1.getId(), 0, DEUDA_CANTIDAD1, DEUDA_CONCEPTO1);
			fail("Usuario destino");
		}
		catch (ExcepcionDeuda e)
		{
			assertEquals(e.getMessage(), KMensajes.MSG_DEUDA_USUARIO2_NOEXISTE);
		}
		catch (Exception e)
		{
			fail("Error Nueva Deuda");
		}
	}
	
	@Test
	public void testConsultarDeudas() {
		try
		{
			//Insertar deudas
			long idNuevaDeuda1 = serviciosDeuda.nuevaDeuda(usuario1.getId(), usuario2.getId(), DEUDA_CANTIDAD1, DEUDA_CONCEPTO1);
			long idNuevaDeuda2 = serviciosDeuda.nuevaDeuda(usuario1.getId(), usuario2.getId(), DEUDA_CANTIDAD2, DEUDA_CONCEPTO2);
			long idNuevaDeuda3 = serviciosDeuda.nuevaDeuda(usuario2.getId(), usuario1.getId(), DEUDA_CANTIDAD3, DEUDA_CONCEPTO3);
			
			//Consultar deudas tipo DEBO
			List<Deuda> deudas = serviciosDeuda.consultarDeudas(usuario1.getId(), TipoDeuda.DEBO, false);
			assertNotEquals(deudas,null);
			assertEquals(deudas.size(),2);
			
			Deuda deuda1 = deudas.get(0);
			Deuda deuda2 = deudas.get(1);
			
			//Las dos primeras cumplen con ese tipo
			assertEquals(deuda1.getId(), idNuevaDeuda2);
			assertEquals(deuda2.getId(), idNuevaDeuda1);
			
			//Consultar deudas tipo DEBEN
			deudas = serviciosDeuda.consultarDeudas(usuario1.getId(), TipoDeuda.DEBEN, false);
			assertNotEquals(deudas,null);
			assertEquals(deudas.size(),1);
			
			deuda1 = deudas.get(0);
			//El tercero cumple con ese tipo
			assertEquals(deuda1.getId(), idNuevaDeuda3);
			
			//Consultar todos
			deudas = serviciosDeuda.consultarDeudas(usuario1.getId(), TipoDeuda.TODAS, false);
			assertNotEquals(deudas, null);
			assertEquals(deudas.size(),3);
			
			deuda1 = deudas.get(0);
			deuda2 = deudas.get(1);
			
			//Todas cumplen
			Deuda deuda3 = deudas.get(2);
			assertEquals(deuda1.getId(), idNuevaDeuda2);
			assertEquals(deuda2.getId(), idNuevaDeuda3);
			assertEquals(deuda3.getId(), idNuevaDeuda1);
		}
		catch (Exception e)
		{
			fail("Error Nueva Deuda");
		}
	}	

}
