/**
 * 
 */
package es.jma.prestamos.daos.impl;

import java.util.List;

import org.hibernate.Query;

import es.jma.prestamos.daos.IOperacionDAO;
import es.jma.prestamos.dominio.Operacion;
import es.jma.prestamos.dominio.Usuario;
import es.jma.prestamos.enums.TipoOperacion;

/**
 * Implementación del DAO Operacion
 * @author jmiranda
 *
 */
public class OperacionDAO extends CrudDAOHibImpl implements IOperacionDAO {
	private static OperacionDAO instance;
	
	private OperacionDAO()
	{
		super();
	}
	
	public static OperacionDAO getInstance()
	{
		if (instance == null)
		{
			instance = new OperacionDAO();
		}
		
		return instance;
	}

	@Override
	public List<Operacion> consultarOperaciones(long idDeuda,
			TipoOperacion tipoOperacion, Usuario usuario) {
		List<Operacion> respuesta = null;
		
		try
		{
			boolean hayTipo = false, hayUsuario = false;
			
			if (tipoOperacion != null)
			{
				hayTipo = true;
			}
			if (usuario != null)
			{
				hayUsuario = true;
			}
			
			//Consultar las operaciones de una deuda
			StringBuilder hql = new StringBuilder("FROM Operacion o WHERE o.deuda.id = :idDeuda ");
			
			//Si hay tipo, filtrar
			if (hayTipo)				
			{
				hql.append("AND o.tipo = :tipo ");
			}
			//Si hay usuario, filtrar
			if (hayUsuario)
			{
				hql.append("AND o.usuario.id = :idUsuario");
			}
			hql.append(" ORDER BY o.fechaRegistro DESC");
			Query query = session.createQuery(hql.toString());
			query.setLong("idDeuda", idDeuda);
			
			if (hayTipo)
			{
				query.setInteger("tipo", tipoOperacion.ordinal());
			}
			if (hayUsuario)
			{
				query.setLong("idUsuario", usuario.getId());
			}
			
			@SuppressWarnings("unchecked")
			List<Operacion> results = query.list();
			
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
	public void borrarOperaciones(long idDeuda) {
		try
		{
			//Consultar bar por nombre
			String hql = "DELETE FROM Operacion o WHERE o.deuda.id = :idDeuda";
			Query query = session.createQuery(hql);
			query.setLong("idDeuda", idDeuda);
			query.executeUpdate();
			
		}
		catch (Exception e)
		{
			throw e;
		}
		
	}
}
