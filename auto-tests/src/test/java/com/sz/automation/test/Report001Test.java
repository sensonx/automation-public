package com.sz.automation.test;

import com.sz.automation.steps.StepsLib;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * @author Senson S.Kritchker
 * Mar 02, 2021
 */
@RunWith(SerenityRunner.class)
public class Report001Test {
    
    @Steps
    StepsLib stepsLib;

    @Test
    public void testVerification01() {
        Assert.assertTrue(stepsLib.stepVerifyString("Comma", "Comma"));
    }

    @Test
    public void testVerification02() {
        Assert.assertTrue(stepsLib.stepVerifyInteger(124986, 3456882));
    }

    @Test
    public void testVerification03() {
        Assert.assertTrue(stepsLib.stepVerifyBoolean(true, (1 == 1)));
    }

    @Test
    public void testVerification04() {
        Assert.assertTrue(stepsLib.stepVerifyString("Comma", "Domma"));
    }
}
