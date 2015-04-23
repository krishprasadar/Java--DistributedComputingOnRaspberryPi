/*
 * Pi RMI.
 * © G J Barnard 2013 - Attribution-NonCommercial-ShareAlike 3.0 Unported - http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB.
 */
package PiRMI;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * This interface defines the methods you want your server to provide as a
 * service to the client.
 * 
 * The client will use it as it's RMI connection 'class' and the server
 * will implement it.
 * 
 * @author G J Barnard
 */
public interface PiInfo extends Remote
{

    public static final String PIINFO_SERVICE = "PiInfoServer";
    public static final int RMIRegistryPort = 2024;
    public static final int ServicePort = 2025;

    public String getHostName(String client) throws RemoteException;
    
    public String getJavaVersion(String client) throws RemoteException;
    
    public PiTriInfo getOSDetails(String client) throws RemoteException;
    
    public String getDataModel(String client) throws RemoteException;
    
    public String getEndian(String client) throws RemoteException;
    
    public PiTriInfo getUserDetails(String client) throws RemoteException;
}