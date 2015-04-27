package slaveNode;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * Created by raghavbabu on 4/25/15.
 */
public class ServiceImpl extends UnicastRemoteObject implements Service {

    public ServiceImpl(int port) throws RemoteException {
        super(port);
    }

    @Override
    public int[] sort(int[] numbers) throws RemoteException {

        for(int i = 0; i < numbers.length ;i++){
            for(int j = i+1 ; j < numbers.length; j++){

                if(numbers[i] > numbers[j]){
                    int tmp = numbers[i];
                    numbers[i] = numbers[j];
                    numbers[j] = tmp;
                }
            }
        }
        return numbers;
    }

    @Override
    public int average(int[] numbers) throws RemoteException {
        return 0;
    }

    @Override
    public String getSlaveIPAddress() throws RemoteException {
        return null;
    }


}
