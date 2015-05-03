package components;

import sharedResources.IntervalNode;
import sharedResources.SharedResources;

import java.io.File;
import java.util.*;

/**
 * Created by Krishna on 4/22/2015.
 */
public class Master
{
    File sourceFile;
    public static boolean done = false;
    public static int jobSizeThreshold = 1024;// 1000000
    static  int totalNumbers = 102400;
    IntervalNode root;
    public static int[][] mockFile;
    public static void main(String[] args) {
        //Master main = new Master("");


        mockFile = new int[2][totalNumbers];
        for(int i=0; i<totalNumbers; i++) {
            mockFile[0][i] = new Random().nextInt(1000000);
        }

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
            list.add(Master.mockFile[0][i]);
        return list;
    }

    public synchronized static void writeToFile(List<Integer> list, int st, int en)
    {
        System.out.print("Updated list of: " + st + " ==> ");
        for(int i=st; i<=en; i++)
        {
            mockFile[1][i] = list.get(i - st);
            System.out.print(list.get(i - st) + " ");

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

    private static boolean isSorted(int finalSorted)
    {
        int i;
        for(i=1; i<mockFile[finalSorted].length; i++)
        {
            if(mockFile[finalSorted][i]<mockFile[finalSorted][i-1])
            {
                System.out.println("index "+(i-1)+" = "+mockFile[finalSorted][i-1]+" ; index "+i+" = "+mockFile[finalSorted][i] );
                break;
            }
        }
        return (i<Master.mockFile[finalSorted].length)? false:true;
    }

    public static void shutDown() {
        /**
         * shutdown all threads
         */
        SharedResources.executor.shutdown();
        System.out.println("Master Shut Down");
        int finalSorted = (IntervalNode.MAX_LEVEL+1)%2;

        if(!isSorted(finalSorted))
            System.out.println("==================SORT FAILED=================");
        else
            System.out.println("==================SORT SUCCESS!!!=================");
    }
}
