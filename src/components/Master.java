package components;

import sharedResources.Job;
import sharedResources.JobStatus;

import java.io.File;
import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Krishna on 4/22/2015.
 */
public class Master
{
    File sourceFile;
    int jobSizeThreshold;
    int totalNumbers;
    BlockingQueue<Job> jobPriorityQueue;
    IntervalNode root;

    public static void main(String[] args) {
        //Master main = new Master("");
        SlaveMonitor monitor = new SlaveMonitor();
        monitor.start();
    }

    public Master(String sourceFile) {
        this.sourceFile = new File(sourceFile);
        totalNumbers = 100000;
        root = new IntervalNode(1, totalNumbers);
    }

    public synchronized int[] fetchDataForJob(Job j)
    {
        int[] arr = new int[j.end - j.start];
        /**
         *
         */

        return arr;
    }

    public synchronized boolean updateJobData(Job j, int[] sortedData)
    {
        /**
         * Update the input file with the sorted data and job start and end index
         */
        return false;
    }

    private void buildIntervalTree()
    {

        jobPriorityQueue = new PriorityBlockingQueue<Job>(10, new Comparator<Job>() {
            @Override
            public int compare(Job j1, Job j2) {
                if(j1.status.getStatusCode() == j2.status.getStatusCode() && j1.status.equals(JobStatus.InProgress))
                    return Long.compare(j1.allottedTime, j2.allottedTime);
                return Integer.compare(j1.status.getStatusCode(), j2.status.getStatusCode());
            }
        });
    }

    public class IntervalNode
    {
        int start, end;
        IntervalNode left, right, parent;
        boolean isLeaf = false;
        boolean completed=false;

        public IntervalNode(int start, int end) {
            this.start = start;
            this.end = end;

            if((end - start) <= jobSizeThreshold)
                generateJob();
            else
                createChildNodes();
        }

        private void createChildNodes() {
            int mid = start+((end-start)/2);
            left = new IntervalNode(start, mid);
            right = new IntervalNode(mid+1, end);
        }

        private void generateJob()
        {
            Job job = new Job(start, end, this);
            jobPriorityQueue.add(job);
        }



    }

}
