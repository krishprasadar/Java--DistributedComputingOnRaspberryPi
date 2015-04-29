package sharedResources;

/**
 * Created by Krishna on 4/22/2015.
 */
public enum JobStatus {
    Updated(4), Completed(3), InProgress(2), Failed(1), Open(0);

    private final int statusCode;

    JobStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode()
    {
        return statusCode;
    }
}
