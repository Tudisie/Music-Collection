package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IMathOperations extends Remote{
    int add(int a, int b) throws RemoteException;
    int sub(int a, int b) throws RemoteException;
    int mul(int a, int b) throws RemoteException;
    int div(int a, int b) throws RemoteException;
}