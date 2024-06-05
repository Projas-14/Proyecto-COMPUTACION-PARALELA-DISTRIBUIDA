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
	 private boolean conectadoAServerPrincipal = true;
	
	public Client() {
		scanner = new Scanner(System.in);
	}
	
	public void startClient() {
	    try {
	        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
	        server = (InterfazDeServer) registry.lookup("server");
	        conectadoAServerPrincipal = true;
	        System.out.println("Conectado al servidor principal");
	    } catch (RemoteException | NotBoundException e) {
	        System.out.println("No se pudo conectar al servidor principal, intentando con el servidor de respaldo...");
	        cambiarAServerRespaldo();// Llamada al método cambiarAServerRespaldo() si deseas intentar conectarte al servidor de respaldo inmediatamente después de arrojar la excepción.

	        
	    }
	}

	 private void cambiarAServerRespaldo() {
	        try {
	            Registry registry = LocateRegistry.getRegistry("localhost", 1100);
	            server = (InterfazDeServer) registry.lookup("serverRespaldo");
	            conectadoAServerPrincipal = false;
	            System.out.println("Conectado al servidor de respaldo");
	        } catch (RemoteException | NotBoundException e) {
	            System.out.println("No se pudo conectar al servidor de respaldo. Verifique que el servidor de respaldo está en funcionamiento.");
	        }
	    }
	

	 public void agregarPersona() throws RemoteException {
		    System.out.println("\nEnviando solicitud al server...");

		    System.out.println("Ingrese el nombre de la nueva persona:");
		    String nombre = scanner.nextLine();

		    System.out.println("Ingrese la fecha de nacimiento (YYYY-MM-DD):");
		    String fecha = scanner.nextLine();

		    System.out.println("Ingrese el RUT:");
		    String rut = scanner.nextLine();

		    System.out.println("Ingrese el monto:");
		    int monto = Integer.parseInt(scanner.nextLine());

		    try {
		        server.agregarPersona(nombre, fecha, rut, monto);
		        System.out.println("Se ha agregado una nueva persona: " + nombre);
		    } catch (RemoteException e) {
		        String errorMessage = e.getMessage();
		        if (errorMessage != null && errorMessage.contains("ya existe")) {
		            System.out.println("Error: el cliente ya existe" );
		        } else {
		            System.out.println("Error al agregar persona en el servidor principal.");
		            if (conectadoAServerPrincipal) {
		                System.out.println("Intentando con el servidor de respaldo...");
		                cambiarAServerRespaldo(); // Cambio a server de respaldo
		                try {
		                    server.agregarPersona(nombre, fecha, rut, monto); // Intento agregar de nuevo
		                    System.out.println("Se ha agregado una nueva persona en el servidor de respaldo: " + nombre);
		                } catch (RemoteException re) {
		                    System.out.println("Error también en el servidor de respaldo.");
		                    throw re;
		                }
		            } else {
		                throw e;
		            }
		        }
		    }
		}


    
    public void getPersonas() throws RemoteException {
        try {
	        
            ArrayList<Persona> personas = server.getPersonas();
            for (Persona persona : personas) {
                System.out.println("Nombre: " + persona.getNombre() + " RUT: " + persona.getRut() + " Fecha de nacimiento " + persona.getFechaNacimiento() + " Monto: " + persona.getMonto());
            }
           
        } catch (RemoteException e) {
            if (conectadoAServerPrincipal) {
            	System.out.println("\nError Forzado getPersonas");
                System.out.println("Error al obtener personas del servidor principal. Intentando con el servidor de respaldo...");
                cambiarAServerRespaldo();
                ArrayList<Persona> personas = server.getPersonas();
                for (Persona persona : personas) {
                    System.out.println("Nombre: " + persona.getNombre() + " RUT: " + persona.getRut() + " Fecha de nacimiento " + persona.getFechaNacimiento() + " Monto: " + persona.getMonto());
                }
            } else {
                throw e;
            }
        }
    }
    
    
    
    
    public void buscarPersonaPorRUT() throws RemoteException {
        System.out.println("Ingrese el RUT de la persona a buscar:");
        String rut = scanner.nextLine();

        try {
            Persona persona = server.buscarPersonaPorRUT(rut); // Buscar por RUT

            if (persona != null) {
                System.out.println("Persona encontrada: " + persona.getNombre() + ", Fecha de nacimiento: " + persona.getFechaNacimiento() + ", RUT: " + persona.getRut() + " Monto: " + persona.getMonto());
            } else {
                System.out.println("Persona con RUT " + rut + " no encontrada.");
            }
        } catch (RemoteException e) {
            if (conectadoAServerPrincipal) {
                System.out.println("Error al buscar persona en el servidor principal. Intentando con el servidor de respaldo...");
                cambiarAServerRespaldo();
                Persona persona = server.buscarPersonaPorRUT(rut);
                if (persona != null) {
                    System.out.println("Persona encontrada: " + persona.getNombre() + ", Fecha de nacimiento: " + persona.getFechaNacimiento() + ", RUT: " + persona.getRut() + " Monto: " + persona.getMonto());
                } else {
                    System.out.println("Persona con RUT " + rut + " no encontrada.");
                }
            } else {
                throw e;
            }
        }
    }
    
    
    public void eliminarPersonaPorRUT() throws RemoteException {
    	System.out.println("Ingrese el RUT de la persona a Eliminar:");
        String rut = scanner.nextLine();
        
    	try {
            boolean eliminado = server.eliminarPersonaPorRUT(rut);
            if (eliminado) {
                System.out.println("Persona eliminada correctamente.");
            } else {
                System.out.println("No se encontró una persona con el RUT: " + rut);
            }
        } catch (RemoteException e) {
        	if (conectadoAServerPrincipal) {
                System.out.println("Error al agregar monto en el servidor principal. Intentando con el servidor de respaldo...");
                cambiarAServerRespaldo();
                boolean eliminado = server.eliminarPersonaPorRUT(rut);
                if (eliminado) {
                    System.out.println("Persona eliminada correctamente " + rut);
                } else {
                    System.out.println("No se encontró una persona con el RUT:" + rut);
                }
            } else {
                throw e;
            }
        }
    }
    
    public void agregarMonto() throws RemoteException {
        System.out.println("Ingrese el RUT del cliente al que se le agregará monto:");
        String rut = scanner.nextLine();

        System.out.println("Ingrese el monto a agregar:");
        double monto = scanner.nextDouble();
        scanner.nextLine(); // Limpiar el buffer

        try {
            boolean éxito = server.agregarMonto(rut, monto);
            if (éxito) {
                System.out.println("Monto agregado con éxito al cliente con RUT: " + rut);
            } else {
                System.out.println("No se pudo agregar monto. Asegúrese de que el cliente exista.");
            }
        } catch (RemoteException e) {
            if (conectadoAServerPrincipal) {
                System.out.println("Error al agregar monto en el servidor principal. Intentando con el servidor de respaldo...");
                cambiarAServerRespaldo();
                boolean éxito = server.agregarMonto(rut, monto);
                if (éxito) {
                    System.out.println("Monto agregado con éxito al cliente con RUT: " + rut);
                } else {
                    System.out.println("No se pudo agregar monto. Asegúrese de que el cliente exista.");
                }
            } else {
                throw e;
            }
        }
    }

    public void retirarMonto() throws RemoteException {
        System.out.println("Ingrese el RUT del cliente del cual se retirará monto:");
        String rut = scanner.nextLine();

        System.out.println("Ingrese el monto a retirar:");
        double monto = scanner.nextDouble();
        scanner.nextLine(); // Limpiar el buffer

        try {
            boolean éxito = server.retirarMonto(rut, monto);
            if (éxito) {
                System.out.println("Monto retirado con éxito del cliente con RUT: " + rut);
            } else {
                System.out.println("No se pudo retirar monto. Asegúrese de que el cliente exista y tenga suficiente saldo.");
            }
        } catch (RemoteException e) {
            if (conectadoAServerPrincipal) {
                System.out.println("Error al retirar monto en el servidor principal. Intentando con el servidor de respaldo...");
                cambiarAServerRespaldo();
                boolean éxito = server.retirarMonto(rut, monto);
                if (éxito) {
                    System.out.println("Monto retirado con éxito del cliente con RUT: " + rut);
                } else {
                    System.out.println("No se pudo retirar monto. Asegúrese de que el cliente exista y tenga suficiente saldo.");
                }
            } else {
                throw e;
            }
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
            if (conectadoAServerPrincipal) {
                System.out.println("Error al convertir monto a dólares en el servidor principal. Intentando con el servidor de respaldo...");
                cambiarAServerRespaldo();
                try {
                    double montoEnDolares = server.convertirMontoADolar(rut);
                    System.out.println("Monto del cliente en dólares: " + montoEnDolares); // Mostrar el resultado
                } catch (RemoteException ex) {
                    System.err.println("Error al convertir el monto a dólares: " + ex.getMessage());
                }
            } else {
                System.err.println("Error al convertir el monto a dólares: " + e.getMessage());
            }
        }
    }
}

