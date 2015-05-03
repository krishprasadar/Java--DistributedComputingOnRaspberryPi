package sharedResources;

import components.Master;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Rathinakumar on 4/29/2015.
 */
public class IntervalNode implements Serializable, Runnable
{
    int start, end;
    public static int MAX_LEVEL = 0;
    IntervalNode left, right, parent;
    boolean completed=false;
    private int level = 0;

    public IntervalNode(int start, int end, IntervalNode parent) {
        this.start = start;
        this.end = end;
        this.parent = parent;
        if(parent!=null)
            this.level = parent.level+1;
        if((end - start) <= Master.jobSizeThreshold)
            generateJob();
        else
            createChildNodes();
    }

    private void createChildNodes() {
        int mid = start+((end-start)/2);
        left = new IntervalNode(start, mid, this);
        right = new IntervalNode(mid+1, end, this);
    }

    private void generateJob()
    {
        Job job = new Job(start, end, this);
        MAX_LEVEL = this.level;
    }

    public IntervalNode getOtherChildThan(IntervalNode child)
    {
        return (this.right.equals(child))? left : right;
    }

    public IntervalNode getSibling()
    {
        return this.parent.getOtherChildThan(this);
    }

    public boolean isCompleted()
    {
        return completed;
    }

    public void setCompleted()
    {
        completed = true;
        //check if the parent is computable
        if(isRoot()) {
            Master.done = true;
            Master.shutDown();
        }
        else
        {
            if(getSibling().isCompleted())
                SharedResources.initializeTask(this.parent);
        }
    }

    public void mergeChildren()
    {
        int mergeFile = ((MAX_LEVEL-level)%2 +1)%2;
        int readFile = (mergeFile+1)%2;
        int i = left.start;
        int j = right.start;
        int m = left.start;
        while (i<=left.end && j<=right.end)
            if(Master.mockFile[readFile][i]<Master.mockFile[readFile][j])
                Master.mockFile[mergeFile][m++] = Master.mockFile[readFile][i++];
            else
                Master.mockFile[mergeFile][m++] = Master.mockFile[readFile][j++];
        while (i<=left.end)
            Master.mockFile[mergeFile][m++] = Master.mockFile[readFile][i++];
        while (j<=right.end)
            Master.mockFile[mergeFile][m++] = Master.mockFile[readFile][j++];
        this.setCompleted();
    }

    private boolean isRoot() {
        return (parent==null)? true: false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntervalNode that = (IntervalNode) o;

        if (start != that.start) return false;
        return end == that.end;

    }

    @Override
    public int hashCode() {
        int result = start;
        result = 31 * result + end;
        return result;
    }

    @Override
    public void run() {
        mergeChildren();
    }
}
