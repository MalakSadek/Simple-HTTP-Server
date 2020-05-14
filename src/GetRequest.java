import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;

/**
 * A class for GET requests, it extends the abstract Request class.
 * @author Malak Sadek
 */
class GetRequest extends Request {

    //This is used to read from the specified file to send the contents to the client
    private BufferedReader brCopy;

    GetRequest(String[] requestComponents, String requestURL, File resource)
            throws FileNotFoundException {
        super(requestComponents, requestURL, resource);
        brCopy = new BufferedReader(new FileReader(resource));
    }

    /**
     * This method calls the default prepareResponse() method in the abstract Request class to create a header, then creates the response body.
     * @return response - to be sent to client
     */
    @Override
    String prepareResponse() throws IOException {
        //Calls the default prepareResponse() in Request to create the header
        StringBuilder response;
        response = new StringBuilder(super.prepareResponse());

        //For text content, the content is read from the file and appended to the response
        if (!response.toString().contains("image")) {
            try {
                brCopy = new BufferedReader(new FileReader(resource));
                String line;
                while ((line = brCopy.readLine()) != null) {
                    response.append(line).append("\n");
                }
                brCopy.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return response.toString();
    }

    /**
     * This method handles GET requests for images as they need to be read as bytes, not lines.
     * @return - imageData is the image's byte data
     */
    byte[] handleImage() {
        byte[] imageData = null;
        try {
            imageData = Files.readAllBytes(resource.toPath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageData;
    }
}
