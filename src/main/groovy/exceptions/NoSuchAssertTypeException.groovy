package exceptions

/**
 *
 * @author: Assilzm
 * create: 15:14.
 * description:
 */
class NoSuchAssertTypeException extends Exception {

    NoSuchAssertTypeException(String msg) {
        super(msg)
    }

    NoSuchAssertTypeException(String msg, Throwable cause) {
        super(msg, cause)
    }

    NoSuchAssertTypeException(Throwable cause) {
        super(cause)
    }
}

