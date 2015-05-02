package sharedResources;

import components.Master;

import java.io.Serializable;

/**
 * Created by Rathinakumar on 4/29/2015.
 */
public class IntervalNode implements Serializable
{
    int start, end;
    IntervalNode left, right, parent;
    boolean completed=false;

    public IntervalNode(int start, int end, IntervalNode parent) {
        this.start = start;
        this.end = end;
        this.parent = parent;

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

    }
}
