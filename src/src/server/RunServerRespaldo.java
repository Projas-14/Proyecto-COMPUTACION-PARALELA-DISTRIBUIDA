package server;

import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import common.InterfazDeServer;

public class RunServerRespaldo {
    public static void main(String[] args) throws RemoteException, AlreadyBoundException {
        InterfazDeServer server = new ServerImpl();
        
        Registry registry = LocateRegistry.createRegistry(1100);
        registry.bind("serverRespaldo", server);
        System.out.println("Servidor de respaldo listo para trabajar");
    }
}
