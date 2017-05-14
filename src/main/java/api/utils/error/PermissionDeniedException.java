package api.utils.error;

/**
 * Created by Vileven on 14.05.17.
 */
public class PermissionDeniedException extends Exception {
    public final String message;

    public PermissionDeniedException(String message) {
        this.message = message;
    }
}
