package exceptions

/**
 *
 * @author: Assilzm
 * create: 11:22.
 * description:
 */
class RestErrorException  extends Exception{
    RestErrorException(String msg) {
        super(msg)
    }

    RestErrorException(String msg, Throwable cause) {
        super(msg, cause)
    }

    RestErrorException(Throwable cause) {
        super(cause)
    }
}
