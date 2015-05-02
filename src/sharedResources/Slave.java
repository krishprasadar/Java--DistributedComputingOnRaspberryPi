package sharedResources;

//import components.Main;

import components.Service;

import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Krishna on 4/22/2015.
 */
public class Slave implements Runnable, Serializable {

    public InetAddress ip;
    public static int jobCountThreshold = 5;

    public SlaveStatus status = null;
    public Service service = null;

    private Job toBePushed = null;
    private Job pullJob = null;
    public long inactivatedTime;

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
            try {
                pushJobData();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            setStatus(SlaveStatus.OPEN);
        } else if (status.equals(SlaveStatus.PULL))
        {
            try {
                pullNextSortedJob();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            setStatus(SlaveStatus.OPEN);
        }


    }

    public boolean pushJobData() throws RemoteException {
        /**
         * push data from file to server
         * add toBePushed to the inProgressJob list
         */

        if((inProcessJobs.size() + completedJobs.size()) < jobCountThreshold) {
            System.out.println("Push Job " + toBePushed.getID() + "; status: " + toBePushed.status + " ; thread" + Thread.currentThread().getId());
            service.push(toBePushed, toBePushed.getDataFromFile());
            toBePushed.assigned();
            long pushStartTimer = System.currentTimeMillis();
            //service.push( stream job data from file )
            totalPushTime += (System.currentTimeMillis() - pushStartTimer);
            totalJobsPushed++;
            jobHistory.add(toBePushed);
            inProcessJobs.add(toBePushed);
            setStatus(SlaveStatus.INPROGRESS);

        }
        else {
            setStatus(SlaveStatus.FULL);
            return false;
        }

        return true;
    }

    public boolean pullNextSortedJob() throws RemoteException {

        if((inProcessJobs.size() + completedJobs.size()) > 0) {
            long pullStartTimer = System.currentTimeMillis();
            //service.pull( write the stream to file)
            List<Integer> jobPulled = service.pull(pullJob);
            if(jobPulled != null) {
                pullJob.pushDataToFile(jobPulled);
                totalPullTime += (System.currentTimeMillis() - pullStartTimer);
                totalJobsPulled++;
                completedJobs.remove(pullJob);
                updatedJobs.add(pullJob);
                setStatus(SlaveStatus.INPROGRESS);
                syncCompetedJobs();
            }
        }
        else {
            setStatus(SlaveStatus.OPEN);
            return false;
        }
        /**
         * get the next completed Job from server and update it to the file
         * remove it from completed list
         * update Job status
         */
        return true;
    }

    public boolean syncCompetedJobs() throws RemoteException {
        /**
         * get the newly completed jobs from server and update
         */

        List<JobInterface> jobs = service.getCompletedJobs();
        //System.out.println("SYNC: Thread" + Thread.currentThread().getId());
        //System.out.println("In Progress Jobs Thread" + Thread.currentThread().getId());
        /*for (Job jj : inProcessJobs) {
            System.out.print(jj.getID() + " ");
        }
        for (Job jj : completedJobs) {
            System.out.print(jj.getID() + " ");
        }*/
        //System.out.println();
        for (JobInterface j : jobs) {
            //System.out.println("SYNC: job ID: "+j.getID()+"");
            if (completedJobs.contains(j))
                continue;
            int index = inProcessJobs.indexOf((Job)j);
            if(index > -1) {
                Job completedJob = inProcessJobs.remove(index);
                completedJobs.add(completedJob);
                completedJob.completed(this);
                setStatus(SlaveStatus.PULL);
            }
        }
        return false;
    }

    public boolean isReadyToPush(Job nextJob) {
        return (status.equals(SlaveStatus.FULL) && jobHistory.contains(nextJob)) ? false : true;
    }

    public void prepareToPush(Job nextJob) {
        toBePushed = nextJob;
        toBePushed.setStatus(JobStatus.ASSIGNED);
        //inProcessJobs.add(toBePushed);
        setStatus(SlaveStatus.PUSH);
    }


    public boolean isReadyForPull() throws RemoteException {
        syncCompetedJobs();
        return (completedJobs.isEmpty()) ? false : true;
    }

    public void prepareToPull(){
        pullJob = completedJobs.get(0);
        //completedJobs.remove(pullJob);
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
        //
        if (this.status == null || !this.status.equals(status)) {
            this.status = status;
            updateQ();
        }

    }

    private void updateQ() {
        switch (status) {
            case OPEN: {
                SharedResources.slave_PushQueue.add(this);
                SharedResources.slave_InActive.remove(this);
                break;
            }
            case PULL: {
                SharedResources.slave_PushQueue.remove(this);
                SharedResources.slave_PullQueue.remove(this);
                break;
            }
            case PUSH: {
                SharedResources.slave_PushQueue.remove(this);
                SharedResources.slave_PullQueue.remove(this);
                break;
            }
            case FULL: {
                SharedResources.slave_PullQueue.add(this);
                break;
            }
            case FAILED: {
                SharedResources.slave_PushQueue.remove(this);
                SharedResources.slave_PullQueue.remove(this);
                SharedResources.slave_InActive.add(this);
                break;
            }
            case INPROGRESS: {
                /**
                 * has to come from PUSH or PULL mode, where slaves have been removed from the Q's
                 * so just add them back to re-position them
                 */
                SharedResources.slave_PushQueue.add(this);
                SharedResources.slave_PullQueue.add(this);
                break;
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

    public void slaveReport()
    {
        System.out.println("slave --> "+this+" ; status : "+status+ "; completed:"+completedJobs.size()+" inprogress "+inProcessJobs.size());
    }
}
