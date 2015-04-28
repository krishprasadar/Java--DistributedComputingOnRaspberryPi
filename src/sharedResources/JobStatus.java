package sharedResources;


import java.io.Serializable;

public enum JobStatus implements Serializable {
    Failed(1), Completed(2), Open(0);

    private final int statusCode;

    JobStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode()
    {
        return statusCode;
    }
}
