package components;

import sharedResources.JobStatus;
import sharedResources.Slave;
import sharedResources.SlaveStatus;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by Krishna on 4/22/2015.
 */
public class SlaveMonitor implements Runnable{
    BlockingQueue<Slave> slavePriorityQueue;

    public SlaveMonitor() {
        slavePriorityQueue = new PriorityBlockingQueue<>(5, new Comparator<Slave>() {
            @Override
            public int compare(Slave s1, Slave s2) {
                if(s1.status.getStatusCode() == s2.status.getStatusCode() && s1.status.equals(SlaveStatus.Open))
                    return Long.compare(s1.averageServiceTime, s2.averageServiceTime);
                return Integer.compare(s1.status.getStatusCode(), s2.status.getStatusCode());
            }
        });
    }

    private void searchForSlaves()
    {
        /**Network Discovery to be updated/
         *
         */
    }

    @Override
    public void run() {

        searchForSlaves();
    }
}
