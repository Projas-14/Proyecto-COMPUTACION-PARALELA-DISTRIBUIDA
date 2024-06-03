package common;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;

public interface InterfazDeServer extends Remote {
	//public ArrayList<Persona> getPersonas() throws RemoteException;
	public void Persona(String nombre, String fechaNacimiento, String rut, int monto) throws RemoteException;
	void agregarPersona(String nombre, String fechaNacimiento, String rut, int monto) throws RemoteException;
	Persona buscarPersonaPorRUT(String rut) throws RemoteException;
	boolean agregarMonto(String rut, double monto) throws RemoteException; 
    boolean retirarMonto(String rut, double monto) throws RemoteException; 
    double convertirMontoADolar(String rut) throws RemoteException;
	ArrayList<common.Persona> getPersonas() throws RemoteException;
	
	boolean requestMutex()throws RemoteException;
	void releaseMutex () throws RemoteException;
	boolean eliminarPersonaPorRUT(String rut) throws RemoteException;
}
