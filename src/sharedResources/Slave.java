package sharedResources;

//import components.Main;

import components.Master;
import components.Service;

import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krishna on 4/22/2015.
 */
public class Slave implements Runnable {

    public InetAddress ip;
    public static int jobCountThreshold = 5;

    public SlaveStatus status = null;
    public Service service = null;

    public Job toBePushed = null;

    public List<Job> jobHistory;
    public List<Job> inProcessJobs;
    public List<Job> completedJobs;
    public List<Job> updatedJobs;

    public int failCount=0;//keep track of failed or over delay count


    public long totalPushTime, totalPullTime;
    public int totalJobsPushed, totalJobsPulled;

    public static final String SORT_SERVICE = "SortService";
    public static final int RMIRegistryPort = 2024;
    public long lastSeen;


    public Slave(InetAddress ip) {
        this.ip = ip;
    }
    /*public Slave(InetAddress ip, int port,Service service, Master master) {
        this.ip = ip;
        this.port = port;
        this.service = service;
        this.master = master;
    }*/

    public void initialize(Service stub) {
        this.service = stub;
        jobHistory = new ArrayList<Job>();
        inProcessJobs = new ArrayList<>();
        completedJobs = new ArrayList<>();
        updatedJobs = new ArrayList<>();

        setStatus(SlaveStatus.OPEN);
    }

    @Override
    public void run(){
        if (status.equals(SlaveStatus.PUSH))
        {
            pushJobData();
            setStatus(SlaveStatus.OPEN);
        } else if (status.equals(SlaveStatus.PUSH))
        {
            pullNextSortedJob();
            setStatus(SlaveStatus.OPEN);
        }
    }

    public boolean pushJobData()
    {
        /**
         * push data from file to server
         * add toBePushed to the inProgressJob list
         */
        toBePushed.assigned();
        long pushStartTimer = System.currentTimeMillis();
        //service.push( stream job data from file )
        totalPushTime += (System.currentTimeMillis() - pushStartTimer);
        totalJobsPushed++;
        jobHistory.add(toBePushed);
        inProcessJobs.add(toBePushed);
        return true;
    }

    public boolean pullNextSortedJob()
    {
        Job pullJob = completedJobs.get(0);
        long pullStartTimer = System.currentTimeMillis();
        //service.pull( write the stream to file)
        totalPullTime += (System.currentTimeMillis() - pullStartTimer);
        totalJobsPulled++;
        completedJobs.remove(pullJob);
        updatedJobs.add(pullJob);

        /**
         * get the next completed Job from server and update it to the file
         * remove it from completed list
         * update Job status
         */
        return true;
    }

    public boolean syncCompetedJobs()
    {
        /**
         * get the newly completed jobs from server and update
         */
        List<Job> jobs = null;
        //service.getCompletedList()
        for (Job j : jobs) {
            if (completedJobs.contains(j)) continue;
            Job completedJob = inProcessJobs.remove(inProcessJobs.indexOf(j));
            completedJobs.add(completedJob);
            completedJob.completed(this);
        }
        return false;
    }

    public boolean isReadyToPush(Job nextJob) {
        return (status.equals(SlaveStatus.FULL) && !jobHistory.contains(nextJob)) ? false : true;
    }

    public void prepareToPush(Job nextJob) {
        toBePushed = nextJob;
        setStatus(SlaveStatus.PUSH);
    }


    public boolean isReadyForPull() {
        return (completedJobs.isEmpty()) ? false : true;
    }

    public void prepareToPull() {
        setStatus(SlaveStatus.PULL);
    }











    public double getAveragePushTime()
    {
        return totalPushTime/totalJobsPushed;
    }

    public double getAveragePullTime()
    {
        return totalPullTime/totalJobsPulled;
    }


    public void setStatus(SlaveStatus status) {
        if (this.status == null || !this.status.equals(status)) {
            this.status = status;
            updateQ();
        }

    }

    private void updateQ() {
        switch (status) {
            case OPEN: {
                SharedResources.slave_PushQueue.add(this);
            }
            case PULL: {
                SharedResources.slave_PushQueue.remove(this);
                SharedResources.slave_PullQueue.remove(this);
            }
            case PUSH: {
                SharedResources.slave_PushQueue.remove(this);
                SharedResources.slave_PullQueue.remove(this);
            }
            case FULL: {
                SharedResources.slave_PullQueue.add(this);
            }
            case FAILED: {
                SharedResources.slave_PushQueue.remove(this);
                SharedResources.slave_PullQueue.remove(this);
            }
            case INPROGRESS: {
                /**
                 * has to come from PUSH or PULL mode, where slaves have been removed from the Q's
                 * so just add them back to re-position them
                 */
                SharedResources.slave_PushQueue.add(this);
                SharedResources.slave_PullQueue.add(this);
            }
        }
    }

    public SlaveStatus getStatus() {
        return status;
    }

    public void slaveNotResponsive() {
        failCount++;
        /**
         * update Jobs about this
         * updates Slave status
         */
    }
}
