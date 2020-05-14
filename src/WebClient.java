
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * A class for WebClient, used to test specific requests.
 * It is run using java WebClientMain localhost 12345, and then the request is inputted
 * @author Malak Sadek
 */
class WebClient {
    private Socket s;
    private final BufferedReader brUser, brServer;
    private final PrintWriter pw;
    private String request;

    WebClient() throws IOException {
        this.s = new Socket(WebClientMain.getHostName(), WebClientMain.getConnectionPort());

        System.out.println("Client connected to " + WebClientMain.getHostName() + " on port " + WebClientMain.getConnectionPort() + ".");

        //Reading from user
        brUser = new BufferedReader(new InputStreamReader(System.in));

        //Writing to server
        pw = new PrintWriter(new OutputStreamWriter(s.getOutputStream()));

        //Reading from server
        brServer = new BufferedReader(new InputStreamReader(s.getInputStream()));

        runClient();
    }

    /**
     * This method handles all the functionalities performed by the client.
     * It collects the request from the user, sends it to the server, then prints out the server's repsonse
     */
    private void runClient() {
        try (brUser; pw; brServer) {

            getUserRequest();
            sendRequestToServer();
            receiveResponseFromServer();

        } catch (Exception e) { // exit cleanly for any Exception (including IOException, SocketTimeoutException, DisconnectedException)
            System.out.println("An error occurred on connection to " + WebClientMain.getHostName() + " on port " + WebClientMain.getConnectionPort() + ". " + e.getMessage());
            System.out.println("Client: ... cleaning up and exiting ... ");
        }
    }

    /**
     * This method collects the user's request.
     * @throws IOException
     */
    private void getUserRequest() throws IOException {
        System.out.println("Enter your request:");
        request = brUser.readLine();
        System.out.println("Collected input...");
    }

    /**
     * This method sends the request to the server.
     */
    private void sendRequestToServer() {
        pw.println(request);
        pw.flush();
        System.out.println("Sent to request to server...");
    }

    /**
     * This method prints out the server's response.
     */
    private void receiveResponseFromServer() {

        Object[] content = brServer.lines().toArray();
        String response = "";

        for (Object o : content) {
            response = response.concat(o.toString() + "\n");
        }

        System.out.println("Server says: " + response);

   }
}
