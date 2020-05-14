import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;

/**
 * A class for PUT & POST requests, it extends the abstract Request class.
 * Both achieve the same effect for this server
 * @author Malak Sadek
 */
class PutPostRequest extends Request {

    /**
     * This is the index where the content of the given file begins.
     * 0 - POST/PUT
     * 1 - /test.txt
     * 2 - text/txt
     * 3 - length
     * 4 - Beginning of file content to be posted
     */
    private static final int BODYSTARTINDEX = 4;

    PutPostRequest(String[] requestComponents, String requestURL, File resource) {
        super(requestComponents, requestURL, resource);
    }

    /**
     * This method overrides prepareMethod() in Request.
     * @return response - to be sent to client
     * @throws IOException
     */
    @Override
    String prepareResponse() throws IOException {

        String response;
        StringBuilder requestContent = new StringBuilder();

        //This collects the contents of the file to be created and then formats it
        for (int i = BODYSTARTINDEX; i < requestComponents.length; i++) {
            requestContent.append(requestComponents[i]).append(" ");
        }
        requestContent = new StringBuilder(requestContent.substring(0, requestContent.indexOf("HTTP")));
        requestContent = new StringBuilder(requestContent.toString().replace("\"", ""));
        requestContent = new StringBuilder(requestContent.toString().replace("\\", ""));
        requestContent.append("\n");

        //This creates the new specified file
        File newFile = new File(WebServerMain.getServiceDirectory() + resource.getName());
        if (newFile.createNewFile()) {
            if (resource.exists()) {

                //This prints the given contents into the newly created file
                System.out.println("Created file...");
                System.out.println("Created reader...");
                PrintWriter pwCopy = new PrintWriter(new BufferedWriter(new FileWriter(newFile)));
                System.out.println("Created writer...");

                pwCopy.println(requestContent);
                pwCopy.close();

                response = "HTTP/1.1 201 Created" + "\n" + "Content-Location: /" + resource.getName() + "\n";
            }
            //If the file cannot be created or found, an error response is sent
            else {
                response = "HTTP/1.1 404 Not Found\n";
            }
        } else {
            response = "HTTP/1.1 404 Not Found\n";
        }
        return response;
    }
}
