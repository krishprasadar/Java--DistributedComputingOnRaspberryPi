/*
 * PiRMI.
 * Copyright (C) 2015 Gareth J Barnard
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Also see: http://www.gnu.org/copyleft/gpl.html GNU GPL v3 or later
 */
package pirmi;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.AccessControlException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * This is the client that connects to the server and service within using the
 * details supplied.
 *
 * It does this by contacting the RMI Registry on the port it is running on with
 * the name of the service it wishes to use.  Therefore three items are required
 * to be known beforehand: server name addressable name / IP address, RMI
 * Registry port on the server and the name of the service.
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
            System.err.println("Usage: java -Djava.security.policy=\"./Policy\" -jar Client..jar server name (or server IP)");
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
            int[] numbers = {1,6,7,3,2};
            numbers = connection.sort(numbers);
            for(int i= 0; i < numbers.length; i++)
            {
                System.out.println(numbers[i]);    
            }            
            System.out.println("Connection Successful!");          
        }
        catch (UnknownHostException | NotBoundException | MalformedURLException | RemoteException | ClassNotFoundException | AccessControlException ex)
        {
            ex.printStackTrace(System.err);
        }
    }

    private void displayPiPairInfoList(LinkedList<PiPairInfo> info)
    {
        Iterator<PiPairInfo> it = info.iterator();
        PiPairInfo current;

        while (it.hasNext())
        {
            current = it.next();
            System.out.println("    " + current.getKey() + " is: " + current.getValue());
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
     * @throws AccessControlException
     */
    private PiInfo connectToServer(String connectionHost) throws UnknownHostException, NotBoundException, MalformedURLException, RemoteException, UnmarshalException, ClassNotFoundException, java.rmi.ConnectException, AccessControlException
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

            Registry registry = LocateRegistry.getRegistry(connectionHost, PiInfo.RMIRegistryPort);
            
            /*
             * This does the actual connection returning a reference to the
             * server service if it suceeds.
             */
            connection = (PiInfo) registry.lookup(PiInfo.SORT_SERVICE);
            
            System.out.println("PiInfoClient:connectToServer - Connected to " + connectionHost + ":" + PiInfo.RMIRegistryPort + "/" + PiInfo.SORT_SERVICE);
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
