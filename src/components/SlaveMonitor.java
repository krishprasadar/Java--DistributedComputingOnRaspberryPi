package components;


import sharedResources.Job;
import sharedResources.JobStatus;
import sharedResources.Slave;
import sharedResources.SlaveStatus;

import java.io.IOException;
import java.net.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.UnmarshalException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Krishna on 4/22/2015.
 */
public class SlaveMonitor implements Runnable {
    BlockingQueue<Slave> slavePriorityQueue;

    private Map<InetAddress,Service> slaveServiceStubMap;
    private Map<Integer,JobStatus> jobStatusMap;

    public SlaveMonitor(Map<InetAddress,Service> slaveServiceStubMap){
      this.slaveServiceStubMap = slaveServiceStubMap;
    }

    public SlaveMonitor() {
        slavePriorityQueue = new PriorityBlockingQueue<Slave>(5, new Comparator<Slave>() {
            @Override
            public int compare(Slave s1, Slave s2) {
                if (s1.status.getStatusCode() == s2.status.getStatusCode() && s1.status.equals(SlaveStatus.Open))
                    return Long.compare(s1.averageServiceTime, s2.averageServiceTime);
                return Integer.compare(s1.status.getStatusCode(), s2.status.getStatusCode());
            }
        });
    }

    private void searchForSlaves() {

//
//        int timeout = 10;
//        for (int i = 1; i <= 255; i++) {
//            String host = "192.168.0" + "." + i;
//            if (isReachableByTcp(host, 22, timeout)) {
//                System.out.println(host + " is reachable");
//                try {
//                    InetAddress addr = InetAddress.getByName(host);
//                    Slave slave = new Slave(addr);
//                    slave.setIp(addr);
//                } catch (UnknownHostException e) {
//                    e.printStackTrace();
//                }
//            }
//         }

        InetAddress addr = null;
        try {
            addr = InetAddress.getByName("127.0.0.1");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Slave slave = new Slave(addr);
        //slave.setIp(addr);
        //connectToSlaveIfItsReachable(slave);

        try {
            connectToServer(slave);

            int[] numbers1 = {10,640,76,32,225};
            Job job1 = new Job();
            job1.setJobId(1);
            job1.setNumbers(numbers1);

            slave.service.push(job1);

            int[] numbers2 = {1,23,45,100,200};
            Job job2 = new Job();
            job1.setJobId(2);
            job2.setNumbers(numbers2);

            slave.service.push(job2);

            while(slave.service.getJobStatus(job1.getJobId()) == JobStatus.Completed &&
                slave.service.getJobStatus(job2.getJobId()) == JobStatus.Completed ){
                continue;

            }
            List<Job> jobList = slave.service.pull();

            for(Job job : jobList) {

                int[] numbers = job.getNumbers();

                for (int i = 0; i < numbers.length; i++) {
                    System.out.println(numbers[i]);
                }
            }

        } catch (UnknownHostException | NotBoundException | MalformedURLException | RemoteException
                | ClassNotFoundException | AccessControlException ex) {
            ex.printStackTrace(System.err);
        }


    }

    public void connectToSlaveIfItsReachable(Slave slave){



    }

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

    private Map<InetAddress,Service> connectToServer(Slave slave) throws UnknownHostException, NotBoundException,
            MalformedURLException, RemoteException, UnmarshalException, ClassNotFoundException,
            java.rmi.ConnectException, AccessControlException {

        try {
            // Create and install a security manager.
            if (System.getSecurityManager() == null) {
                System.out.println("Creating new security manager");
                System.setSecurityManager(new SecurityManager());
            }


            System.out.println("Client is on " + InetAddress.getLocalHost().getHostName()
                    + " with Java version " + System.getProperty("java.version"));

            Registry registry = LocateRegistry.getRegistry("127.0.0.1", slave.port);


            slave.service = (Service) registry.lookup(Service.SORT_SERVICE);
            System.out.println("PiInfoClient:connectToServer - Connected to " + slave.ip.toString()
                    + ":" + Service.RMIRegistryPort + "/" + Service.SORT_SERVICE);

            slaveServiceStubMap.put(slave.ip, slave.service);

            System.out.println("Slave and service Map created for " + slave.ip);
            return slaveServiceStubMap;
        }

        catch (UnmarshalException ue) {
            System.err.println("PiInfoClient:connectToServer() - UnmarshalException - " +
                    "Check that the server can access it's configuration / policy files");
            ue.printStackTrace(System.err);
        }
        return null;
    }

    @Override
    public void run() {
        searchForSlaves();
    }

}
