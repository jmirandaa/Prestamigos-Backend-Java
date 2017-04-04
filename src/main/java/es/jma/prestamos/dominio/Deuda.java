/**
 * 
 */
package es.jma.prestamos.dominio;

import static es.jma.prestamos.utils.UtilValidador.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.jma.prestamos.exceptiones.ExcepcionDeuda;

/**
 * POJO para guardar la información de una deuda
 * @author jmiranda
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Deuda {
	//Atributos
	@Id
	@GeneratedValue
	private long id ;
	private String idDeuda;
	private LocalDateTime fechaRegistro;
	private String concepto ;
	private double cantidad ;
	private double saldado ;
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="deuda")
	@JsonIgnore
	private List<Operacion> operaciones ;
	private boolean saldada ;
	@ManyToOne(cascade = CascadeType.ALL)
	private Usuario usuario ;
	@ManyToOne(cascade = CascadeType.ALL)
	private Usuario usuarioDestino;
	
	//Constructores
	public Deuda()
	{
		
	}
	
	public Deuda (String concepto, double cantidad, double saldado, boolean saldada, Usuario usuario, Usuario usuarioDestino)
	{
		this.concepto = concepto;
		this.cantidad = cantidad;
		this.saldado = saldado;
		this.usuario = usuario;
		this.usuarioDestino = usuarioDestino;
		this.saldada = saldada;
	}

	/**
	 * @return the id
	 */
	public long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(long id) {
		this.id = id;
	}



	/**
	 * @return the idDeuda
	 */
	public String getIdDeuda() {
		return idDeuda;
	}

	/**
	 * @param idDeuda the idDeuda to set
	 */
	public void setIdDeuda(String idDeuda) {
		this.idDeuda = idDeuda;
	}

	/**
	 * @return the usuarioDestino
	 */
	public Usuario getUsuarioDestino() {
		return usuarioDestino;
	}

	/**
	 * @param usuarioDestino the usuarioDestino to set
	 */
	public void setUsuarioDestino(Usuario usuarioDestino) {
		this.usuarioDestino = usuarioDestino;
	}

	/**
	 * @return the fechaRegistro
	 */
	public Date getFechaRegistro() {
		Date date = null;
		if (fechaRegistro != null)
		{
			date = Date.from(fechaRegistro.toInstant(ZoneOffset.UTC));
		}
		return date;
	}
	
	@JsonIgnore
	public LocalDateTime getFechaRegistroN()
	{
		return fechaRegistro;
	}

	/**
	 * @param fechaRegistro the fechaRegistro to set
	 */
	public void setFechaRegistro(Date fechaRegistro) {
		if (fechaRegistro != null)
		{
			this.fechaRegistro = LocalDateTime.ofInstant(fechaRegistro.toInstant(), ZoneId.systemDefault());
		}
	}
	
	public void setFechaRegistroN(LocalDateTime fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	/**
	 * @return the concepto
	 */
	public String getConcepto() {
		return concepto;
	}

	/**
	 * @param concepto the concepto to set
	 */
	public void setConcepto(String concepto) {
		if (!esTexto(concepto))
		{
			throw new ExcepcionDeuda("El concepto no puede estar vacío o menor de "+TAM_MIN);
		}
		this.concepto = concepto;
	}

	/**
	 * @return the cantidad
	 */
	public double getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(double cantidad) {
		if (!esPositivo(cantidad))
		{
			throw new ExcepcionDeuda("La cantidad tiene que ser positiva");
		}
		this.cantidad = cantidad;
	}

	/**
	 * @return the saldado
	 */
	public double getSaldado() {
		return saldado;
	}

	/**
	 * @param saldado the saldado to set
	 */
	public void setSaldado(double saldado) {
		this.saldado = saldado;
	}


	/**
	 * @return the operaciones
	 */
	public List<Operacion> getOperaciones() {
		return operaciones;
	}

	/**
	 * @param operaciones the operaciones to set
	 */
	public void setOperaciones(List<Operacion> operaciones) {
		this.operaciones = operaciones;
	}

	/**
	 * @return the saldada
	 */
	public boolean isSaldada() {
		return saldada;
	}

	/**
	 * @param saldada the saldada to set
	 */
	public void setSaldada(boolean saldada) {
		this.saldada = saldada;
	}

	/**
	 * @return the usuario
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Deuda [id=" + id + ", idDeuda=" + idDeuda + ", fechaRegistro=" + fechaRegistro + ", concepto="
				+ concepto + ", cantidad=" + cantidad + ", saldado=" + saldado +  ", operaciones="
				+ operaciones + ", saldada=" + saldada + ", usuario=" + usuario + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(cantidad);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((concepto == null) ? 0 : concepto.hashCode());
		result = prime * result + ((fechaRegistro == null) ? 0 : fechaRegistro.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((idDeuda == null) ? 0 : idDeuda.hashCode());
		result = prime * result + ((operaciones == null) ? 0 : operaciones.hashCode());
		result = prime * result + (saldada ? 1231 : 1237);
		temp = Double.doubleToLongBits(saldado);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Deuda other = (Deuda) obj;
		if (Double.doubleToLongBits(cantidad) != Double.doubleToLongBits(other.cantidad))
			return false;
		if (concepto == null) {
			if (other.concepto != null)
				return false;
		} else if (!concepto.equals(other.concepto))
			return false;
		if (fechaRegistro == null) {
			if (other.fechaRegistro != null)
				return false;
		} else if (!fechaRegistro.equals(other.fechaRegistro))
			return false;
		if (id != other.id)
			return false;
		if (idDeuda == null) {
			if (other.idDeuda != null)
				return false;
		} else if (!idDeuda.equals(other.idDeuda))
			return false;
		if (operaciones == null) {
			if (other.operaciones != null)
				return false;
		} else if (!operaciones.equals(other.operaciones))
			return false;
		if (saldada != other.saldada)
			return false;
		if (Double.doubleToLongBits(saldado) != Double.doubleToLongBits(other.saldado))
			return false;
		if (usuario == null) {
			if (other.usuario != null)
				return false;
		} else if (!usuario.equals(other.usuario))
			return false;
		return true;
	}
	
	
}
