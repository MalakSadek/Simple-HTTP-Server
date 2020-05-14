import java.io.File;
import java.io.FileNotFoundException;

/**
 * A class for the RequestHandler that splits up the request and collects its information.
 * @author Malak Sadek
 */
class RequestHandler {

    private String requestType, requestURL;
    private String[] requestComponents;

    RequestHandler(String request) {
        //This breaks up the request into components and assigns them
        this.requestComponents = request.split(" ");
        requestType = requestComponents[0];
        requestURL = requestComponents[1];
        requestURL = requestURL.substring(requestURL.indexOf("/") + 1);
    }

    /**
     * Returns the split up request components.
     * @return requestComponents
     */
    public String[] processRequest() {
        return requestComponents;
    }

    /**
     * Getter function for requestType.
     * @return requestType
     */
    public String getRequestType() {
        return requestType;
    }

    /**
     * Getter function for requestURL.
     * @return requestURL
     */
    public String getRequestURL() {
        return requestURL;
    }

    /**
     * This method makes a GetRequest object.
     * @param resource - the specified file in the client's request
     * @return - the created GetRequest object
     * @throws FileNotFoundException
     */
    public GetRequest makeGetRequest(File resource) throws FileNotFoundException {
        return new GetRequest(requestComponents, requestURL, resource);
    }

    /**
     * This method makes a HeadRequest object.
     * @param resource - the specified file in the client's request
     * @return - the created HeadRequest object
     */
    public HeadRequest makeHeadRequest(File resource) {
        return new HeadRequest(requestComponents, requestURL, resource);
    }

    /**
     * This method makes a DeleteRequest object.
     * @param resource - the specified file in the client's request
     * @return - the created DeleteRequest object
     */
    public DeleteRequest makeDeleteRequest(File resource) {
        return new DeleteRequest(requestComponents, requestURL, resource);
    }

    /**
     * This method makes a PutPostRequest object.
     * @param resource - the specified file in the client's request
     * @return - the created PutPostRequest object
     */
    public PutPostRequest makePutPostRequest(File resource) {
        return new PutPostRequest(requestComponents, requestURL, resource);
    }
}
