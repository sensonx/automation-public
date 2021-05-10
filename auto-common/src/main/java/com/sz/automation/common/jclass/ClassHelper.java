package com.sz.automation.common.jclass;

import com.sz.automation.common.enums.SysVar;

/**
 * @author Senson S.Kritchker
 * May 10, 2021
 */
public class ClassHelper {

    public static String getProjectPath() {
        String projectPath = System.getProperty(SysVar.USER_DIR);
        return projectPath.substring(0, projectPath.lastIndexOf("\\"));
    }
}