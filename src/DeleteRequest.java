import java.io.File;

/**
 * A class for DELETE requests, it extends the abstract Request class.
 * @author Malak Sadek
 */
class DeleteRequest extends Request {
    DeleteRequest(String[] requestComponents, String requestURL, File resource) {
        super(requestComponents, requestURL, resource);
    }

    /**
     * This method deletes the specified resource and returns an appropriate response based on whether it was able to do so.
     * @return response - to be sent to the client
     */
    @Override
    String prepareResponse() {
        String response;

        if (resource.delete()) {
            response = "HTTP/1.1 204 No Content\n";
        } else {
            response = "HTTP/1.1 404 Not Found\n";
        }

        return response;
    }
}
