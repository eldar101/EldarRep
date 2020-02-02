// basic method for extension of name exceptions, in order to validate user input oin the phone book
package q2;

public class IllegalNameException extends Exception {

    private static final long serialVersionUID = 1L;

    public IllegalNameException() {
        super();
    }

    public IllegalNameException(String message) {
        super(message);
    }
} // end of IllegalNameException
