package components;

import sharedResources.IntervalNode;
import sharedResources.SharedResources;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Krishna on 4/22/2015.
 */
public class Master
{
    File sourceFile;
    public static boolean done = false;
    public static int jobSizeThreshold = 1024;// 1000000
    static  int totalNumbers = 1024000;
    IntervalNode root;
    public static List<Integer> mockFile = new ArrayList<>();
    public static List<Integer> mockOutputFile = new ArrayList<>();
    public static void main(String[] args) {
        //Master main = new Master("");



        for(int i=0; i<totalNumbers; i++)
            mockFile.add(new Random().nextInt(1000000));
        mockOutputFile.addAll(mockFile);
        Master main = new Master();

        SharedResources.executor.execute(new SlaveMonitor());
        SharedResources.executor.execute(new Allocator());
        SharedResources.executor.execute(new Updator());

        System.out.println();

    }

    public Master()   {
        buildIntervalTree();
    }
    public Master(String sourceFile) {
        this.sourceFile = new File(sourceFile);
        totalNumbers = getArraySize();
        buildIntervalTree();
    }

    public synchronized static List<Integer> getBetween(int st, int en)
    {
        List<Integer> list = new ArrayList<>();
        for(int i=st; i<=en; i++)
            list.add(Master.mockFile.get(i));
        return list;
    }

    public synchronized static void writeToFile(List<Integer> list, int st, int en)
    {
        System.out.print("Updated list of: " + st + " ==> ");
        for(int i=st; i<en; i++)
        {
            mockOutputFile.set(i, list.get(i - st));
            System.out.print(list.get(i - st) + " ");
            long start = System.currentTimeMillis();
            long end = System.currentTimeMillis();
        }
        System.out.println();
    }

    private void buildIntervalTree()
    {
        root = new IntervalNode(0, totalNumbers-1, null);
    }

    public int getArraySize() {
        /**
         * read the number of lines in the sourceFile
         */
        return 1000000000;
    }
}
