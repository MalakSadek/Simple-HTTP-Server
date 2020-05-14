import java.io.File;

/**
 * A class for HEAD requests, it extends the abstract Request class.
 * HeadRequest does not override the prepareResponse() method in Request since it just returns the header
 * @author Malak Sadek
 */
class HeadRequest extends Request {
    HeadRequest(String[] requestComponents, String requestURL, File resource) {
        super(requestComponents, requestURL, resource);
    }
}
