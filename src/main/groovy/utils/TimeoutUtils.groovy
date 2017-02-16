package utils

import org.apache.log4j.Logger

/**
 *
 * @author: assilzm
 * create: 2017-2-15 17:49.
 * description:
 */
class TimeoutUtils {

    private final static Logger logger = LogUtils.getLogger(TimeoutUtils)


    void run(Closure closure){
        Timer t = new Timer()
        t.runAfter(0) {
            try{
                closure.run()
            }catch (Exception e) {
                Thread.currentThread().interrupt()
                logger.debug(e.getMessage())
            }
            t.cancel()
        }
    }
}
