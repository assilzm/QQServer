package utils

import java.nio.charset.Charset

/**
 *
 * @author: Assilzm
 * create: 2016-12-10 15:14.
 * description:
 */
class FileUtils {


    static Properties getProperties(File file) {
        Properties properties = new Properties()
        FileInputStream inputFile = new FileInputStream(file)
        BufferedReader bf = new BufferedReader(new InputStreamReader(inputFile))
        properties.load(bf)
        bf.close()
        inputFile.close()
        return properties
    }
}
