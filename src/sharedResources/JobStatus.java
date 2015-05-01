package sharedResources;

/**
 * Created by Krishna on 4/22/2015.
 */
public enum JobStatus {
    UPDATED(4), COMPLETED(3), ASSIGNED(2), FAILED(1), OPEN(0);

    private final int statusCode;

    JobStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode()
    {
        return statusCode;
    }
}
