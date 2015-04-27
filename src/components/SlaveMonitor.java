package components;

import sharedResources.JobStatus;
import sharedResources.Slave;
import sharedResources.SlaveStatus;

import java.io.IOException;
import java.net.*;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Krishna on 4/22/2015.
 */
public class SlaveMonitor implements Runnable {
    BlockingQueue<Slave> slavePriorityQueue;

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
        /**Network Discovery to be updated/
         *
         */
        int timeout = 10;
        for (int i = 1; i <= 255; i++) {
            String host = "192.168.0" + "." + i;
            if (isReachableByTcp(host, 22, timeout)) {
                System.out.println(host + " is reachable");
            }
        }
    }

    @Override
    public void run() {

        searchForSlaves();
    }

    public static void main(String[] args) throws IOException {
        SlaveMonitor slave = new SlaveMonitor();
        slave.run();
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

}
