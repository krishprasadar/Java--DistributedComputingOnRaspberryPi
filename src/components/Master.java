package components;

import sharedResources.IntervalNode;
import sharedResources.Job;

import java.io.File;

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
        //Master main = new Master("");
        SlaveMonitor monitor = new SlaveMonitor();
        monitor.start();
    }

    public Master(String sourceFile) {
        this.sourceFile = new File(sourceFile);
        totalNumbers = getArraySize();
        buildIntervalTree();
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
