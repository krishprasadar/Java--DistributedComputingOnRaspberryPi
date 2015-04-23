package sharedResources;

import components.Master;

/**
 * Created by Krishna on 4/22/2015.
 */
public class Job {
    static int count = 0;
    public int start, end, jobId;
    Master.IntervalNode sourceIntervalNode;
    public SortServer sortServer;
    public JobStatus status=JobStatus.Open;
    public long allottedTime, CompletedTime;


    public Job(int start, int end, Master.IntervalNode intervalNode) {
        this.start = start;
        this.end = end;
        this.sourceIntervalNode = intervalNode;
        jobId = Job.count++;
    }
}
