/**
 * 
 */
package es.jma.prestamos.validadores;

import static es.jma.prestamos.utils.UtilValidador.*;
import es.jma.prestamos.constantes.KMensajes;

/**
 * Validador para el formulario de nueva deuda
 * @author jmiranda
 *
 */
public class ValidarNuevaDeuda extends AbstractValidador{
	//Atributos
	private String nombre;
	private String concepto;
	private double cantidad;
	
	//Constructores
	public ValidarNuevaDeuda(String nombre, String concepto, double cantidad)
	{
		this.nombre = nombre;
		this.concepto = concepto;
		this.cantidad = cantidad;
	}

	@Override
	public boolean validar() {
		boolean resultado = false;
		//El nombre puede ser también correo
		boolean esNombreoEmail = esEmail(nombre) || esNombreyApellidos(nombre);
		boolean conceptoValido = esTexto(concepto);
		boolean cantidadValida = esPositivo(cantidad);
		
		resultado = esNombreoEmail && conceptoValido && cantidadValida;
		
		if (!esNombreoEmail)
		{
			msgError = KMensajes.MSG_ERROR_NOMBRE;
		}
		else if (!conceptoValido)
		{
			msgError = KMensajes.MSG_ERROR_CONCEPTO;
		}
		else if (!cantidadValida)
		{
			msgError = KMensajes.MSG_ERROR_CANTIDAD;
		}
		
		return resultado;
	}
}
