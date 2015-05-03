package sharedResources;

//import components.Main;

import components.Master;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Krishna on 4/22/2015.
 */
public class Job implements JobInterface, Serializable{
    //static int count = 0;
    private int start, end;
    public IntervalNode sourceIntervalNode;
    //public Main.Service sortService;
    public JobStatus status = null;
    private Slave completedBy;
    public long allottedTime, completedTime;
    private static final long serialVersionUID = 7526472295622776147L;
    public int ID;


    public Job(int start, int end, IntervalNode intervalNode) {
        this.start = start;
        this.end = end;
        this.sourceIntervalNode = intervalNode;
        ID = start;
        setStatus(JobStatus.OPEN);
    }


    public List<Integer> getDataFromFile() {
        return Master.getBetween(start, end);
    }

    public void pushDataToFile(List<Integer> list) {
        Master.writeToFile(list, start, end);
        setStatus(JobStatus.UPDATED);
    }


    public void failed()
    {
        setStatus(JobStatus.FAILED);
    }

    public void assigned()
    {
        allottedTime = System.currentTimeMillis();
        setStatus(JobStatus.ASSIGNED);
    }

    public void completed(Slave slave) {
        completedBy = slave;
        completedTime = System.currentTimeMillis();
        setStatus(JobStatus.COMPLETED);
    }

    public void setStatus(JobStatus status)
    {
        if( this.status==null || ! this.status.equals(status))
        {
            this.status = status;
            updateQueues();
        }
    }

    private void updateQueues()
    {
        synchronized (SharedResources.job_OpenQueue)
        {
            switch (status)
            {
                case OPEN:
                {
                    SharedResources.job_OpenQueue.add(this);
                    break;
                }
                case UPDATED: {
                    SharedResources.job_OpenQueue.remove(this);
                    sourceIntervalNode.setCompleted();
                    break;
                }
                case COMPLETED:
                {
                    SharedResources.job_OpenQueue.remove(this);
                    break;
                }
                case ASSIGNED:
                {
                    SharedResources.job_OpenQueue.remove(this);
                    break;
                }
                case FAILED:
                {
                    SharedResources.job_OpenQueue.remove(this);
                    SharedResources.job_OpenQueue.add(this);
                }
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Job job = (Job) o;

        if (ID != job.ID) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return ID;
    }

    @Override
    public int getID() {
        return ID;
    }

    public void jobReport()
    {
        System.out.println("    JOB:"+getID()+" status:"+status);
    }
}
