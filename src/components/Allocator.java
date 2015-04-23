package components;

import sharedResources.Job;
import sharedResources.Slave;

import java.util.concurrent.BlockingQueue;

/**
 * Created by Krishna on 4/22/2015.
 */
public class Allocator {
    BlockingQueue<Job> jobPriorityQueue;
    BlockingQueue<Slave> slavePriorityQueue;
}
