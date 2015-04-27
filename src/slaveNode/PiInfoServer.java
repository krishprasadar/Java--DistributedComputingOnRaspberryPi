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
package slaveNode;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import pirmi.PiInfo;

/**
 * This is the server that creates / gets a registry for the client to ask
 * where the service we will serve is.  Some other examples have you running
 * the registry on the command line, but this demonstrates a way of doing so
 * within code, saving time.
 * 
 * The registry needs to operate on one port and the service another.  This is
 * so that one registry can inform clients of lots of services running on their
 * own ports.  I have chosen to specify the port of the service, but you could
 * let the virtual machine to do so.  This is helpful if you are using a
 * network packet sniffer like WireShark as you can then filter the capture by
 * port.
 * 
 * You could have the server implement the service too, but I have decided to
 * separate it out for clarity.
 * 
 * @author G J Barnard
 */
public class PiInfoServer extends Thread
{
    private static PiInfoServer instance = null;
    private PiInfoService piInfoServer = null;

    /**
     * Constructor.
     *
     * @throws RemoteException
     */
    private PiInfoServer() throws RemoteException
    {
        super();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
        PiInfoServer us = PiInfoServer.getInstance();
        us.createServer();
    }

    /**
     * Create the server.
     */
    public void createServer()
    {
        // Create and install a security manager
        if (System.getSecurityManager() == null)
        {
            System.out.println("Creating new security manager");

            // http://download.oracle.com/javase/6/docs/api/java/rmi/RMISecurityManager.html
            // states to use SecurityManager instead of RMISecurityManager.
            System.setSecurityManager(new SecurityManager());
        }

        try
        {
            // Create the service with the specified port.
            piInfoServer = new PiInfoService(PiInfo.ServicePort);

            // http://stackoverflow.com/questions/2142668/java-rmi-automating-in-an-ide
            Registry r;
            try
            {
                r = LocateRegistry.createRegistry(PiInfo.RMIRegistryPort);
            }
            catch (java.rmi.server.ExportException ex)
            {
                r = LocateRegistry.getRegistry(PiInfo.RMIRegistryPort);
            }

            System.out.println("Service attempts rebind of registry");
            r.rebind(PiInfo.SORT_SERVICE, piInfoServer);
            System.out.println("Server bound in registry");
            System.out.println("Press Ctrl-C to exit.");
        }
        catch (java.rmi.server.ExportException ee)
        {
            // Cannot get RMI port.
            System.err.println("Server  cannot use RMI port " + PiInfo.RMIRegistryPort + " as it is already in use, check that you are not running another instance of the server.");
            ee.printStackTrace(System.err);
            System.exit(-1);
        }
        catch (Exception e)
        {
            System.err.println("Server err: " + e.getMessage());
            System.exit(-1);
        }
    }

    /**
     * Executed at shutdown in response to a Ctrl-C etc.
     */
    @Override
    public void run()
    {
        // Perform shutdown methods.
        System.out.println("Server Shutting Down.");
    }

    /**
     * Gets us in the singleton pattern.
     *
     * @return Us
     */
    public static PiInfoServer getInstance()
    {
        if (instance == null)
        {
            try
            {
                instance = new PiInfoServer();
                // Prepare for shutdown...
                Runtime.getRuntime().addShutdownHook(instance);
            }
            catch (RemoteException ex)
            {
                System.err.println("PiInfoServer:getInstance() - " + ex.getMessage());
            }
        }

        return instance;
    }
}
