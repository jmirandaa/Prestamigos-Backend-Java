/**
 * 
 */
package es.jma.prestamos.dominio;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * POJO para guardar los datos globales
 * de una deuda
 * @author jmiranda
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResumenDeuda {
	//Atributos
	private double total;
	private double todalDebo;
	private double totalMeDeben;
	private int numDeudasDebo;
	private int numDeudasMeDeben;
	
	//Constructores
	public ResumenDeuda()
	{
		
	}

	/**
	 * @return the total
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * @param total the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 * @return the todalDebo
	 */
	public double getTodalDebo() {
		return todalDebo;
	}

	/**
	 * @param todalDebo the todalDebo to set
	 */
	public void setTodalDebo(double todalDebo) {
		this.todalDebo = todalDebo;
	}

	/**
	 * @return the totalMeDeben
	 */
	public double getTotalMeDeben() {
		return totalMeDeben;
	}

	/**
	 * @param totalMeDeben the totalMeDeben to set
	 */
	public void setTotalMeDeben(double totalMeDeben) {
		this.totalMeDeben = totalMeDeben;
	}

	/**
	 * @return the numDeudasDebo
	 */
	public int getNumDeudasDebo() {
		return numDeudasDebo;
	}

	/**
	 * @param numDeudasDebo the numDeudasDebo to set
	 */
	public void setNumDeudasDebo(int numDeudasDebo) {
		this.numDeudasDebo = numDeudasDebo;
	}

	/**
	 * @return the numDeudasMeDeben
	 */
	public int getNumDeudasMeDeben() {
		return numDeudasMeDeben;
	}

	/**
	 * @param numDeudasMeDeben the numDeudasMeDeben to set
	 */
	public void setNumDeudasMeDeben(int numDeudasMeDeben) {
		this.numDeudasMeDeben = numDeudasMeDeben;
	}
	
	
}
