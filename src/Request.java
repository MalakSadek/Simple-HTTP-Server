import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * This is an abstract class for all types of requests.
 * Different request type classes extend this abstract class
 * @author Malak Sadek
 */
abstract class Request {

    protected String[] requestComponents;
    private String requestURL;
    protected File resource;

    Request(String[] requestComponents, String requestURL, File resource) {
        this.requestComponents = requestComponents;
        this.requestURL = requestURL;
        this.resource = resource;
    }

    /**
     * The default for this method is to create a header for the response.
     * Headers are composed of a status line, date, content type line, and content length type
     * @return - response to be sent to client
     * @throws IOException
     */
    String prepareResponse() throws IOException {

        String status, contentType, contentLength, date, response;

        //Date, obtained from:
        //https://www.javatpoint.com/java-get-current-date
        LocalDateTime now = LocalDateTime.now();
        date = "Date: ";
        date = date + now + " GMT";

        //Status code
        if (resource.getPath().contains("404")) {
            status = "HTTP/1.1 404 Not Found";
        } else {
            status = "HTTP/1.1 200 OK";
        }

        //Content Type
        contentType = "Content-Type: ";
        if (requestURL.contains("jpg") || requestURL.contains("JPG") || requestURL.contains("png") || requestURL.contains("PNG") || requestURL.contains("gif") || requestURL.contains("GIF")) {
            contentType = contentType + "image/" + resource.getName().substring(resource.getName().indexOf(".") + 1);
        } else {
            contentType = contentType + "text/" + resource.getName().substring(resource.getName().indexOf(".") + 1);
        }

        //Content Length
        contentLength = "Content-Length: ";
        contentLength = contentLength + resource.length();

        response = status + "\n" + date + "\n" + contentType + "\n" + contentLength + "\n" + "\r\n";
        return response;
    }
}
