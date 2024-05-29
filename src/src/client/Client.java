package client;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.ArrayList;
import java.util.Scanner;

import common.InterfazDeServer;
import common.Persona;


public class Client {
	private InterfazDeServer server;
	private Scanner scanner;
	
	public Client() {
		scanner = new Scanner(System.in);
	}
	
	public void startClient() throws RemoteException, NotBoundException {
		Registry registry = LocateRegistry.getRegistry("localhost", 1099);
		server = (InterfazDeServer) registry.lookup("server");
	}
	

    public void agregarPersona() throws RemoteException {
        System.out.println("Ingrese el nombre de la nueva persona:");
        String nombre = scanner.nextLine();
        
        System.out.println("Ingrese la fecha de nacimiento");
        String fecha = scanner.nextLine();
        
        
        System.out.println("Ingrese el rut");
        String rut = scanner.nextLine();
        
        System.out.println("Ingrese el monto");
        int monto = Integer.parseInt(scanner.nextLine());
        
        

        server.agregarPersona(nombre,fecha ,rut, monto);
        System.out.println("Se ha agregado una nueva persona: " + nombre);
    }
    public void getPersonas() throws RemoteException {
        ArrayList<Persona> personas = server.getPersonas();
        for (Persona persona : personas) {
        	System.out.println("Nombre: " + persona.getNombre()+ " RUT: " + persona.getRut()+ " Fecha de nacimiento " + persona.getFechaNacimiento() + " Monto: " + persona.getMonto());
        }

    }
    
    
    
    public void buscarPersonaPorRUT() throws RemoteException {
        System.out.println("Ingrese el RUT de la persona a buscar:");
        String rut = scanner.nextLine();

        Persona persona = server.buscarPersonaPorRUT(rut); // Buscar por RUT

        if (persona != null) {
            System.out.println("Persona encontrada: " + persona.getNombre() + ", Fecha de nacimiento: " + persona.getFechaNacimiento() + ", RUT: " + persona.getRut() + " Monto: "+ persona.getMonto());
        } else {
            System.out.println("Persona con RUT " + rut + " no encontrada.");
        }
    }
    
    
    public void agregarMonto() throws RemoteException {
        System.out.println("Ingrese el RUT del cliente al que se le agregará monto:");
        String rut = scanner.nextLine();

        System.out.println("Ingrese el monto a agregar:");
        double monto = scanner.nextDouble();
        scanner.nextLine(); // Limpiar el buffer

        boolean éxito = server.agregarMonto(rut, monto);

        if (éxito) {
            System.out.println("Monto agregado con éxito al cliente con RUT: " + rut);
        } else {
            System.out.println("No se pudo agregar monto. Asegúrese de que el cliente exista.");
        }
    }

    public void retirarMonto() throws RemoteException {
        System.out.println("Ingrese el RUT del cliente del cual se retirará monto:");
        String rut = scanner.nextLine();

        System.out.println("Ingrese el monto a retirar:");
        double monto = scanner.nextDouble();
        scanner.nextLine(); // Limpiar el buffer

        boolean éxito = server.retirarMonto(rut, monto);

        if (éxito) {
            System.out.println("Monto retirado con éxito del cliente con RUT: " + rut);
        } else {
            System.out.println("No se pudo retirar monto. Asegúrese de que el cliente exista y tenga suficiente saldo.");
        }
    }
    
    public void convertirMontoADolar() {
        // Solicitar el RUT del cliente
        System.out.println("Ingrese el RUT del cliente para convertir a dólares:");
        String rut = scanner.nextLine();

        try {
            // Llamar al método del servidor para convertir el monto
            double montoEnDolares = server.convertirMontoADolar(rut); // Método del servidor

            System.out.println("Monto del cliente en dólares: " + montoEnDolares); // Mostrar el resultado
        } catch (RemoteException e) {
            System.err.println("Error al convertir el monto a dólares: " + e.getMessage());
           
        }
    }
}


