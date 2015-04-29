package sharedResources;

//import components.Main;

import components.Master;
import components.Service;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Created by Krishna on 4/22/2015.
 */
public class Slave implements Runnable {

    public InetAddress ip;
    public static int jobCountThreshold = 5;
    public Master master;

    public SlaveStatus status;
    public Service service;

    public Job toBePushed;
    public List<Job> jobHistory;
    public List<Job> inProcessJobs;
    public List<Job> completedJobs;
    public List<Job> updatedJobs;

    public int failCount=0;//keep track of failed or over delay count


    public long totalPushTime, totalPullTime;
    public int totalJobsPushed, totalJobsPulled;

    public long averageServiceTime, bestTime, worstTime;
    public static final String SORT_SERVICE = "SortService";
    public static final int RMIRegistryPort = 2024;
    public long lastSeen;

    public boolean pushMode = false, pullMode = false;


    public Slave(InetAddress ip) {
        this.ip = ip;
    }
    /*public Slave(InetAddress ip, int port,Service service, Master master) {
        this.ip = ip;
        this.port = port;
        this.service = service;
        this.master = master;
    }*/


    @Override
    public void run(){
        if(pushMode)
        {
            pushJobData();
            pushMode = false;
        }
        else if(pullMode)
        {
            pullNextSortedJob();
            pullMode = false;
        }
    }

    public boolean pushJobData()
    {
        /**
         * push data from file to server
         * add toBePushed to the inProgressJob list
         */

        return true;
    }

    public boolean isReadyToPush() {
        if( !pullMode && status.equals(SlaveStatus.Active) && toBePushed == null && inProcessJobs.size()+completedJobs.size() < jobCountThreshold)
            return true;
        return false;
    }

    public boolean updateCompetedList()
    {
        /**
         * get the newly completed jobs from server and update
         */
        return false;
    }

    public boolean pullNextSortedJob()
    {
        /**
         * get the next completed Job from server and update it to the file
         * remove it from completed list
         * update Job status
         */
        return true;
    }

    public void slaveNotResponsive()
    {
        /**
         * update Jobs about this
         * updates Slave status
         */
    }

    public void prepareToPush(Job bestJob) {
        pushMode = true;
        SharedResources.slave_PullQueue.remove(this);
    }

    public void prepareToPull() {
        pullMode = true;
        SharedResources.slave_PullQueue.remove(this);
    }

    public boolean isReadyToPull() {
        if( ! pushMode && ! completedJobs.isEmpty() && status.equals(SlaveStatus.Active))
            return true;
        return false;
    }

    public double getAveragePushTime()
    {
        return totalPushTime/totalJobsPushed;
    }

    public double getAveragePullTime()
    {
        return totalPullTime/totalJobsPulled;
    }
}
