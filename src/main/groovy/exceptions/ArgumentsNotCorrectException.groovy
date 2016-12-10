package exceptions

/**
 *
 * @author: weiwei
 * create: 2016-11-22 14:46.
 * description:
 */
class ArgumentsNotCorrectException extends Exception {
    ArgumentsNotCorrectException(String msg) {
        super(msg)
    }

    ArgumentsNotCorrectException(String msg, Throwable cause) {
        super(msg, cause)
    }

    ArgumentsNotCorrectException(Throwable cause) {
        super(cause)
    }
}
