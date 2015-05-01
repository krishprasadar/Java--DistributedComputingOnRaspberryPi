package sharedResources;

import java.io.Serializable;

/**
 * Created by raghavbabu on 4/29/15.
 */
public interface JobInterface extends Serializable{

    public int getEndIndex();

    public int getJobId();

    public int[] getNumbers();

    public void setNumbers(int[] numbers);

    public int getStartIndex();

    public JobStatus getStatus();

    public void setStatus(JobStatus status) ;

    public void setJobId(int jobId);

}
