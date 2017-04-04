/**
 * 
 */
package es.jma.prestamos.constantes;

/**
 * Constantes para los mensajes de respuesta
 * @author jmiranda
 *
 */
public class KMensajes {
	//Usuario
	public static final String MSG_LOGIN_ERR = "Error al comprobar usuario";
	public static final String MSG_LOGIN_INCORRECTO = "Usuario o contraseña incorrecta";
	public static final String MSG_NUEVO_USUARIO_EXISTE = "Usuario ya registrado";
	public static final String MSG_ACTUALIZAR_USUARIO_NOEXISTE = "Usuario no registrado";
	
	//Deuda
	public static final String MSG_DEUDA_USUARIO1_NOEXISTE = "El usuario origen no existe";
	public static final String MSG_DEUDA_USUARIO2_NOEXISTE = "El usuario destino no existe";
	
	//Operacion
	public static final String MSG_DEUDA_NOEXISTE = "La deuda no existe";
	public static final String MSG_DEUDA_USUARIO_NOEXISTE = "El usuario que realiza la operación no tiene esa deuda";	
	public static final String MSG_OP_SALDADO_MAS = "No se puede más del valor de la deuda";
	
	//Recuperar
	public static final String MSG_CODIGO_NOVALIDO = "Código de seguridad o contraseña no válido";
	
	public static final String MSG_OK = "OK";
	public static final String MSG_ERROR = "ERROR";
	
	public static final String MSG_ERROR_NULL = "Campo requerido no existe";
	public static final String MSG_ERROR_NOMBRE = "El nombre debe tener apellido o ser una dirección de correo";
	public static final String MSG_ERROR_CONCEPTO = "El concepto no es válido";
	public static final String MSG_ERROR_CANTIDAD = "La cantidad debe ser positiva";
}
