import java.io.IOException;

/**
 * A class for the main function that runs WebClient.
 * @author Malak Sadek
 */
public class WebClientMain {

    private static int connectionPort;
    private static String hostName;

    /**
     * The main function to run the client.
     * It takes in the host name and port number to send requests on
     * @param args - host name & port number
     */
    public static void main(String[] args) {
        //Makes sure the host name and port number are supplied
        if (args.length < 2) {
            System.out.println("usage: java WebClientMain port_number host_name");
            System.exit(1);
        } else {

            try {
                connectionPort = Integer.parseInt(args[0]);
                hostName = args[1];

                WebClient c = new WebClient();

            } catch (NumberFormatException nfe) {
                System.out.print("Invalid port!\nusage: java WebClientMain port_number host_name");
                System.exit(1);
            } catch (IOException e) {
                System.out.print("Cannot connect to host!\nusage: java WebClientMain port_number host_name");
                System.exit(1);
            }
        }
    }

    /**
     * Getter method for connectionPort.
     * @return - connectionPort
     */
    public static int getConnectionPort() {
        return connectionPort;
    }

    /**
     * Getter method for hostName.
     * @return - hostName
     */
    public static String getHostName() {
        return hostName;
    }
}
