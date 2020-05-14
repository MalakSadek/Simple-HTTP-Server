import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * A class for the main that runs the server.
   @author Malak Sadek
 */
public class WebServerMain {

    //These are made static as many other classes access them (to create the resource folder for example)
    private static String serviceDirectory;
    private static int listeningPort;

    /**
     * The main function for running the server.
     * It takes in the service directory and the port the server will listen on
     * @param args - service directory & port number
     */
    public static void main(String[] args) {
        //Invalid command
        if (args.length < 2) {
            System.out.print("Usage: java WebServerMain <document_root> <port>");
            System.exit(1);
        } else {

            //Try-catch block with exception obtained from: https://www.baeldung.com/java-check-string-number
            try {
                listeningPort = Integer.parseInt(args[1]);

                //If statement obtained from: https://stackoverflow.com/questions/15571496/how-to-check-if-a-folder-exists
                if (Files.exists(Paths.get(args[0]))) {

                    //The server directory is assigned if its a valid path to a folder that exists
                    serviceDirectory = args[0];

                    //Run the server
                    WebServer s = new WebServer();
                } else {
                    System.out.print("Usage: java WebServerMain <document_root> <port>");
                    System.exit(1);
                }
            } catch (IOException e) {
                System.out.print("Usage: java WebServerMain <document_root> <port>");
                System.exit(1);
            }
        }
    }

    /**
     * Getter function for serviceDirectory.
     * @return - serviceDirectory
     */
    public static String getServiceDirectory() {
        return serviceDirectory;
    }

    /**
     * Getter function for listeningPort.
     * @return - listeningPort
     */
    public static int getListeningPort() {
        return listeningPort;
    }
}
