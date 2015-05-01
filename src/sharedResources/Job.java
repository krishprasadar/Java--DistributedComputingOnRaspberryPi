package sharedResources;

//import components.Main;

/**
 * Created by Krishna on 4/22/2015.
 */
public class Job {
    //static int count = 0;
    private int start, end;
    public IntervalNode sourceIntervalNode;
    //public Main.Service sortService;
    public JobStatus status = JobStatus.OPEN;
    private Slave completedBy;
    private long allottedTime, completedTime;

    public int ID;

    public Job(int start, int end, IntervalNode intervalNode) {
        this.start = start;
        this.end = end;
        this.sourceIntervalNode = intervalNode;
        ID = start;
    }


    public void getDataFromFile() {

    }

    public void pushDataToFile() {

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
            case OPEN:
            {
                SharedResources.job_OpenQueue.add(this);
                break;
            }
            case UPDATED: {
                SharedResources.job_OpenQueue.remove(this);
                /**
                 * update the IntervalTree
                 */
                break;
            }
            case COMPLETED:
            {
                SharedResources.job_OpenQueue.remove(this);
            }
            default:
            {
                SharedResources.job_OpenQueue.remove(this);
                SharedResources.job_OpenQueue.add(this);
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
}
