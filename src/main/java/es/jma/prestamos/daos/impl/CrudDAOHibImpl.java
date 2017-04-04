/**
 * 
 */
package es.jma.prestamos.daos.impl;

import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;

import es.jma.prestamos.daos.ICrudDAO;

/**
 * Implementación de ICrudDAO
 * @author jmiranda
 *
 */
public class CrudDAOHibImpl implements ICrudDAO {
	protected Session session;
	private static CrudDAOHibImpl instance;
	
	
	protected CrudDAOHibImpl()
	{

	}

	public void setSession(Session session)
	{
		this.session = session;
	}
	
	public static CrudDAOHibImpl getInstance()
	{
		if (instance == null)
		{
			instance = new CrudDAOHibImpl();
		}
		
		return instance;
	}
	
	@Override
	@Transactional
	public void nuevo(Object objeto) throws Exception {
		try
		{
			session.saveOrUpdate(objeto);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	@Override
	@Transactional
	public void actualizar(Object objeto) throws Exception {
		try
		{
			session.update(objeto);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

	@Override
	@Transactional
	public void borrar(Object objeto) throws Exception {
		try
		{
			session.delete(objeto);
		}
		catch (Exception e)
		{
			throw e;
		}
	}

}
