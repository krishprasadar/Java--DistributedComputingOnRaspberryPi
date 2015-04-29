package components;

import sharedResources.SharedResources;
import sharedResources.Slave;

/**
 * Created by Rathinakumar on 4/28/2015.
 */
public class Updator {

    public static int TASK_PRIORITY = 1;

    public void run()
    {
        while( !Master.done)
        {
            if(! SharedResources.job_OpenQueue.isEmpty() || ! SharedResources.job_CompletedQueue.isEmpty())
            {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Slave suitableSlave = SharedResources.slave_PullQueue.peek();

                if(suitableSlave.isReadyToPull())
                {
                    suitableSlave.prepareToPush(SharedResources.job_OpenQueue.poll());
                    SharedResources.executor.execute(suitableSlave);
                }
            }

        }
    }

}
