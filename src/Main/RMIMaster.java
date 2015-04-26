package Main;

import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.UnmarshalException;


/**
 * Created by raghavbabu on 4/25/15.
 */
public class RMIMaster {

    private Service connection = null;
    private static RMIMaster instance = null;
    private String client;

    public static void main(String[] args)
    {
        if (args.length == 1)
        {
            RMIMaster us = RMIMaster.getInstance();
            us.getServiceFromSlave("127.0.0.1");
        }
        else
        {
            System.err.println();
        }
    }

    private RMIMaster()
    {
    }


    private void getServiceFromSlave(String host)
    {
        try
        {
            connection = connectToServer(host);

            int[] arr = new int[]{5,2,3,4,1};
            int[] sortedNumbers = connection.sort(arr);

            for(int i = 0; i < arr.length; i++)
            System.out.println(sortedNumbers[i]);
        }

        catch (Exception ex)
        {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * Gets master in the singleton pattern.
     *
     */
    public static RMIMaster getInstance()
    {
        if (instance == null)
        {
            instance = new RMIMaster();
        }

        return instance;
    }

    /**
     * Attempt a connection to the server.
     *
     * @param connectionHost is IP address.
     */
    private Service connectToServer(String connectionHost) throws Exception
    {
        try
        {

            if (System.getSecurityManager() == null)
            {
                System.out.println("Creating new security manager");
                System.setSecurityManager(new SecurityManager());
            }

            client = InetAddress.getLocalHost().getHostName();
            System.out.println("Client is on " + client + " with Java version " + System.getProperty("java.version"));


            connection = (Service) Naming.lookup("rmi://"
                    + connectionHost
                    + ":" + Service.RMIRegistryPort + "/" + Service.SORT_SERVICE);

            System.out.println("RMIMaster:connectToServer - Connected to " + connectionHost + ":" + Service.RMIRegistryPort + "/" + Service.SORT_SERVICE);
        }
        catch (UnmarshalException ue)
        {
            System.err.println("RMIMaster:connectToServer() - UnmarshalException - Check that the server can access it's configuration / policy files");
            ue.printStackTrace(System.err);
            throw ue;
        }
        catch (Exception e)
        {
            e.printStackTrace(System.err);
            throw e;
        }
        return connection;
    }

}
