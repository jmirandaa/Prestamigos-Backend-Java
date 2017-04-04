/**
 * 
 */
package es.jma.prestamos.servicios.impl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import es.jma.prestamos.daos.ICrudDAO;
import es.jma.prestamos.daos.impl.CrudDAOHibImpl;

/**
 * Clase utilizada por los servicios de Hibernate
 * @author jmiranda
 *
 */
public abstract class ServiciosHibernate implements ICrudDAO{
	@Autowired
	protected SessionFactory sessionFactory;
	//Atributo para no hacer commits nunca
	protected boolean txEnabled;
	private CrudDAOHibImpl crudDAO;
	
	//Session y TX
	protected Session session;
	protected Transaction tx;	
	
	protected ServiciosHibernate(SessionFactory sessionFactory)
	{
		this.sessionFactory = sessionFactory;
		this.txEnabled = true;
		this.crudDAO = CrudDAOHibImpl.getInstance();
	}
	
	/**
	 * Hacer commit de una transacción si procede
	 * @param tx
	 */
	protected void commit(Transaction tx)
	{
		if (tx != null)
		{
			if (txEnabled)
			{
				tx.commit();
			}

		}
	}
	
	/**
	 * Si las transacciones están desactivadas,
	 * usar la sessión pasada
	 * @return
	 */
	protected Session initSession()
	{
		if (txEnabled)
		{
			return sessionFactory.getCurrentSession();
		}
		else
		{
			return this.session;
		}
		
	}
	
	/** 
	 * Si las transacciones están desactivadas,
	 * usar la pasada
	 * @return
	 */
	protected Transaction initTx(Session session)
	{
		if (txEnabled)
		{
			return session.beginTransaction();
		}
		else
		{
			return this.tx;
		}		
	}
	
	/**
	 * Hacer rollback de una transacción si procede
	 * @param tx
	 */
	protected void rollback(Transaction tx)
	{
		if (tx != null)
		{
			tx.rollback();
		}
	}
	
	/**
	 * Cerrar sesión de Hibernate si procede
	 * @param session
	 */
	protected void cerrarSesion(Session session)
	{
		if ((session != null) && (txEnabled))
		{
			session.close();
		}
	}
	

	/**
	 * @return the txEnabled
	 */
	public boolean isTxEnabled() {
		return txEnabled;
	}

	/**
	 * @param txEnabled the txEnabled to set
	 */
	public void setTxEnabled(boolean txEnabled) {
		this.txEnabled = txEnabled;
	}
	
	@Override
	public void nuevo (Object objeto) throws Exception
	{    	
		try
		{
			if (objeto != null)
			{

				session = initSession();
				tx = initTx(session);
		
				//Insertar objeto
				crudDAO.setSession(session);
				crudDAO.nuevo(objeto);
				commit(tx);
			}
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}
		
	}
	
	@Override
	public void actualizar (Object objeto) throws Exception
	{
    	
		try
		{
			if (objeto != null)
			{
				session = initSession();
				tx = initTx(session);				
			
				//Actualizar
				crudDAO.setSession(session);
				crudDAO.actualizar(objeto);
				commit(tx);
			}
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}
	
	}
	
	@Override
	public void borrar (Object objeto) throws Exception
	{
    	
		try
		{
			if (objeto != null)
			{
				session = initSession();
				tx = initTx(session);				
			
				//Borrar
				crudDAO.setSession(session);
				crudDAO.borrar(objeto);
				commit(tx);
			}
		}
		catch (Exception e)
		{
			rollback(tx);
			throw e;
		}
		
	}

	/**
	 * @param session the session to set
	 */
	public void setSession(Session session) {
		this.session = session;
	}


	/**
	 * @param tx the tx to set
	 */
	public void setTx(Transaction tx) {
		this.tx = tx;
	}
	
	
		
}
