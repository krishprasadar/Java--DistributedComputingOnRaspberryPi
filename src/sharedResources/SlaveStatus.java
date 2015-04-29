package sharedResources;

/**
 * Created by Krishna on 4/22/2015.
 */
public enum SlaveStatus {
    Active(1), InActive(2);

    private final int statusCode;

    SlaveStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode()
    {
        return statusCode;
    }
}
