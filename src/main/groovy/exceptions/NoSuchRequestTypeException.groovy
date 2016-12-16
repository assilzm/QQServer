package exceptions

/**
 *
 * @author: Assilzm
 * create: 2016-9-21 20:52.
 * description:
 */
class NoSuchRequestTypeException extends Exception {

    NoSuchRequestTypeException(String msg) {
        super(msg)
    }

    NoSuchRequestTypeException(String msg, Throwable cause) {
        super(msg, cause)
    }

    NoSuchRequestTypeException(Throwable cause) {
        super(cause)
    }
}
