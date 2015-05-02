package components;

import sharedResources.SharedResources;
import sharedResources.Slave;

import java.rmi.RemoteException;

/**
 * Created by Rathinakumar on 4/28/2015.
 */
public class Updator implements Runnable{

    public static int TASK_PRIORITY = 1;

    public void run()
    {
        while( !Master.done)
        {
            synchronized (SharedResources.slave_PullQueue)
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

                    try {
                        if (slave != null && slave.isReadyForPull())
                        {
                            slave.prepareToPull();
                            SharedResources.executor.execute(slave);
                        }
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

}
