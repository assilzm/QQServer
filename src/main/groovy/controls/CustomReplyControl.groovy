package controls

import utils.FileUtils

import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardWatchEventKinds
import java.nio.file.WatchService

/**
 *
 * @author: assilzm
 * create: 2017-1-14 10:30.
 * description:
 */
class CustomReplyControl {
    final static File CONFIG_FILE = new File("replies.properties")

    static Properties properties = new Properties()

    static {
        load()
    }

    static void load() {
        properties = FileUtils.getProperties(CONFIG_FILE)
    }


    static String getReply(String message) {
        println properties
        return properties.get(message)
    }



}
