package servants;

import interfaces.IMathOperations;

import java.rmi.RemoteException;

public class MathOperationsImpl implements IMathOperations {
    public MathOperationsImpl(){
        super();
    }

    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }

    @Override
    public int sub(int a, int b) throws RemoteException {
        return a - b;
    }

    @Override
    public int mul(int a, int b) throws RemoteException {
        return a * b;
    }

    @Override
    public int div(int a, int b) throws RemoteException {
        if(b == 0)
        {
            throw new RemoteException("Can't divide by 0!");
        }
        else
        {
            return a/b;
        }
    }
}