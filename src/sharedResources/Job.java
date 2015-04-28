package sharedResources;

import java.io.Serializable;


public class Job implements Serializable {

    private int start, end, jobId;
    private int numbers[];
    private JobStatus status;

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


//    public Job(int start, int end ) {
//        this.start = start;
//        this.end = end;
//        jobId = Job.count++;
//    }
}
