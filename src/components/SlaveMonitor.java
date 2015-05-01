package components;


import sharedResources.Slave;

import java.io.IOException;
import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.AccessControlException;

/**
 * Created by Krishna on 4/22/2015.
 */
public class SlaveMonitor extends Thread {

    private Service connection = null;
    private String client;

    public SlaveMonitor() {

    }

    public void initiate()
    {
        new SlaveMonitor().start();
    }

    public void start()
    {

    }


    private void searchForSlaves() {
        /**Network Discovery to be updated/
         *
         /*  *//*
        int timeout = 10;
        for (int i = 1; i <= 255; i++) {
            String host = "192.168.0" + "." + i;
            if (isReachableByTcp(host, 22, timeout)) {
                System.out.println(host + " is reachable");
            }
        }*/
        try {

            InetAddress addr = InetAddress.getByName("127.0.0.1");
            Slave slave = new Slave(addr);
            connectToServer(slave);
            int[] numbers = {1,6,7,3,2};
            numbers = slave.service.sort(numbers);
            for(int i= 0; i < numbers.length; i++)
            {
                System.out.println(numbers[i]);
            }
            System.out.println("Connection Successful!");

        } catch (UnknownHostException | NotBoundException | MalformedURLException | RemoteException | ClassNotFoundException | AccessControlException ex) {
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

    private boolean connectToServer(Slave slave) throws UnknownHostException, NotBoundException, MalformedURLException, RemoteException, UnmarshalException, ClassNotFoundException, java.rmi.ConnectException, AccessControlException {
        try {
            // Create and install a security manager.
            if (System.getSecurityManager() == null) {
                System.out.println("Creating new security manager");
                System.setSecurityManager(new SecurityManager());
            }

            // Info
            System.out.println("Client is on " + InetAddress.getLocalHost().getHostName() + " with Java version " + System.getProperty("java.version"));
            System.out.println(Slave.RMIRegistryPort);
            Registry registry = LocateRegistry.getRegistry("127.0.0.1",Slave.RMIRegistryPort);

            /*
             * This does the actual connection returning a reference to the
             * server service if it suceeds.
             */
            slave.initialize((Service) registry.lookup(Slave.SORT_SERVICE));

            System.out.println("PiInfoClient:connectToServer - Connected to " + slave.ip.toString() + ":" + Slave.RMIRegistryPort + "/" + Slave.SORT_SERVICE);

            return true;
        } catch (UnmarshalException ue) {
            System.err.println("PiInfoClient:connectToServer() - UnmarshalException - Check that the server can access it's configuration / policy files");
            ue.printStackTrace(System.err);
        }
        return false;
    }

}
