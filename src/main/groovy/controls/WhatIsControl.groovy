package controls

import utils.FileUtils

import java.util.regex.Matcher

/**
 *
 * @author: Assilzm
 * create: 2016-12-10 10:49.
 * description:
 */
class WhatIsControl {

    final static File CONFIG_FILE= new File("whatis.properties")


    static String getDescription(String keyword) {
        Properties properties=FileUtils.getProperties(CONFIG_FILE)
        return properties.get(keyword.toUpperCase())
    }


}