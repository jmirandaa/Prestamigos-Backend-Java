/**
 * 
 */
package es.jma.prestamos.dominio;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.jma.prestamos.enums.TipoOperacion;

/**
 * POJO para guardar la información de las operaciones
 * que se realizan sobre las deudas
 * @author jmiranda
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Operacion {
	//Atributos
	@Id
	@GeneratedValue
	private long id;
	private LocalDateTime fechaRegistro;
	private double cantidad;
	@ManyToOne(cascade = CascadeType.ALL)
	private Deuda deuda;
	@Enumerated(EnumType.ORDINAL)
	private TipoOperacion tipo;
	@ManyToOne(cascade = CascadeType.ALL)
	private Usuario usuario;

	//Constructores
	public Operacion()
	{
		
	}
	
	public Operacion(double cantidad, Deuda deuda, TipoOperacion tipo, Usuario usuario)
	{
		this.cantidad = cantidad;
		this.deuda = deuda;
		this.tipo = tipo;
		this.usuario = usuario;
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
	public LocalDateTime getFechaRegistroN() {
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
	 * @return the cantidad
	 */
	public double getCantidad() {
		return cantidad;
	}

	/**
	 * @param cantidad the cantidad to set
	 */
	public void setCantidad(double cantidad) {
		this.cantidad = cantidad;
	}

	/**
	 * @return the deuda
	 */
	public Deuda getDeuda() {
		return deuda;
	}

	/**
	 * @param deuda the deuda to set
	 */
	public void setDeuda(Deuda deuda) {
		this.deuda = deuda;
	}

	/**
	 * @return the tipo
	 */
	public TipoOperacion getTipo() {
		return tipo;
	}

	/**
	 * @param tipo the tipo to set
	 */
	public void setTipo(TipoOperacion tipo) {
		this.tipo = tipo;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Operacion [id=" + id + ", fechaRegistro=" + fechaRegistro + ", cantidad=" + cantidad + ", deuda="
				+ deuda + ", tipo=" + tipo + "]";
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
		result = prime * result + ((deuda == null) ? 0 : deuda.hashCode());
		result = prime * result + ((fechaRegistro == null) ? 0 : fechaRegistro.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((tipo == null) ? 0 : tipo.hashCode());
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
		Operacion other = (Operacion) obj;
		if (Double.doubleToLongBits(cantidad) != Double.doubleToLongBits(other.cantidad))
			return false;
		if (deuda == null) {
			if (other.deuda != null)
				return false;
		} else if (!deuda.equals(other.deuda))
			return false;
		if (fechaRegistro == null) {
			if (other.fechaRegistro != null)
				return false;
		} else if (!fechaRegistro.equals(other.fechaRegistro))
			return false;
		if (id != other.id)
			return false;
		if (tipo != other.tipo)
			return false;
		return true;
	}
	
	
}
