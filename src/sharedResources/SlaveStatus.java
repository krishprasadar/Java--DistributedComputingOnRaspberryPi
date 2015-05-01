package sharedResources;

/**
 * Created by Krishna on 4/22/2015.
 */
public enum SlaveStatus {

    OPEN(1), PUSH(5), PULL(5), INPROGRESS(2), FAILED(3), FULL(4);

    private final int statusCode;

    SlaveStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode()
    {
        return statusCode;
    }
}
