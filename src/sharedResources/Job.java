package sharedResources;

import java.io.Serializable;
import java.rmi.Remote;


public class Job implements JobInterface,Serializable {

     static int count = 0;
     int startIndex, endIndex, jobId;
     int numbers[];
     public IntervalNode sourceIntervalNode;
     JobStatus status;
     public Slave completedBy;
     public long allottedTime, CompletedTime;

    public Job(){

    }


    public void assigned()
    {
        allottedTime = System.currentTimeMillis();
        setStatus(JobStatus.InProgress);
    }

    public long getAllottedTime() {
        return allottedTime;
    }

    public void setAllottedTime(long allottedTime) {
        this.allottedTime = allottedTime;
    }

    public Slave getCompletedBy() {
        return completedBy;
    }

    public void setCompletedBy(Slave completedBy) {
        this.completedBy = completedBy;
    }

    public long getCompletedTime() {
        return CompletedTime;
    }

    public void setCompletedTime(long completedTime) {
        CompletedTime = completedTime;
    }

    public static int getCount() {
        return count;
    }

    public static void setCount(int count) {
        Job.count = count;
    }

    public IntervalNode getSourceIntervalNode() {
        return sourceIntervalNode;
    }

    public void setSourceIntervalNode(IntervalNode sourceIntervalNode) {
        this.sourceIntervalNode = sourceIntervalNode;
    }

    private static final long serialVersionUID = -464196277362659008L;

//    public Job(int startIndex,int endIndex,IntervalNode sourceIntervalNode){
//
//        this.startIndex = startIndex;
//        this.endIndex = endIndex;
//        this.sourceIntervalNode = sourceIntervalNode;
//    }

    private void updateQueues()
    {
        switch (status)
        {
            case Updated:
            {
                SharedResources.job_CompletedQueue.remove(this);
                SharedResources.job_OpenQueue.remove(this);
                break;
            }
            case Completed:
            {
                SharedResources.job_CompletedQueue.add(this);
                SharedResources.job_OpenQueue.remove(this);
                SharedResources.job_OpenQueue.add(this);
                break;
            }
            default:
            {
                SharedResources.job_CompletedQueue.remove(this);
                SharedResources.job_OpenQueue.remove(this);
                SharedResources.job_OpenQueue.add(this);
            }
        }
    }


    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int[] getNumbers() {
        return numbers;
    }

    public void setNumbers(int[] numbers) {
        this.numbers = numbers;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {

        if( ! this.status.equals(status))
        {
            this.status = status;
            updateQueues();
        }
    }

}
