/*
 * Pi RMI.
 * © G J Barnard 2013 - Attribution-NonCommercial-ShareAlike 3.0 Unported - http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB.
 */
package PiRMI;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

/**
 * This is the class that implements the service served by the server for the
 * client.  It extends UnicastRemoteObject -
 * http://docs.oracle.com/javase/7/docs/api/java/rmi/server/UnicastRemoteObject.html
 * - so that will serve RMI requests and therefore requires to be processed by
 * the 'rmic' tool to create the 'stub'used by the client as the reference to
 * the server - see:
 * http://docs.oracle.com/javase/7/docs/technotes/tools/windows/rmic.html.
 * 
 * @author G J Barnard
 */
public class PiInfoService extends UnicastRemoteObject implements PiInfo
{

    public PiInfoService(int port) throws RemoteException
    {
        super(port);
    }

    /*
     * Methods to get the following:
     * java.version
     * os.arch
     * os.name
     * os.version
     * sun.arch.data.model
     * sun.cpu.endian
     * user.name
     * user.home
     * user.dir
     */ 
    
    @Override
    public PiTriInfo getOSDetails(String client) throws RemoteException
    {
        System.out.println("PiInfoService:getOSDetails("+ client +") called.");
        PiTriInfo info = new PiTriInfo(
                new PiPairInfo("os.name", System.getProperty("os.name")),
                new PiPairInfo("os.arch", System.getProperty("os.arch")),
                new PiPairInfo("os.version", System.getProperty("os.version")));

        return info;
    }

    @Override
    public String getDataModel(String client) throws RemoteException
    {
        System.out.println("PiInfoService:getDataModel("+ client +") called.");
        return System.getProperty("sun.arch.data.model");
    }

    @Override
    public String getJavaVersion(String client) throws RemoteException
    {
        System.out.println("PiInfoService:getJavaVersion("+ client +") called.");
        return System.getProperty("java.version");
    }

    @Override
    public String getEndian(String client) throws RemoteException
    {
        return System.getProperty("sun.cpu.endian");
    }

    @Override
    public PiTriInfo getUserDetails(String client) throws RemoteException
    {
        System.out.println("PiInfoService:getUserDetails("+ client +") called.");
        PiTriInfo info = new PiTriInfo(
                new PiPairInfo("user.name", System.getProperty("user.name")),
                new PiPairInfo("user.home", System.getProperty("user.home")),
                new PiPairInfo("user.dir", System.getProperty("user.dir")));

        return info;
    }

    @Override
    public String getHostName(String client) throws RemoteException
    {
        System.out.println("PiInfoService:getHostName("+ client +") called.");
        try
        {
            return InetAddress.getLocalHost().getHostName();
        }
        catch (UnknownHostException ex)
        {
            return "Unknown";
        }
    }
}