package sharedResources;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Rathinakumar on 4/28/2015.
 */
public class SharedResources {

    public static int MAX_PUSH_THREADS = 5;

    public static ExecutorService executor = Executors.newFixedThreadPool(MAX_PUSH_THREADS);

    public static BlockingQueue<Job> job_OpenQueue = new PriorityBlockingQueue<Job>(10, new Comparator<Job>() {
        @Override
        public int compare(Job j1, Job j2) {
            if (j2.status.equals(JobStatus.ASSIGNED) && j1.status.equals(JobStatus.ASSIGNED))
                return Long.compare(j1.allottedTime, j2.allottedTime);
            return Integer.compare(j1.status.getStatusCode(), j2.status.getStatusCode());
        }
    });


    public static BlockingQueue<Slave> slave_PushQueue = new PriorityBlockingQueue<Slave>(5, new Comparator<Slave>() {
        @Override
        public int compare(Slave s1, Slave s2) {
            if (s2.status.equals(SlaveStatus.INPROGRESS) && s1.status.equals(SlaveStatus.INPROGRESS))
                return Double.compare(s1.getAveragePushTime(), s2.getAveragePushTime());
            return Integer.compare(s1.status.getStatusCode(), s2.status.getStatusCode());
        }
    });


    public static BlockingQueue<Slave> slave_PullQueue = new PriorityBlockingQueue<Slave>(5, new Comparator<Slave>() {
        @Override
        public int compare(Slave s1, Slave s2) {
            if (s2.status.equals(SlaveStatus.INPROGRESS) && s1.status.equals(SlaveStatus.INPROGRESS))
                return Integer.compare(s2.completedJobs.size(), s1.completedJobs.size());
            return Integer.compare(s2.status.getStatusCode(), s1.status.getStatusCode());
        }
    });

    public static BlockingQueue<Slave> slave_InActive = new PriorityBlockingQueue<>(2, new Comparator<Slave>() {
        @Override
        public int compare(Slave o1, Slave o2) {
            return Long.compare(o1.inactivatedTime, o2.inactivatedTime);
        }
    });
}
