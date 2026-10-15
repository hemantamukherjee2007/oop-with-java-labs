import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteCalculator extends Remote {

    int addition(int a, int b) throws RemoteException;

    int subtraction(int a, int b) throws RemoteException;

    int multiply(int a, int b) throws RemoteException;
}
