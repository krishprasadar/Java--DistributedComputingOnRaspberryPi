package sharedResources;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Created by   Rathinakumar Visweswaran,
 *              Krishna
 *              Raghav Babu
 *              on 4/22/2015.
 */
public interface Service extends Remote {

    public static final String SORT_SERVICE = "SortService";
    public static final String AVG_SERVICE = "AverageService";

    public static final int RMIRegistryPort = 2024;
    public static final int ServicePort = 2025;

    public int[] sort(int[] numbers) throws RemoteException;
    public int average(int[] numbers) throws RemoteException;
}
