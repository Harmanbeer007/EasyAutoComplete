package harmanbeer007.easylibrary.easyautocompleteview.exceptions;

/**
 * Created by harman.
 */
public class NoModelDefinedException extends Exception {

    public NoModelDefinedException() {
        super("No Model defined for AutoCompleteView");

    }

    public NoModelDefinedException(String message, Throwable cause) {
        super(message, cause);
        // TODO Auto-generated constructor stub
    }
}
