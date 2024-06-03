package client;

import java.rmi.RemoteException;
import java.rmi.NotBoundException;
import java.util.Scanner;

public class RunClient {
    public static void main(String[] args) throws RemoteException, NotBoundException {
        Client client = new Client();
        client.startClient();

        Scanner scanner = new Scanner(System.in);
        int opcionPrincipal;

        do {
            System.out.println("\nMenú Principal:");
            System.out.println("1. Operaciones con Personas");
            System.out.println("2. Operaciones con Montos");
            System.out.println("3. Operaciones con API (Convertir Monto de Cliente a Dólar)");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opción: ");
            opcionPrincipal = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcionPrincipal) {
                case 1:
                    menuPersonas(client, scanner);
                    break;
                case 2:
                    menuMontos(client, scanner);
                    break;
                case 3:
                	client.convertirMontoADolar(); // Conversión a dólares
                    break;
       
                	
                    
                    
                case 4:
                	System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcionPrincipal != 4);

        scanner.close();
    }


	private static void menuPersonas(Client client, Scanner scanner) throws RemoteException {
        int opcionPersonas;
        do {
            System.out.println("\nOperaciones con Personas:");
            System.out.println("1. Agregar Persona");
            System.out.println("2. Mostrar Personas");
            System.out.println("3. Buscar Persona por RUT");
            System.out.println("4. Eliminar Persona por RUT");
            System.out.println("5. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcionPersonas = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcionPersonas) {
                case 1:
                    client.agregarPersona(); // Agregar una nueva persona
                    break;
                case 2:
                    client.getPersonas(); // Mostrar todas las personas
                    break;
                case 3:
                    client.buscarPersonaPorRUT(); // Buscar una persona por RUT
                    break;
                case 4:
                	client.eliminarPersonaPorRUT();
                	break;
                case 5:
                    System.out.println("Volviendo al Menú Principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcionPersonas != 4);
    }

    private static void menuMontos(Client client, Scanner scanner) throws RemoteException {
        int opcionMontos;
        do {
            System.out.println("\nOperaciones con Montos:");
            System.out.println("1. Agregar Monto a Cliente");
            System.out.println("2. Retirar Monto de Cliente");
            System.out.println("3. Volver al Menú Principal");
            System.out.print("Seleccione una opción: ");
            opcionMontos = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcionMontos) {
                case 1:
                    client.agregarMonto(); // Agregar monto a un cliente
                    break;
                case 2:
                    client.retirarMonto(); // Retirar monto de un cliente
                    break;
                case 3:
                    System.out.println("Volviendo al Menú Principal...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        } while (opcionMontos != 3);
    }
   
    
}
