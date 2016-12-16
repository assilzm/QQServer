package exceptions

/**
 *
 * @author: Assilzm
 * create: 10:34.
 * description:
 */
class NoSuchRestTypeException extends Exception {

    NoSuchRestTypeException(String msg) {
        super(msg)
    }

    NoSuchRestTypeException(String msg, Throwable cause) {
        super(msg, cause)
    }

    NoSuchRestTypeException(Throwable cause) {
        super(cause)
    }
}
