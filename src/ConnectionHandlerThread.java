
import java.io.File;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.InputStreamReader;
import java.io.DataOutputStream;
import java.net.Socket;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A class for the server thread, created to handle concurrent clients.
 * @author Malak Sadek
 */
public class ConnectionHandlerThread extends Thread {

    //This keeps track of which client a thread is handling (0-4)
    private int threadNumber;

    //This holds the file referenced in the request
    private File resource;

    //These are the request string from the client and the response string sent back to the client
    private String request, response;

    //This is used to read the client's request
    private BufferedReader brClient;

    //This is used to write in the log file
    private PrintWriter pwLog;
    private FileWriter fw;

    //This is used to send the response back to the client across the socket
    private OutputStream os;

    //This holds the bytes for images requested
    private byte[] imageData;

    //This splits up the client requests and handles it
    private RequestHandler handler;

    /**
     * This creates a new thread to service a client.
     * @param conn is the socket connecting between the server thread & client
     * @param threadNumber is the current index number for a given thread (0-4)
     * @throws IOException
     */
    ConnectionHandlerThread(Socket conn, int threadNumber) throws IOException {
        this.threadNumber = threadNumber;

        brClient = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        os = new DataOutputStream(conn.getOutputStream());

        //Information on how to write to a text file obtained from:
        //https://www.homeandlearn.co.uk/java/write_to_textfile.html and
        //https://stackoverflow.com/questions/4269302/how-do-you-append-to-a-text-file-instead-of-overwriting-it-in-java
        fw = new FileWriter(WebServerMain.getServiceDirectory() + "/log_file.txt", true);
        pwLog = new PrintWriter(fw);
    }

    /**
     * This method is automatically called when the thread is started.
     */
    @Override
    public void run() {
        super.run();
        serviceClient();
    }

    /**
     * This class handles all the logic the thread does, one step at a time.
     * It reads the client request, breaks it up and processes it, identifies its type, sends a response, logs it, and then cleans up
     */
    private void serviceClient() {
        try {
            receiveRequest();
            handleRequest();
            identifyRequest(handler);
            sendResponse();
            logRequest();
            cleanUp();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method reads the request sent by the client over the socket.
     * If something goes wrong, this is flagged as a server-side error
     * @throws IOException
     */
    private void receiveRequest() throws IOException {
        try {
            request = brClient.readLine();
            System.out.println(threadNumber + ": received request: " + request);
        }
        catch (Exception e) {
            response = "HTTP/1.1 500 Internal Server Error\n";
            os.write(response.getBytes());
            os.flush();
            System.out.println("Cannot service request from client " + threadNumber + " ...");
            cleanUp();
        }
    }

    /**
     * This method initializes the request handler which then splits up the request to collect information about it.
     * It also initializes the resource file specified by the client in the request
     */
    private void handleRequest() {
        System.out.println("Processing request...");
        handler = new RequestHandler(request);
        resource = new File((WebServerMain.getServiceDirectory() + handler.getRequestURL()).replace("\"", ""));
    }

    /**
     * This method checks that the request is valid and then creates the appropriate request type object.
     * It will also respond if a GET, DELETE, or HEAD method is requesting a resource that does not exist,
     * or if a PUT or POST method is attempting to place a file that is already on the server directory
     * @param handler
     * @throws IOException
     */
    private void identifyRequest(RequestHandler handler) throws IOException {
        //Requests will always at least have a type and file
        if (handler.processRequest().length < 2) {
            response = "HTTP/1.1 400 Bad Request\n";
        } else {

            //If the request is GET, DELETE or HEAD, if the resource doesn't not exist, an error page is displayed
            if ((!handler.getRequestType().equals("PUT")) && (!handler.getRequestType().equals("POST"))) {
                if (!resource.exists()) {
                    returnErrorPage();
                }
                //If the resource exists, the appropriate request type object is created (which inherit from the
                //abstract request class)
                else {
                    switch(handler.getRequestType()) {
                        case "GET":

                            GetRequest getReq = handler.makeGetRequest(resource);
                            response = getReq.prepareResponse();

                            //Images requested or embedded within html files are handled separately
                            if (response.contains("image")) {
                                imageData = getReq.handleImage();
                            }
                            break;

                        case "HEAD":

                            HeadRequest headReq = handler.makeHeadRequest(resource);
                            response = headReq.prepareResponse();
                            break;

                        case "DELETE":

                            DeleteRequest deleteReq = handler.makeDeleteRequest(resource);
                            response = deleteReq.prepareResponse();
                            break;

                        default:
                            //Any other kind of request
                            response = "HTTP/1.1 501 Not Implemented\n";
                            break;
                    }
                }
            } else {
                //If the request is a POST or PUT and the resource already exists, an error response is sent
                if (resource.exists()) {
                    response = "HTTP/1.1 409 Resource already exists\n";
                } else {
                    PutPostRequest putPostReq = handler.makePutPostRequest(resource);
                    response = putPostReq.prepareResponse();
                }
            }
        }
    }

    /**
     * This method displays an error page when a resource requested does not exist.
     * Can be seen by typing localhost:12345/anything.html into a browser
     * It mimics a GET request on the file 404.html
     * @throws IOException
     */
    private void returnErrorPage() throws IOException {
        GetRequest getReq = handler.makeGetRequest(new File(WebServerMain.getServiceDirectory() + "/404.html"));
        response = getReq.prepareResponse();
    }

    /**
     * This method sends the created response back to the client over the socket.
     * @throws IOException
     */
    private void sendResponse() throws IOException {

            System.out.println(threadNumber + ": responding to request with: " + response);

            //This includes the header and any content text
            os.write(response.getBytes(), 0, response.getBytes().length);

            //Images are then sent separately if they exist
            if (imageData != null) {
                os.write(imageData);
            }

            os.flush();
    }

    /**
     * This method logs the details of requests and responses in a text file called log.txt.
     */
    private void logRequest() {

        //https://www.javatpoint.com/java-get-current-date
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();

        pwLog.println("User: " + threadNumber + " made the request " + request + " on " + dtf.format(now) + " and got the response: " + response);
    }

    /**
     * This method closes all buffers cleanly and is called at the end of servicing the client or during catch clauses if an exception occurs.
     * @throws IOException
     */
    private void cleanUp() throws IOException {

        fw.close();
        brClient.close();
        os.close();
        pwLog.close();

        if (WebServer.getNumberOfConnections() > 0) {
            WebServer.setNumberOfConnections(WebServer.getNumberOfConnections() - 1);
        } else {
            WebServer.setNumberOfConnections(0);
        }
    }
}
