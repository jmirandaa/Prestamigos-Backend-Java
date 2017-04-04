/**
 * 
 */
package es.jma.prestamos.servicios.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import es.jma.prestamos.constantes.KMensajes;
import es.jma.prestamos.daos.impl.DeudaDAO;
import es.jma.prestamos.daos.impl.OperacionDAO;
import es.jma.prestamos.daos.impl.UsuarioDAO;
import es.jma.prestamos.dominio.Deuda;
import es.jma.prestamos.dominio.Operacion;
import es.jma.prestamos.dominio.Usuario;
import es.jma.prestamos.enums.TipoOperacion;
import es.jma.prestamos.exceptiones.ExcepcionOperacion;
import es.jma.prestamos.servicios.IServiciosOperacion;

/**
 * Implementación del servicio Operacion
 * @author jmiranda
 *
 */
public class ServiciosOperacion extends ServiciosHibernate implements IServiciosOperacion {
	private static ServiciosOperacion instance;
	private OperacionDAO operacionDAO;
	
	private ServiciosOperacion(SessionFactory sessionFactory)
	{
		super(sessionFactory);
		operacionDAO = OperacionDAO.getInstance();
	}
	
	public static ServiciosOperacion getInstance(SessionFactory sessionFactory)
	{
		if (instance == null)
		{
			instance = new ServiciosOperacion(sessionFactory);
		}
		return instance;
	}

	@Override
	public List<Operacion> consultarOperaciones(long idDeuda,
			TipoOperacion tipoOperacion, Usuario usuario) {
		List<Operacion> resultado = null;
		Transaction tx = null;
		try
		{
			session = initSession();
			tx = initTx(session);
			
			//Borrar
			operacionDAO.setSession(session);
			resultado = operacionDAO.consultarOperaciones(idDeuda, tipoOperacion, usuario);
			
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
	public long nuevaOperacion(Operacion operacion) throws Exception {
		long resultado = -1;
		Transaction tx = null;
		
		try
		{
			//Comprobación de parámetros obligatorios
			long idDeuda = operacion.getDeuda().getId();
			long idUsuario = operacion.getUsuario().getId();
			double cantidad = operacion.getCantidad();
			TipoOperacion tipoOperacion = operacion.getTipo();
			
			if ((idDeuda <= 0) || (idUsuario <= 0) || (cantidad <= 0))
			{
				throw new ExcepcionOperacion(KMensajes.MSG_ERROR_NULL);
			}
			
			//Datos de conexión
			session = initSession();
			tx = initTx(session);
			session.clear();			
			DeudaDAO deudaDAO = DeudaDAO.getInstance();
			UsuarioDAO usuarioDAO = UsuarioDAO.getInstance();
			
			operacionDAO.setSession(session);
			deudaDAO.setSession(session);
			usuarioDAO.setSession(session);
			
			//Consultar deuda
			Deuda deuda = deudaDAO.consultarDeuda(idDeuda);
			session.flush();
			session.clear();
			if (deuda == null)
			{
				throw new ExcepcionOperacion(KMensajes.MSG_DEUDA_NOEXISTE);
			}
			
			//Actualizar la deuda
			double valorDeuda = deuda.getCantidad();
			double saldado = deuda.getSaldado();
			
			//Si se está pagando, actualizar
			if (tipoOperacion.equals(TipoOperacion.PAGAR))
			{
				saldado += cantidad;
				
				//Lo daldado no puede ser mayor al valor
				if (saldado > valorDeuda)
				{
					throw new ExcepcionOperacion(-2,KMensajes.MSG_OP_SALDADO_MAS);
				}
				else
				{
					//Si lo saldado es igual al valor, se da por saldada
					if (saldado == valorDeuda)
					{
						deuda.setSaldada(true);
					}
					//Actualizar
					deuda.setSaldado(saldado);
				}
			}
			//Si se está aumentando, aumentar la deuda
			else
			{
				deuda.setCantidad(valorDeuda+cantidad);
			}
			
			//Consultar Usuario
			Usuario usuario = null;
			
			//O es el usuario que debe
			if (idUsuario == deuda.getUsuario().getId())
			{
				usuario = deuda.getUsuario();
			}
			//O es al que le deben
			else if (idUsuario == deuda.getUsuarioDestino().getId())
			{
				usuario = deuda.getUsuarioDestino();
			}
			else
			{
				throw new ExcepcionOperacion(KMensajes.MSG_DEUDA_USUARIO_NOEXISTE);
			}
			
			//Crear la operación
			Operacion nuevaOperacion = new Operacion();
			nuevaOperacion.setCantidad(cantidad);
			nuevaOperacion.setDeuda(deuda);
			nuevaOperacion.setFechaRegistroN(LocalDateTime.now());
			nuevaOperacion.setTipo(tipoOperacion);
			nuevaOperacion.setUsuario(usuario);
			
			usuarioDAO.nuevo(nuevaOperacion);
			session.flush();
					
			resultado = nuevaOperacion.getId();
			
			commit(tx);
		}
		catch (NullPointerException e)
		{
			throw new ExcepcionOperacion(KMensajes.MSG_ERROR_NULL);
		}		
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}	
		
		return resultado;
	}

	@Override
	public void borrarOperaciones(long idDeuda) {
		Transaction tx = null;
		try
		{
			session = initSession();
			tx = initTx(session);
			
			//Borrar
			operacionDAO.setSession(session);
			operacionDAO.borrarOperaciones(idDeuda);
			
			commit(tx);
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}	
	}
}
