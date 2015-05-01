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
            if (SharedResources.slave_PullQueue.isEmpty())
            {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Slave slave = SharedResources.slave_PullQueue.peek();

                if (slave.isReadyForPull())
                {
                    slave.prepareToPull();
                    SharedResources.executor.execute(slave);
                }
            }

        }
    }

}
