package common;

import java.io.Serializable;

public class Persona implements Serializable{
	private static final long serialVersionUID = 1L;
	private String nombre;
	private String fechaNacimiento;
	private String rut;
	private int monto;
	
	public Persona(String nombre ,String fechaNacimiento,String rut, int monto) {
		this.nombre = nombre;
		this.fechaNacimiento = 	fechaNacimiento;
		this.rut = 	rut;
		this.monto = monto;
		
	}
	
	public String getNombre() {
		return nombre;
	}
	
	public String getFechaNacimiento() {
		return fechaNacimiento;
	}
	
	public String getRut() {
		return rut;
	}
	public int getMonto() {
		return monto;
	}
	public void setMonto(int monto) { 
        this.monto = monto;
    }
	
}
