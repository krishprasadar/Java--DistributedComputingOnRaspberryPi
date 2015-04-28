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
public class Slave {

    public InetAddress ip;
    public int port = Service.RMIRegistryPort;
    public Job jobAssigned;
    public int failCount=0; //keep track of failed or over delay count
    public SlaveStatus status;
    public Service service;
    public Master master;
    public List<Job> completedJobs;
    public long averageServiceTime, bestTime, worstTime;
    public long lastSeen;

    public Slave(InetAddress ip) {
        this.ip = ip;
    }



    public InetAddress getIp() {
        return ip;
    }

    public void setIp(InetAddress ip) {
        this.ip = ip;
    }

    public long getAverageServiceTime() {
        return averageServiceTime;
    }

    public void setAverageServiceTime(long averageServiceTime) {
        this.averageServiceTime = averageServiceTime;
    }

    public long getBestTime() {
        return bestTime;
    }

    public void setBestTime(long bestTime) {
        this.bestTime = bestTime;
    }

    public List<Job> getCompletedJobs() {
        return completedJobs;
    }

    public void setCompletedJobs(List<Job> completedJobs) {
        this.completedJobs = completedJobs;
    }

    public int getFailCount() {
        return failCount;
    }

    public void setFailCount(int failCount) {
        this.failCount = failCount;
    }

    public Job getJobAssigned() {
        return jobAssigned;
    }

    public void setJobAssigned(Job jobAssigned) {
        this.jobAssigned = jobAssigned;
    }

    public long getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(long lastSeen) {
        this.lastSeen = lastSeen;
    }

    public Master getMaster() {
        return master;
    }

    public void setMaster(Master master) {
        this.master = master;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public SlaveStatus getStatus() {
        return status;
    }

    public void setStatus(SlaveStatus status) {
        this.status = status;
    }

    public long getWorstTime() {
        return worstTime;
    }

    public void setWorstTime(long worstTime) {
        this.worstTime = worstTime;
    }


//    @Override
//    public void run(){
//
//            try {
//                if(jobAssigned != null)
//                    master.updateJobData(jobAssigned, service.sort(master.fetchDataForJob(jobAssigned)));
//            } catch (RemoteException e) {
//                e.printStackTrace();
//            }
//    }

}
