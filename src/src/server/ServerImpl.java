package server;
import common.InterfazDeServer;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;

import common.Persona;

public class ServerImpl extends UnicastRemoteObject implements InterfazDeServer {
    private static final long serialVersionUID = 1L;
    private ArrayList<Persona> bdPersonas = new ArrayList<>();
    private Connection connection;
    private boolean inUse;

    public ServerImpl() throws RemoteException {
        super();
        conectarBD();
        cargarPersonasDeBD();
    }

    private void conectarBD() {
        try {
            String url = "jdbc:mysql://localhost:3306/bd_tarea"; // 
            String username = "root"; // 
            String password_BD = ""; // 
            connection = DriverManager.getConnection(url, username, password_BD);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("No se pudo conectar a la base de datos.");
        }
    }

    private void cargarPersonasDeBD() throws RemoteException {

        if (connection == null) {
            conectarBD();
        }
        try (Statement stmt = connection.createStatement()) {
            String sql = "SELECT * FROM cliente"; // 
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String fechaNacimiento = rs.getString("fechaNacimiento");
                String rut = rs.getString("rut");
                int monto = rs.getInt("monto");

                Persona persona = new Persona(nombre, fechaNacimiento, rut, monto);
                bdPersonas.add(persona);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al cargar personas desde la base de datos.");
        }
    }

    @Override
    public ArrayList<Persona> getPersonas() throws RemoteException {
    	while(true) {
    		if(requestMutex()) {
    			System.out.println("Tengo permiso para iniciar la sección critica");
    			break;
    		}
    		try {
    			Thread.sleep(2000);    		
    		} catch(InterruptedException e) {
    				e.printStackTrace();
    		}
    		System.out.println("Aún no tengo permiso...");
    	}
    	
        ArrayList<Persona> listaPersonas = new ArrayList<>();

        if (connection == null) {
            conectarBD();
        }

        String sql = "SELECT * FROM cliente"; // Obtenemos todas los clientes desde la base de datos
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                String nombre = rs.getString("nombre");
                String fechaNacimiento = rs.getString("fechaNacimiento");
                String rut = rs.getString("rut");
                int monto = rs.getInt("monto"); // Obtiene el monto actual

                Persona persona = new Persona(nombre, fechaNacimiento, rut, monto);
                listaPersonas.add(persona); // Añade a la lista
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al obtener la lista de personas.");
        }
        int duracionSleep = 8000;
        System.out.println("Iniciando busqueda. Tiempo estimado: " + duracionSleep);
        
        try {
        	Thread.sleep(duracionSleep);
        } catch(InterruptedException e) {
        	e.printStackTrace();
        }
        releaseMutex();
        
        
        System.out.println("Busqueda exitosa");
        
        
        return listaPersonas; // Devuelve la lista actualizada
    }

    @Override
    public void agregarPersona(String nombre, String fechaNacimiento, String rut, int monto) throws RemoteException {
    	while(true) {
    		if(requestMutex()) {
    			System.out.println("Tengo permiso para iniciar la sección critica");
    			break;
    		}
    		try {
    			Thread.sleep(2000);    		
    		} catch(InterruptedException e) {
    				e.printStackTrace();
    		}
    		System.out.println("Aún no tengo permiso...");
    	}
    	
    	
        if (connection == null) {
            conectarBD();
        }

        Persona newPersona = new Persona(nombre, fechaNacimiento, rut, monto);
        bdPersonas.add(newPersona);

        String sql = "INSERT INTO cliente (nombre, fechaNacimiento, rut, monto) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, nombre);
            pstmt.setString(2, fechaNacimiento);
            pstmt.setString(3, rut);
            pstmt.setInt(4, monto);

            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                //System.out.println("Se agregó la persona a la base de datos.");
            } else {
                System.out.println("No se pudo agregar la persona a la base de datos.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al agregar persona a la base de datos.");
        }
        int duracionSleep = 8000;
        System.out.println("Iniciando inserción. Tiempo estimado: " + duracionSleep);
        
        try {
        	Thread.sleep(duracionSleep);
        } catch(InterruptedException e) {
        	e.printStackTrace();
        }
        releaseMutex();
        
        
        System.out.println("Insert exitoso, agregado a la base de datos: " + nombre);
    }
    
    
    @Override
    public Persona buscarPersonaPorRUT(String rut) throws RemoteException {
    	while(true) {
    		if(requestMutex()) {
    			System.out.println("Tengo permiso para iniciar la sección critica");
    			break;
    		}
    		try {
    			Thread.sleep(2000);    		
    		} catch(InterruptedException e) {
    				e.printStackTrace();
    		}
    		System.out.println("Aún no tengo permiso...");
    	}
    	
        if (connection == null) {
            conectarBD();
        }

        Persona personaEncontrada = null;

        String sql = "SELECT * FROM cliente WHERE rut = ?"; // 
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, rut);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) { // Si hay un resultado
                String nombre = rs.getString("nombre");
                String fechaNacimiento = rs.getString("fechaNacimiento");
                int monto = rs.getInt("monto");

                personaEncontrada = new Persona(nombre, fechaNacimiento, rut, monto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error al buscar persona por RUT.");
            
        }
        int duracionSleep = 8000;
        System.out.println("Iniciando inserción. Tiempo estimado: " + duracionSleep);
        
        try {
        	Thread.sleep(duracionSleep);
        } catch(InterruptedException e) {
        	e.printStackTrace();
        }
        releaseMutex();
        
        
        System.out.println("Busqueda de persona realizada con éxito");

        return personaEncontrada; // Devuelve la persona encontrada o null si no hay resultado
    }

    
    @Override
    public boolean agregarMonto(String rut, double monto) throws RemoteException {
        // Solicitar acceso a la sección crítica
        while (true) {
            if (requestMutex()) {
                System.out.println("Tengo permiso para iniciar la sección crítica");
                break;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Aún no tengo permiso...");
        }

        if (connection == null) {
            conectarBD();
        }

        String sql = "UPDATE cliente SET monto = monto + ? WHERE rut = ?";
        boolean result = false;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, monto);
            pstmt.setString(2, rut);

            int rowsAffected = pstmt.executeUpdate();
            result = rowsAffected > 0; // Verificar si la operación fue exitosa
        } catch (SQLException e) {
            e.printStackTrace();
            result = false; // Devuelve false si hubo un error
        }

        int duracionSleep = 8000;
        System.out.println("Iniciando actualización. Tiempo estimado: " + duracionSleep);
        
        try {
            Thread.sleep(duracionSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        releaseMutex();
        System.out.println("Actualización de monto completada con éxito");

        return result;
    }


    @Override
    public boolean retirarMonto(String rut, double monto) throws RemoteException {
        // Solicitar acceso a la sección crítica
        while (true) {
            if (requestMutex()) {
                System.out.println("Tengo permiso para iniciar la sección crítica");
                break;
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Aún no tengo permiso...");
        }

        if (connection == null) {
            conectarBD();
        }

        String selectSql = "SELECT monto FROM cliente WHERE rut = ?";
        double montoActual = 0;
        boolean result = false;

        try (PreparedStatement pstmt = connection.prepareStatement(selectSql)) {
            pstmt.setString(1, rut);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                montoActual = rs.getDouble("monto");
            }

            // Verificar si el retiro es posible
            if (montoActual >= monto) {
                String updateSql = "UPDATE cliente SET monto = monto - ? WHERE rut = ?";
                try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
                    updateStmt.setDouble(1, monto);
                    updateStmt.setString(2, rut);

                    int rowsAffected = updateStmt.executeUpdate();
                    result = rowsAffected > 0; // Retorna true si el retiro fue exitoso
                }
            } else {
                System.out.println("El cliente no tiene suficiente saldo para retirar.");
                result = false; // Retorno false si no hay suficiente saldo
            }
        } catch (SQLException e) {
            e.printStackTrace();
            result = false; // Devuelve false si hubo un error
        }

        int duracionSleep = 8000;
        System.out.println("Iniciando retiro. Tiempo estimado: " + duracionSleep);

        try {
            Thread.sleep(duracionSleep);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        releaseMutex();
        System.out.println("Retiro de monto completado con éxito");

        return result;
    }

    
    public double convertirMontoADolar(String rut) throws RemoteException {
    	/*while(true) {
    		if(requestMutex()) {
    			System.out.println("Tengo permiso para iniciar la sección critica");
    			break;
    		}
    		try {
    			Thread.sleep(2000);    		
    		} catch(InterruptedException e) {
    				e.printStackTrace();
    		}
    		System.out.println("Aún no tengo permiso...");
    	}*/
    	
        // Obtener la tasa de cambio actual
        ApiExterna apiExterna = new ApiExterna();
        double clpToUsd = apiExterna.obtenerTasaCambioCLPaUSD(); // Obtener CLP a USD

        if (clpToUsd < 0) { // Si hubo un error al obtener la tasa
            throw new RemoteException("No se pudo obtener la tasa de cambio.");
        }

        // Buscar la persona por RUT para obtener su monto
        Persona persona = buscarPersonaPorRUT(rut);

        if (persona == null) { // Si no se encontró la persona
            throw new RemoteException("No se encontró un cliente con el RUT: " + rut);
        }

        // Obtener el monto del cliente en CLP y convertirlo a USD
        double montoEnClp = persona.getMonto();
        double montoEnUsd = montoEnClp * clpToUsd; // Convertir el monto a USD
        
        int duracionSleep = 8000;
        System.out.println("Iniciando transformación. Tiempo estimado: " + duracionSleep);
        
       /* try {
        	Thread.sleep(duracionSleep);
        } catch(InterruptedException e) {
        	e.printStackTrace();
        }
        releaseMutex();
        
        
        System.out.println("Transformación de monto realizada con exito");*/


        return montoEnUsd; // Devuelve el monto convertido
    }

	@Override
	public void Persona(String nombre, String fechaNacimiento, String rut, int monto) throws RemoteException {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public synchronized boolean requestMutex() throws RemoteException{
		if (inUse) {
			return false; //esta ocupado, no se puede dar acceso
		}else
		{
			inUse = true;
			return true;
		}
	
	}
	
	public void releaseMutex() throws RemoteException{
		inUse = false;
	}
	
	@Override
	public boolean eliminarPersonaPorRUT(String rut) throws RemoteException {

	    while (true) {
	        if (requestMutex()) {
	            System.out.println("Tengo permiso para iniciar la sección crítica");
	            break;
	        }
	        try {
	            Thread.sleep(2000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        System.out.println("Aún no tengo permiso...");
	    }

	    if (connection == null) {
	        conectarBD();
	    }
	    
	    String sql = "DELETE FROM cliente WHERE rut = ?";
	    boolean result = false;
	    
	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        pstmt.setString(1, rut);

	        int rowsAffected = pstmt.executeUpdate();
	        if (rowsAffected > 0) {
	            bdPersonas.removeIf(persona -> persona.getRut().equals(rut));
	            System.out.println("Cliente eliminado correctamente.");
	            result = true;
	        } else {
	            System.out.println("No se encontró el cliente con el RUT especificado.");
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	        System.out.println("Error al eliminar el cliente.");
	    }

	    int duracionSleep = 8000;
	    System.out.println("Iniciando eliminación. Tiempo estimado: " + duracionSleep);
	    
	    try {
	        Thread.sleep(duracionSleep);
	    } catch(InterruptedException e) {
	        e.printStackTrace();
	    }

	    releaseMutex();
	    
	    return result;
	}

}
