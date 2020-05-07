package com.udc.master.tfm.tracksports.directcom.rest.object;

import java.io.Serializable;

/**
 * Clase que representa la peticion que se realiza para el registro de usuarios
 * @author a.oteroc
 *
 */
public class RegistrationRequestWS implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** Nombre del usuario */
	private String nombre;
	/** Fecha de nacimiento del usuario */
	private String fechaNacimiento;
	/** Peso del usuario */
	private Float peso;
	/** Altura del usuario */
	private Float altura;
	/** Genero del usuario */
	private Short genero;
	/** Paso del usuario */
	private Integer paso;
	
	public RegistrationRequestWS() {}

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
		this.nombre = nombre;
	}

	/**
	 * @return the fechaNacimiento
	 */
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * @param fechaNacimiento the fechaNacimiento to set
	 */
	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	/**
	 * @return the peso
	 */
	public Float getPeso() {
		return peso;
	}

	/**
	 * @param peso the peso to set
	 */
	public void setPeso(Float peso) {
		this.peso = peso;
	}

	/**
	 * @return the altura
	 */
	public Float getAltura() {
		return altura;
	}

	/**
	 * @param altura the altura to set
	 */
	public void setAltura(Float altura) {
		this.altura = altura;
	}

	/**
	 * @return the genero
	 */
	public Short getGenero() {
		return genero;
	}

	/**
	 * @param genero the genero to set
	 */
	public void setGenero(Short genero) {
		this.genero = genero;
	}

	/**
	 * @return the paso
	 */
	public Integer getPaso() {
		return paso;
	}

	/**
	 * @param paso the paso to set
	 */
	public void setPaso(Integer paso) {
		this.paso = paso;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "RegistrationRequestWS [nombre=" + nombre + ", fechaNacimiento="
				+ fechaNacimiento + ", peso=" + peso + ", altura=" + altura
				+ ", genero=" + genero + ", paso=" + paso + "]";
	}
}
