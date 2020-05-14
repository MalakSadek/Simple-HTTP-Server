import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * A class for the Server. It accepts connections from clients and then creates threads and assigns them to clients to handle their requests.
 * It can be run by using java WebServerMain /directory 12345
 * @author Malak Sadek
 */
class WebServer {
    //The number of clients that are connected to the server
    private static int numberOfConnections = 0;
    //The server can only handle a maximum of 5 clients at a time
    private static final int THREADLIMIT = 5;

    WebServer() throws IOException {

        ServerSocket s = new ServerSocket(WebServerMain.getListeningPort());
        System.out.println("Server started ... listening on port " + WebServerMain.getListeningPort() + " ...");

        //Server keeps listening for new connections
        while (true) {
            if (numberOfConnections < THREADLIMIT) {

                //Accept any new connections continuously and assign each one to a connection handler thread
                Socket connection = s.accept();

                System.out.println("Server got new connection request from " + connection.getInetAddress());

                //Create new handler for this connection
                try {
                    ConnectionHandlerThread ch = new ConnectionHandlerThread(connection, numberOfConnections);

                    //Start handler thread
                    ch.start();

                    //Increase the number of connections by one
                    numberOfConnections++;

                } catch (IOException io) {
                    //When a client closes, decrement the number of connections by one
                    System.out.println("Connection number" + numberOfConnections + " has faced a technical issue.");
                    if (numberOfConnections > 0) {
                        numberOfConnections--;
                    } else {
                        numberOfConnections = 0;
                    }
                }
            } else {
                System.out.println("Cannot accept more requests at the moment, please wait... ");
            }
        }
    }

    /**
     * Getter function for numberOfConnections.
     * @return - numberOfConnections
     */
    public static int getNumberOfConnections() {
        return numberOfConnections;
    }

    /**
     * Setter function for numberOfConnections.
     * @param numberOfConnections
     */
    public static void setNumberOfConnections(int numberOfConnections) {
        WebServer.numberOfConnections = numberOfConnections;
    }
}
