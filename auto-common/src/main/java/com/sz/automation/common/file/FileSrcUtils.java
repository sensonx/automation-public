package com.sz.automation.common.file;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;

/**
 * @author Senson S.Kritchker
 * May 10, 2021
 */
public class FileSrcUtils {

    private static final Logger logger = LogManager.getLogger(FileSrcUtils.class);

    public static boolean deleteDirectoryFiles(String path) {
        return deleteDirectoryFiles(new File(path));
    }

    public static boolean deleteDirectoryFiles(File directory) {
        logger.debug("Deleting Directory...");
        if (directory == null) {
            logger.error("Invalid Directory - NULL");
            return false;
        }
        try {
            logger.debug("Deleting all files of [{}]", directory.getAbsolutePath());
            FileUtils.cleanDirectory(directory);
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return false;
    }
}