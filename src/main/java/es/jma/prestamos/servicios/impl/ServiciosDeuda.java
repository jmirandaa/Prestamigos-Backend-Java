/**
 * 
 */
package es.jma.prestamos.servicios.impl;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import es.jma.prestamos.constantes.KMensajes;
import es.jma.prestamos.daos.impl.DeudaDAO;
import es.jma.prestamos.daos.impl.OperacionDAO;
import es.jma.prestamos.daos.impl.UsuarioDAO;
import es.jma.prestamos.dominio.Deuda;
import es.jma.prestamos.dominio.ResumenDeuda;
import es.jma.prestamos.dominio.Usuario;
import es.jma.prestamos.enums.TipoDeuda;
import es.jma.prestamos.exceptiones.ExcepcionDeuda;
import es.jma.prestamos.servicios.IServiciosDeuda;

/**
 * Implementación del servicio Deuda
 * @author jmiranda
 *
 */
public class ServiciosDeuda extends ServiciosHibernate implements IServiciosDeuda {
	private static ServiciosDeuda instance;
	private DeudaDAO deudaDAO;
	
	private ServiciosDeuda(SessionFactory sessionFactory)
	{
		super(sessionFactory);
		deudaDAO = DeudaDAO.getInstance();
	}
	
	public static ServiciosDeuda getInstance(SessionFactory sessionFactory)
	{
		if (instance == null)
		{
			instance = new ServiciosDeuda(sessionFactory);
		}
		return instance;
	}

	@Override
	public List<Deuda> consultarDeudas(String email, TipoDeuda tipo, boolean saldada) {
		List<Deuda> resultado = null;
		Transaction tx = null;
		
		try
		{
			session = initSession();
			tx = initTx(session);
			
			deudaDAO.setSession(session);
			resultado = deudaDAO.consultarDeudas(email, tipo, saldada);
			
			commit(tx);
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}
					
		return resultado;
	}

	@Override
	public long nuevaDeuda(long idUsuarioOrigen, long idDestino, double cantidad,
			String concepto) throws Exception {
		long resultado = -1;
		Transaction tx = null;
		
		try
		{	
			session = initSession();
			tx = initTx(session);
			UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
			
			deudaDAO.setSession(session);
			usuarioDAO.setSession(session);
			
			LocalDateTime fecha = LocalDateTime.now();
			//Generar id deuda
			String identificador = String.valueOf(Calendar.getInstance().getTimeInMillis())+"-"+idUsuarioOrigen;
			
			//Consultar usuario origen
			Usuario usuarioOrigen = usuarioDAO.consultarUsuario(idUsuarioOrigen);
			session.flush();
			session.clear();
			
			//Consultar usuario destino
			Usuario usuarioDestino = usuarioDAO.consultarUsuario(idDestino);
			session.flush();
			session.clear();
			
			//Si alguno de los usuarios no es válido, excepción
			if (usuarioOrigen == null)
			{
				throw new ExcepcionDeuda(KMensajes.MSG_DEUDA_USUARIO1_NOEXISTE);
			}
			else if (usuarioDestino == null)
			{
				throw new ExcepcionDeuda(KMensajes.MSG_DEUDA_USUARIO2_NOEXISTE);
			}
			
			//Crear deuda usuario origen
			Deuda deuda1 = new Deuda();
			deuda1.setCantidad(cantidad);
			deuda1.setConcepto(concepto);
			deuda1.setFechaRegistroN(fecha);
			deuda1.setIdDeuda(identificador);
			deuda1.setSaldada(false);
			deuda1.setSaldado(0);
			deuda1.setUsuario(usuarioOrigen);
			deuda1.setUsuarioDestino(usuarioDestino);
			deudaDAO.nuevo(deuda1);
			session.flush();
			session.clear();
			
			resultado = deuda1.getId();
			
			commit(tx);		
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;			
		}
		
		return resultado;
	}

	@Override
	public List<Deuda> consultarDeudas(long idUsuario, TipoDeuda tipo, boolean saldada) {
		List<Deuda> resultado = null;
		Transaction tx = null;
		
		try
		{
			session = initSession();
			tx = initTx(session);
			
			deudaDAO.setSession(session);
			resultado = deudaDAO.consultarDeudas(idUsuario, tipo, saldada);
			
			commit(tx);
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}
					
		return resultado;
	}

	@Override
	public Deuda consultarDeuda(long idDeuda) {
		Deuda resultado = null;
		Transaction tx = null;
		
		try
		{
			session = initSession();
			tx = initTx(session);
			
			deudaDAO.setSession(session);
			resultado = deudaDAO.consultarDeuda(idDeuda);
			
			commit(tx);
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}
					
		return resultado;
	}

	@Override
	public void actualizarConcepto(long idDeuda, String concepto) throws Exception {
		Transaction tx = null;
		
		try
		{
			//Datos de conexión
			session = initSession();
			tx = initTx(session);
			
			deudaDAO.setSession(session);
			
			//Consultar deuda
			Deuda deuda = deudaDAO.consultarDeuda(idDeuda);
			session.flush();
			if (deuda == null)
			{
				throw new ExcepcionDeuda(KMensajes.MSG_DEUDA_NOEXISTE);
			}
			
			//Actualizar los campos
			deuda.setConcepto(concepto);
			deudaDAO.actualizar(deuda);
			
			commit(tx);
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}
		
	}

	@Override
	public void borrarDeuda(long idDeuda) throws Exception {
		Transaction tx = null;
		
		try
		{
			//Datos de conexión
			session = initSession();
			tx = initTx(session);
			
			OperacionDAO operacionDAO = OperacionDAO.getInstance();
			deudaDAO.setSession(session);
			operacionDAO.setSession(session);
			
			//Consultar deuda
			Deuda deuda = deudaDAO.consultarDeuda(idDeuda);
			session.flush();
			if (deuda == null)
			{
				throw new ExcepcionDeuda(KMensajes.MSG_DEUDA_NOEXISTE);
			}
			
			//Borrar
			operacionDAO.borrarOperaciones(idDeuda);
			session.flush();
			
			deudaDAO.borrarDeuda(idDeuda);
			session.flush();
			
			commit(tx);
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}
	}

	@Override
	public ResumenDeuda consultarResumen(long idUsuario) {
		ResumenDeuda resultado = null;
		Transaction tx = null;
		
		try
		{
			session = initSession();
			tx = initTx(session);
			
			deudaDAO.setSession(session);
			resultado = deudaDAO.consultarResumen(idUsuario);
			
			commit(tx);
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}
					
		return resultado;
	}
}
