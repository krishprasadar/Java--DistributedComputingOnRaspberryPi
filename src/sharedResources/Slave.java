package sharedResources;

//import components.Main;

import components.Master;

import java.net.InetAddress;
import java.rmi.RemoteException;
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
    public Main.Service service;
    public Master master;
    public List<Job> completedJobs;
    public long averageServiceTime, bestTime, worstTime;

    public long lastSeen;

    public Slave(InetAddress ip, int port, Main.Service service, Master master) {
        this.ip = ip;
        this.port = port;
        this.service = service;
        this.master = master;
    }


    @Override
    public void run(){

            try {
                if(jobAssigned != null)
                    master.updateJobData(jobAssigned, service.sort(master.fetchDataForJob(jobAssigned)));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
    }
}
