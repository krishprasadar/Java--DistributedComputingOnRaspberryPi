package components;

import sharedResources.IntervalNode;
import sharedResources.Job;

import java.io.File;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Krishna on 4/22/2015.
 */
public class Master
{
    File sourceFile;
    public static boolean done = false;
    public static int jobSizeThreshold = 10000000;
    int totalNumbers;
    IntervalNode root;

    public static void main(String[] args) {

        Map<InetAddress,Service> slaveServiceStubMap = new HashMap<InetAddress,Service>();

        SlaveMonitor monitor = new SlaveMonitor(slaveServiceStubMap);
        Thread slaveMonitorThread = new Thread(monitor);
        slaveMonitorThread.start();

    }

    public Master(String sourceFile) {
        this.sourceFile = new File(sourceFile);
        totalNumbers = getArraySize();
        buildIntervalTree();
    }

    public synchronized int[] fetchDataForJob(Job j)
    {
        int[] arr = new int[j.getEndIndex() - j.getStartIndex()];
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
        root = new IntervalNode(1, totalNumbers, null);
    }

    public int getArraySize() {
        /**
         * read the number of lines in the sourceFile
         */
        return 1000000000;
    }
}
