
import java.rmi.*;
import java.rmi.server.*;

public class CalculatorImpl extends UnicastRemoteObject
        implements RemoteCalculator {

    public CalculatorImpl() throws RemoteException {
        super();
    }

    public int addition(int a, int b) throws RemoteException {
        return a + b;
    }

    public int subtraction(int a, int b) throws RemoteException {
        return a - b;
    }

    public int multiply(int a, int b) throws RemoteException {
        return a * b;
    }
}
