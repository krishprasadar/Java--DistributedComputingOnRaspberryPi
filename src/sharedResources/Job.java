package sharedResources;

//import components.Main;

import components.Master;

/**
 * Created by Krishna on 4/22/2015.
 */
public class Job {
    static int count = 0;
    public int start, end;
    public IntervalNode sourceIntervalNode;
    //public Main.Service sortService;
    public JobStatus status = JobStatus.Open;
    public Slave completedBy;
    public long allottedTime, CompletedTime;

    public Job(int start, int end, IntervalNode intervalNode) {
        this.start = start;
        this.end = end;
        this.sourceIntervalNode = intervalNode;
    }



    public void failed()
    {
        setStatus(JobStatus.Failed);
    }

    public void assigned()
    {
        allottedTime = System.currentTimeMillis();
        setStatus(JobStatus.InProgress);
    }

    public void setStatus(JobStatus status)
    {
        if( ! this.status.equals(status))
        {
            this.status = status;
            updateQueues();
        }
    }

    private void updateQueues()
    {
        switch (status)
        {
            case Updated:
            {
                SharedResources.job_CompletedQueue.remove(this);
                SharedResources.job_OpenQueue.remove(this);
                /**
                 * update the IntervalTree
                 */
                break;
            }
            case Completed:
            {
                SharedResources.job_CompletedQueue.add(this);

                SharedResources.job_OpenQueue.remove(this);
                SharedResources.job_OpenQueue.add(this);
            }
            default:
            {
                SharedResources.job_CompletedQueue.remove(this);
                SharedResources.job_OpenQueue.remove(this);
                SharedResources.job_OpenQueue.add(this);
            }
        }
    }

}
