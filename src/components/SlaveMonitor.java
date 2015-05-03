package components;


import sharedResources.*;

import java.io.IOException;
import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Krishna on 4/22/2015.
 */
public class SlaveMonitor implements Runnable {

    private Service connection = null;
    private String client;
    public List<InetAddress> listOfSlaves;



    private void searchForSlaves() {
        /*Network Discovery to be updated*/


      /*  int timeout = 10;
        for (int i = 100; i <= 200; i++) {
            String host = "10.10.10" + "." + i;
            if (isReachableByTcp(host, 22, timeout)) {
                System.out.println(host + " is reachable");

            }
        }*/
        listOfSlaves = new ArrayList<>();


        try {

            InetAddress addr = InetAddress.getByName("10.10.10.136");
            listOfSlaves.add(addr);
            Slave slave1 = new Slave(addr);
            addr = InetAddress.getByName("10.10.10.133");
            listOfSlaves.add(addr);
            Slave slave2 = new Slave(addr);
            if (connectToServer(slave1)) {
                System.out.println("Connection Successful!");
                slave1.setStatus(SlaveStatus.OPEN);
            }
            else
            {
                slave1.setStatus(SlaveStatus.FAILED);
            }

            if (connectToServer(slave2)) {
                System.out.println("Connection Successful!");
                slave2.setStatus(SlaveStatus.OPEN);
            }
            else
            {
                slave2.setStatus(SlaveStatus.FAILED);
            }


        } catch (Exception ex) {
            ex.printStackTrace(System.err);
        }

    }

    @Override
    public void run() {

        searchForSlaves();
    }

   /* public static void main(String[] args) throws IOException {
        SlaveMonitor slave = new SlaveMonitor();
        slave.run();
    }*/

    public static boolean isReachableByTcp(String host, int port, int timeout) {
        try {
            Socket socket = new Socket();
            SocketAddress socketAddress = new InetSocketAddress(host, port);
            socket.connect(socketAddress, timeout);
            socket.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean connectToServer(Slave slave)  {
        try {
            // Create and install a security manager.
            if (System.getSecurityManager() == null) {
                System.out.println("Creating new security manager");
                System.setSecurityManager(new SecurityManager());
            }

            // Info
            System.out.println("Client is on " + InetAddress.getLocalHost().getHostName() + " with Java version " + System.getProperty("java.version"));
            System.out.println(Slave.RMIRegistryPort);
            //Registry registry = LocateRegistry.getRegistry("127.0.0.1",Slave.RMIRegistryPort);
            Registry registry = LocateRegistry.getRegistry(slave.ip.getHostAddress(),Slave.RMIRegistryPort);

            /*
             * This does the actual connection returning a reference to the
             * server service if it suceeds.
             */

            slave.initialize((Service) registry.lookup(Slave.SORT_SERVICE));


            System.out.println("PiInfoClient:connectToServer - Connected to " + slave.ip.toString() + ":" + Slave.RMIRegistryPort + "/" + Slave.SORT_SERVICE);

            return true;
        } catch (Exception ue) {
            System.err.println("PiInfoClient:connectToServer() - UnmarshalException - Check that the server can access it's configuration / policy files");
            ue.printStackTrace(System.err);
        }
        return false;
    }

}
