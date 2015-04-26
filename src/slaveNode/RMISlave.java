package slaveNode;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * Created by raghavbabu on 4/25/15.
 */
public class RMISlave {

    private static RMISlave instance = null;
    private Service service = null;

    private RMISlave() throws RemoteException
    {
        super();
    }

    public static void main(String[] args)
    {
        RMISlave slave = RMISlave.getInstance();
        slave.createServer();
    }

    /**
     * Create the server.
     */
    public void createServer()
    {
        if (System.getSecurityManager() == null)
        {
            System.out.println("Creating new security manager");
            System.setSecurityManager(new SecurityManager());
        }


        try
        {
            service = new ServiceImpl(Service.ServicePort);

            Registry r;
            try
            {
                r = LocateRegistry.createRegistry(Service.RMIRegistryPort);
            }
            catch (java.rmi.server.ExportException ex)
            {
                r = LocateRegistry.getRegistry(Service.RMIRegistryPort);
            }

            System.out.println("Service attempts rebind of registry");

            r.rebind(Service.SORT_SERVICE, service);
            System.out.println("Server bound in registry");
            System.out.println("Press Ctrl-C to exit.");
        }
        catch (java.rmi.server.ExportException ee)
        {
            // Cannot get RMI port.
            System.err.println("Server  cannot use RMI port " + Service.RMIRegistryPort + " as it is already in use, check that you are not running another instance of the server.");
            ee.printStackTrace(System.err);
            System.exit(-1);
        }
        catch (Exception e)
        {
            System.err.println("Server err: " + e.getMessage());
            System.exit(-1);
        }
    }


    public static RMISlave getInstance()
    {
        if (instance == null)
        {
            try
            {
                instance = new RMISlave();

            }
            catch (RemoteException ex)
            {
                System.err.println("RMISlave:getInstance() - " + ex.getMessage());
            }
        }

        return instance;
    }

}
