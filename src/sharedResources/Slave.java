package sharedResources;

import components.Master;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by Krishna on 4/22/2015.
 */
public class Slave implements Runnable {
    public InetAddress ip;
    public int port;
    public Job jobAssigned;
    public int failCount=0;//keep track of failed or over delay count
    public SlaveStatus status;
    public SortServer sortServer;
    public Master master;
    public List<Job> completedJobs;
    public long averageServiceTime, bestTime, worstTime;

    public long lastSeen;

    public Slave(InetAddress ip, int port, SortServer sortServer, Master master) {
        this.ip = ip;
        this.port = port;
        this.sortServer = sortServer;
        this.master = master;
    }


    @Override
    public void run() {
        if(jobAssigned != null)
            master.updateJobData(jobAssigned, sortServer.sort(master.fetchDataForJob(jobAssigned)));
    }
}
