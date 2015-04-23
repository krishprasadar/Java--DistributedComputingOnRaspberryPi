package sharedResources;

/**
 * Created by Krishna on 4/22/2015.
 */
public enum JobStatus {
    InProgress(2), Failed(1), Completed(4), Open(0);

    private final int statusCode;

    JobStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode()
    {
        return statusCode;
    }
}
