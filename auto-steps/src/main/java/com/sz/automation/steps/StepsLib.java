package com.sz.automation.steps;

import net.thucydides.core.annotations.Step;

/**
 * @author Senson S.Kritchker
 * Mar 02, 2021
 */
public class StepsLib {

    @Step("Step - Verification for String")
    public boolean stepVerifyString(String expected, String actual) {
        stepReport(String.format("Expected - %s | Actual - %s", expected, actual));
        return expected.equals(actual);
    }

    @Step("Step - Verification for Integer")
    public boolean stepVerifyInteger(Integer expected, Integer actual) {
        stepReport(String.format("Expected - %s | Actual - %s", expected, actual));
        return expected.equals(actual);
    }

    @Step("Step - Verification for Boolean")
    public boolean stepVerifyBoolean(Boolean expected, Boolean actual) {
        stepReport(String.format("Expected - %s | Actual - %s", expected, actual));
        return expected.equals(actual);
    }

    @Step("Step - Report: [{0}]")
    public void stepReport(String message) {
    }
}