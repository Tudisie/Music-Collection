package servants;

import interfaces.IMathOperations;
import interfaces.ISayHello;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class ServantRunner {
    private static final String rmiServantName = "RMIHelloWorld";

    public static void main(String[] args){
        //if(null == System.getSecurityManager()){
        //    System.setSecurityManager(new SecurityManager());
        //}

        try{
            IMathOperations remoteWorker = new MathOperationsImpl();

            IMathOperations servantStub = (IMathOperations) UnicastRemoteObject.exportObject(remoteWorker, 0);
            Registry targetRegistry = LocateRegistry.getRegistry();
            targetRegistry.rebind(ServantRunner.rmiServantName, servantStub);

            System.out.println("Greeter awaiting messages");
        } catch(Exception ex){
            System.err.println("Should not be generic exception unless last resort");
            ex.printStackTrace();
        }
    }
}
