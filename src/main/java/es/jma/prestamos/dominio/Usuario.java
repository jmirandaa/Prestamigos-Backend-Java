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
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import es.jma.prestamos.exceptiones.ExcepcionUsuario;

/**
 * POJO para guardar datos de usuarios
 * @author jmiranda
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Usuario {
	//Atributos
	@Id
	@GeneratedValue
	private long id;
	private LocalDateTime fechaRegistro;
	private String email;
	private String password;
	private String nombre;
	private String apellidos;
	private String avatarBase64;
	private boolean registrado;
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="usuario")
	@JsonIgnore
	private List<Deuda> deudas;
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="usuario")
	@JsonIgnore
	private List<Operacion> operaciones;
	@ManyToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	@JsonIgnore
	private List<Usuario> amigos;
	
	//Constructores
	public Usuario()
	{
		
	}
	
	public Usuario(String nombre, String apellidos)
	{
		setNombre(nombre);
		setApellidos(apellidos);
	}
	
	public Usuario(String nombre, String apellidos, String email, String password)
	{
		this(nombre,apellidos);
		setEmail(email);
		setPassword(password);
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
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		if (!esEmail(email))
		{
			throw new ExcepcionUsuario("El email no es válido");
		}
		this.email = email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		if (!esTexto(password))
		{
			throw new ExcepcionUsuario("La contraseña no puede estar vacía");
		}
		this.password = password;
	}

	/**
	 * @return the nombre
	 */
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre the nombre to set
	 */
	public void setNombre(String nombre) {
		if (!esTexto(nombre))
		{
			throw new ExcepcionUsuario("El nombre no puede estar vacío");
		}
		this.nombre = nombre;
	}

	/**
	 * @return the apellidos
	 */
	public String getApellidos() {
		return apellidos;
	}

	/**
	 * @param apellidos the apellidos to set
	 */
	public void setApellidos(String apellidos) {
		if (!esTexto(apellidos))
		{
			throw new ExcepcionUsuario("Los apellidos no pueden estar vacío");
		}		
		this.apellidos = apellidos;
	}

	/**
	 * 
	 * @return the avatarBase64
	 */

	public String getAvatarBase64() {
		return avatarBase64;
	}

	/**
	 * @param avatarBase64 the avatarBase64 to set
	 */
	public void setAvatarBase64(String avatarBase64) {
		this.avatarBase64 = avatarBase64;
	}

	/**
	 * @return the registrado
	 */
	public boolean isRegistrado() {
		return registrado;
	}

	/**
	 * @param registrado the registrado to set
	 */
	public void setRegistrado(boolean registrado) {
		this.registrado = registrado;
	}

	/**
	 * @return the deudas
	 */
	public List<Deuda> getDeudas() {
		return deudas;
	}

	/**
	 * @param deudas the deudas to set
	 */
	public void setDeudas(List<Deuda> deudas) {
		this.deudas = deudas;
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
	 * @return the amigos
	 */
	public List<Usuario> getAmigos() {
		return amigos;
	}

	/**
	 * @param amigos the amigos to set
	 */
	public void setAmigos(List<Usuario> amigos) {
		this.amigos = amigos;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amigos == null) ? 0 : amigos.hashCode());
		result = prime * result + ((apellidos == null) ? 0 : apellidos.hashCode());
		result = prime * result + ((avatarBase64 == null) ? 0 : avatarBase64.hashCode());
		result = prime * result + ((deudas == null) ? 0 : deudas.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fechaRegistro == null) ? 0 : fechaRegistro.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + ((operaciones == null) ? 0 : operaciones.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + (registrado ? 1231 : 1237);
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
		Usuario other = (Usuario) obj;
		if (amigos == null) {
			if (other.amigos != null)
				return false;
		} else if (!amigos.equals(other.amigos))
			return false;
		if (apellidos == null) {
			if (other.apellidos != null)
				return false;
		} else if (!apellidos.equals(other.apellidos))
			return false;
		if (avatarBase64 == null) {
			if (other.avatarBase64 != null)
				return false;
		} else if (!avatarBase64.equals(other.avatarBase64))
			return false;
		if (deudas == null) {
			if (other.deudas != null)
				return false;
		} else if (!deudas.equals(other.deudas))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fechaRegistro == null) {
			if (other.fechaRegistro != null)
				return false;
		} else if (!fechaRegistro.equals(other.fechaRegistro))
			return false;
		if (id != other.id)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (operaciones == null) {
			if (other.operaciones != null)
				return false;
		} else if (!operaciones.equals(other.operaciones))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (registrado != other.registrado)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Usuario [id=" + id + ", fechaRegistro=" + fechaRegistro + ", email=" + email + ", password=" + password
				+ ", nombre=" + nombre + ", apellidos=" + apellidos + ", avatarBase64=" + avatarBase64 + ", registrado="
				+ registrado + ", deudas=" + deudas + ", operaciones=" + operaciones + ", amigos=" + amigos + "]";
	}
	
	
}
