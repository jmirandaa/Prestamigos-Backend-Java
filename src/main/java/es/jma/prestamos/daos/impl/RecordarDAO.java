/**
 * 
 */
package es.jma.prestamos.daos.impl;

import java.util.Calendar;
import java.util.List;

import org.hibernate.Query;

import es.jma.prestamos.daos.IRecordarDAO;
import es.jma.prestamos.dominio.Recordar;

/**
 * Implementación del DAO Recordar
 * @author jmiranda
 *
 */
public class RecordarDAO extends CrudDAOHibImpl implements IRecordarDAO {
	private static RecordarDAO instance;
	
	private RecordarDAO()
	{
		super();
	}
	
	public static RecordarDAO getInstance()
	{
		if (instance == null)
		{
			instance = new RecordarDAO();
		}
		
		return instance;
	}
	
	/* (non-Javadoc)
	 * @see es.jma.prestamos.daos.IRecordarDAO#consultarToken(java.lang.String)
	 */
	@Override
	public Recordar consultarToken(String email) {
		Recordar resultado = null;
		
		try
		{
			//Consultar token
			String hql = "SELECT r FROM Recordar r WHERE r.email = :email AND LENGTH(r.token) > 6 AND r.expire >= :dateNow ORDER BY r.expire DESC";
			Query query = session.createQuery(hql);
			query.setString("email", email);
			query.setDate("dateNow", Calendar.getInstance().getTime());
			@SuppressWarnings("unchecked")
			List<Recordar> results = query.list();
			
			if ((results != null ) && (results.size() > 0))
			{
				resultado = results.get(0);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return resultado;
	}

	/* (non-Javadoc)
	 * @see es.jma.prestamos.daos.IRecordarDAO#consultarCodigoNumerico(java.lang.String)
	 */
	@Override
	public Recordar consultarCodigoNumerico(String email) {
		Recordar resultado = null;
		
		try
		{
			//Consultar código
			String hql = "SELECT r FROM Recordar r WHERE r.email = :email AND LENGTH(r.token) < 7 AND r.expire >= :dateNow ORDER BY r.expire DESC";
			Query query = session.createQuery(hql);
			query.setString("email", email);
			query.setDate("dateNow", Calendar.getInstance().getTime());
			@SuppressWarnings("unchecked")
			List<Recordar> results = query.list();
			
			if ((results != null ) && (results.size() > 0))
			{
				resultado = results.get(0);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return resultado;
	}

	/* (non-Javadoc)
	 * @see es.jma.prestamos.daos.IRecordarDAO#consultarCodigoNumerico(java.lang.String, java.lang.String)
	 */
	@Override
	public Recordar consultarCodigoNumerico(String email, String code) {
		Recordar resultado = null;
		
		try
		{
			//Consultar código
			String hql = "SELECT r FROM Recordar r WHERE r.email = :email AND r.token = :code AND r.expire >= :dateNow ORDER BY r.expire DESC";
			Query query = session.createQuery(hql);
			query.setString("email", email);
			query.setString("code", code);
			query.setDate("dateNow", Calendar.getInstance().getTime());
			@SuppressWarnings("unchecked")
			List<Recordar> results = query.list();
			
			if ((results != null ) && (results.size() > 0))
			{
				resultado = results.get(0);
			}
		}
		catch (Exception e)
		{
			throw e;
		}
		
		return resultado;
	}

}
