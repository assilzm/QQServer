package exceptions

/**
 *
 * @author: weiwei
 * create: 11:27.
 * description:
 */
class JsonParseException extends Exception{
    JsonParseException(String msg) {
        super(msg)
    }

    JsonParseException(String msg, Throwable cause) {
        super(msg, cause)
    }

    JsonParseException(Throwable cause) {
        super(cause)
    }
}