package utils

/**
 *
 * @author: weiwei
 * create: 2016-12-10 15:14.
 * description:
 */
class FileUtils {


    static Properties getProperties(File file) {
        Properties properties = new Properties()
        FileInputStream inputFile = new FileInputStream(file)
        properties.load(inputFile)
        inputFile.close()
        return properties
    }
}
