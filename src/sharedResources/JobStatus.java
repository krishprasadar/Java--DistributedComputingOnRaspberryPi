package sharedResources;


import java.io.Serializable;
import java.rmi.Remote;

public enum JobStatus implements Serializable {

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
