package api.utils.error;

/**
 * Created by Vileven on 15.05.17.
 */
public class EntityNotFoundException extends Exception {
    public final String message;

    public EntityNotFoundException(String message) {
        this.message = message;
    }
}
