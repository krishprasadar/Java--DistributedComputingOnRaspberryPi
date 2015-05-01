package components;

import sharedResources.Job;
import sharedResources.SharedResources;
import sharedResources.Slave;

/**
 * Created by Krishna on 4/22/2015.
 */
public class Allocator implements Runnable{

    public static int TASK_PRIORITY = 1;

    public void run()
    {
        while( !Master.done)
        {
            if (SharedResources.job_OpenQueue.isEmpty() || SharedResources.slave_PushQueue.isEmpty())
            {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Slave slave = SharedResources.slave_PushQueue.peek();
                Job nextJob = SharedResources.job_OpenQueue.peek();
                if (slave.isReadyToPush(nextJob))
                {
                    slave.prepareToPush(nextJob);
                    SharedResources.executor.execute(slave);
                }
            }

        }
    }
}
