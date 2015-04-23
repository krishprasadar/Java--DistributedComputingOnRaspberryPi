/*
 * Pi RMI.
 * © G J Barnard 2013 - Attribution-NonCommercial-ShareAlike 3.0 Unported - http://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB.
 */
package PiRMI;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;

/**
 * This is the client that connects to the server and service within using the
 * details supplied.
 *
 * It does this by contacting the RMI Registry on the port it is running on
 * with the name of the service it wishes to use. Therefore three items are
 * required to be known beforehand: server name addressable name / IP address,
 * RMI Registry port on the server and the name of the service.
 *
 * @author G J Barnard
 */
public class PiInfoClient
{

    private PiInfo connection = null;
    private static PiInfoClient instance = null;
    private String client;

    public static void main(String[] args)
    {
        if (args.length == 1)
        {
            PiInfoClient us = PiInfoClient.getInstance();
            us.reportPiInfo(args[0]);
        }
        else
        {
            System.err.println("Usage: java -Djava.security.policy=\"./pirmi/Policy\" PiRMI.PiInfoClient server name (or server IP)");
        }
    }

    private PiInfoClient()
    {
    }

    /**
     *
     */
    private void reportPiInfo(String host)
    {
        try
        {
            connection = connectToServer(host);

            String serverName = connection.getHostName(client);
            System.out.println(serverName + "'s Java version is: " + connection.getJavaVersion(client));

            PiTriInfo osInfo = connection.getOSDetails(client);
            System.out.println(serverName + "'s OS details are:");
            displayPiTriInfo(osInfo);

            System.out.println(serverName + "'s data model is: " + connection.getDataModel(client));

            System.out.println(serverName + "'s endian is: " + connection.getEndian(client));

            PiTriInfo userInfo = connection.getUserDetails(client);
            System.out.println(serverName + "'s User details are:");
            displayPiTriInfo(userInfo);
        }
        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
        }
    }

    private void displayPiTriInfo(PiTriInfo info)
    {
        PiPairInfo[] pair = new PiPairInfo[3];

        pair[0] = info.getOne();
        pair[1] = info.getTwo();
        pair[2] = info.getThree();

        for (byte i = 0; i <= 2; i++)
        {
            System.out.println("    " + pair[i].getKey() + " is: " + pair[i].getValue());
        }
    }

    /**
     * Gets us in the singleton pattern.
     *
     * @return Us
     */
    public static PiInfoClient getInstance()
    {
        if (instance == null)
        {
            instance = new PiInfoClient();
        }

        return instance;
    }

    /**
     * Attempt a connection to the server.
     *
     * @param connectionHost server addressable name or IP address.
     * @return The connection.
     * @throws UnknownHostException
     * @throws NotBoundException
     * @throws MalformedURLException
     * @throws RemoteException
     * @throws UnmarshalException
     * @throws ClassNotFoundException
     * @throws java.rmi.ConnectException
     * @throws java.security.AccessControlException
     */
    private PiInfo connectToServer(String connectionHost) throws UnknownHostException, NotBoundException, MalformedURLException, RemoteException, UnmarshalException, ClassNotFoundException, java.rmi.ConnectException, java.security.AccessControlException
    {
        try
        {
            // Create and install a security manager.
            if (System.getSecurityManager() == null)
            {
                System.out.println("Creating new security manager");
                System.setSecurityManager(new SecurityManager());
            }

            // Info.
            client = InetAddress.getLocalHost().getHostName();
            System.out.println("Client is on " + client + " with Java version " + System.getProperty("java.version"));

            /*
             * This does the actual connection returning a reference to the
             * server service if it suceeds.
             */
            connection = (PiInfo) Naming.lookup("rmi://"
                    + connectionHost
                    + ":" + PiInfo.RMIRegistryPort + "/" + PiInfo.PIINFO_SERVICE);

            System.out.println("PiInfoClient:connectToServer - Connected to " + connectionHost + ":" + PiInfo.RMIRegistryPort + "/" + PiInfo.PIINFO_SERVICE);
        }
        catch (UnmarshalException ue)
        {
            System.err.println("PiInfoClient:connectToServer() - UnmarshalException - Check that the server can access it's configuration / policy files");
            ue.printStackTrace(System.err);
            throw ue;
        }
        return connection;
    }
}