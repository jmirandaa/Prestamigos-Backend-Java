/**
 * 
 */
package es.jma.prestamos.daos.impl;

import java.util.List;

import org.hibernate.Query;

import es.jma.prestamos.daos.IDeudaDAO;
import es.jma.prestamos.dominio.Deuda;
import es.jma.prestamos.dominio.ResumenDeuda;
import es.jma.prestamos.enums.TipoDeuda;

/**
 * Implementación del DAO Deuda
 * @author jmiranda
 *
 */
public class DeudaDAO extends CrudDAOHibImpl implements IDeudaDAO {
	private static DeudaDAO instance;
	
	private DeudaDAO()
	{
		super();
	}
	
	public static DeudaDAO getInstance()
	{
		if (instance == null)
		{
			instance = new DeudaDAO();
		}
		
		return instance;
	}

	@Override
	public List<Deuda> consultarDeudas(String email, TipoDeuda tipo, boolean saldada) {
		List<Deuda> respuesta = null;
		
		try
		{
			StringBuilder hql = new StringBuilder();
			hql.append("From Deuda d ");
			//Consultar deudas
			if (tipo == TipoDeuda.DEBEN)
			{
				hql.append("WHERE d.usuarioDestino.email = :email");
			}
			else if (tipo == TipoDeuda.DEBO)
			{
				hql.append("WHERE d.usuario.email = :email");
			}
			else if (tipo == TipoDeuda.TODAS)
			{
				hql.append("WHERE (d.usuario.email = :email OR d.usuarioDestino.email = :email)");
			}

			hql.append(" AND d.saldada = :saldada ORDER BY d.fechaRegistro DESC");
			
			Query query = session.createQuery(hql.toString());
			query.setString("email", email);
			query.setBoolean("saldada", saldada);
			@SuppressWarnings("unchecked")
			List<Deuda> results = query.list();
			
			if ((results != null ) && (results.size() > 0))
			{
				respuesta = results;
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return respuesta;
	}

	@Override
	public List<Deuda> consultarDeudas(long idUsuario, TipoDeuda tipo, boolean saldada) {
		List<Deuda> respuesta = null;
		
		try
		{
			StringBuilder hql = new StringBuilder();
			hql.append("From Deuda d ");
			//Consultar deudas
			if (tipo == TipoDeuda.DEBEN)
			{
				hql.append("WHERE d.usuarioDestino.id = :id");
			}
			else if (tipo == TipoDeuda.DEBO)
			{
				hql.append("WHERE d.usuario.id = :id");
			}
			else if (tipo == TipoDeuda.TODAS)
			{
				hql.append("WHERE (d.usuario.id = :id OR d.usuarioDestino.id = :id)");
			}

			hql.append(" AND d.saldada = :saldada ORDER BY d.fechaRegistro DESC");
			
			Query query = session.createQuery(hql.toString());
			query.setLong("id", idUsuario);
			query.setBoolean("saldada", saldada);
			@SuppressWarnings("unchecked")
			List<Deuda> results = query.list();
			
			if ((results != null ) && (results.size() > 0))
			{
				respuesta = results;
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return respuesta;
	}

	@Override
	public Deuda consultarDeuda(long idDeuda) {
		Deuda respuesta = null;
		
		try
		{
			String hql = "FROM Deuda WHERE id = :idDeuda ";

			Query query = session.createQuery(hql.toString());
			query.setLong("idDeuda", idDeuda);
			
			@SuppressWarnings("unchecked")
			List<Deuda> results = query.list();
			
			if ((results != null ) && (results.size() > 0))
			{
				respuesta = results.get(0);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return respuesta;
	}

	@Override
	public void borrarDeuda(long idDeuda) {
		try
		{
			//Consultar bar por nombre
			String hql = "DELETE FROM Deuda WHERE id = :idDeuda";
			Query query = session.createQuery(hql);
			query.setLong("idDeuda", idDeuda);
			query.executeUpdate();
			
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	@Override
	public ResumenDeuda consultarResumen(long idUsuario) {
		ResumenDeuda respuesta = null;
		
		try
		{
			respuesta = new ResumenDeuda();
			
			//Consultar número de deudas que debo
			StringBuilder hql = new StringBuilder("SELECT count(*) as num_deudas_debo FROM Deuda d WHERE d.usuario.id = :idUsuario AND d.saldada = false");
			Query queryNumDeudasDebo = session.createQuery(hql.toString());
			queryNumDeudasDebo.setLong("idUsuario", idUsuario);
			long numDeudasDebo = 0;
			Object resultUnique = queryNumDeudasDebo.uniqueResult();
			if (resultUnique != null)
			{
				numDeudasDebo = (long) resultUnique;
			}
			respuesta.setNumDeudasDebo((int)numDeudasDebo);
			
			//Consultar número de deudas que deben
			hql.setLength(0);
			hql.append("SELECT count(*) as num_deudas_deben FROM Deuda d WHERE d.usuarioDestino.id = :idUsuario AND d.saldada = false");
			Query queryNumDeudasDeben = session.createQuery(hql.toString());
			queryNumDeudasDeben.setLong("idUsuario", idUsuario);
			long numDeudasDeben = 0;
			resultUnique = queryNumDeudasDeben.uniqueResult();
			if (resultUnique != null)
			{
				numDeudasDeben = (long) resultUnique;
			}
			respuesta.setNumDeudasMeDeben((int)numDeudasDeben);
			
			//Consultar total de deudas que debo
			hql.setLength(0);
			hql.append("SELECT sum(d.cantidad - d.saldado) as total_deudas_debo FROM Deuda d WHERE d.usuario.id = :idUsuario AND d.saldada = false");
			Query queryTotalDeudasDebo = session.createQuery(hql.toString());
			queryTotalDeudasDebo.setLong("idUsuario", idUsuario);
			double totalDeudasDebo = 0.0;
			resultUnique = queryTotalDeudasDebo.uniqueResult();
			if (resultUnique != null)
			{
				totalDeudasDebo = (double) resultUnique;
			}
			respuesta.setTodalDebo(totalDeudasDebo);
			
			//Consultar total de deudas que me deben
			hql.setLength(0);
			hql.append("SELECT sum(d.cantidad - d.saldado) as total_deudas_deben FROM Deuda d WHERE d.usuarioDestino.id = :idUsuario AND d.saldada = false");
			Query queryTotalDeudasDeben = session.createQuery(hql.toString());
			queryTotalDeudasDeben.setLong("idUsuario", idUsuario);
			double totalDeudasDeben = 0.0;
			resultUnique = queryTotalDeudasDeben.uniqueResult();
			if (resultUnique != null)
			{
				totalDeudasDeben = (double) resultUnique;
			}
			respuesta.setTotalMeDeben(totalDeudasDeben);
			
			//Total
			respuesta.setTotal(totalDeudasDebo-totalDeudasDeben);
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return respuesta;
	}
}
