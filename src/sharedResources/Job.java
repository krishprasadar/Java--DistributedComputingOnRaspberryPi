package sharedResources;

import java.io.Serializable;
import java.rmi.Remote;


public class Job implements Serializable,Remote {

     int start, end, jobId;
     int numbers[];
     JobStatus status;

    private static final long serialVersionUID = -464196277362659008L;

    public Job(){

    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public int[] getNumbers() {
        return numbers;
    }

    public void setNumbers(int[] numbers) {
        this.numbers = numbers;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public JobStatus getStatus() {
        return status;
    }

    public void setStatus(JobStatus status) {
        this.status = status;
    }

}
