package clients;

import interfaces.IMathOperations;
import interfaces.ISayHello;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

public class RMIClient {
    private static final String rmiHost = "localhost";
    private static final int rmiPort = 1099;
    private static final String servantObjectRegistryName = "RMIHelloWorld";

    public static void main(String[] args){
        try{
            Registry remoteRegistry = LocateRegistry.getRegistry(
                    RMIClient.rmiHost, RMIClient.rmiPort);
            IMathOperations rmiClientStub = (IMathOperations)
                    remoteRegistry.lookup(RMIClient.servantObjectRegistryName);
            System.out.println("Input values, end with \"quit\"");
            String consoleInput;
            Scanner consoleScanner = new Scanner(System.in);
            consoleInput = consoleScanner.nextLine();
            while(0 != consoleInput.compareToIgnoreCase("quit")){
                int a = 0, b = 0;
                String operator = "";
                String[] numbers;

                if(consoleInput.contains("+")) {
                    operator = "+";
                    numbers = consoleInput.split("\\+");
                    a = Integer.parseInt(numbers[0]);
                    b = Integer.parseInt(numbers[1]);
                }
                else if(consoleInput.contains("-")) {
                    operator = "-";
                    numbers = consoleInput.split("\\-");
                    a = Integer.parseInt(numbers[0]);
                    b = Integer.parseInt(numbers[1]);
                }
                else if(consoleInput.contains("*")) {
                    operator = "*";
                    numbers = consoleInput.split("\\*");
                    a = Integer.parseInt(numbers[0]);
                    b = Integer.parseInt(numbers[1]);
                }
                else if(consoleInput.contains("/")) {
                    operator = "/";
                    numbers = consoleInput.split("/");
                    a = Integer.parseInt(numbers[0]);
                    b = Integer.parseInt(numbers[1]);
                }
                int result = 0;
                if(operator.equals("+"))
                    result = rmiClientStub.add(a,b);
                else if(operator.equals("-"))
                    result = rmiClientStub.sub(a,b);
                else if(operator.equals("*"))
                    result = rmiClientStub.mul(a,b);
                else if(operator.equals("/"))
                    result = rmiClientStub.div(a,b);
                System.out.println("Received from server: " + result);
                consoleInput = consoleScanner.nextLine();
            }
        } catch (Exception ex) {
            System.err.println("Should not be generic exception unless last resort");
            ex.printStackTrace();
        }
    }
}
