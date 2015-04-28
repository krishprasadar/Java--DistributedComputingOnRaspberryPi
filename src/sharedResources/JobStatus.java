package sharedResources;


import java.io.Serializable;
import java.rmi.Remote;

public enum JobStatus implements Serializable,Remote {

    Failed(1), Completed(2), Open(0);

    private final int statusCode;
    private static final long serialVersionUID = -464196277362659007L;

    JobStatus(int statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCode()
    {
        return statusCode;
    }
}
