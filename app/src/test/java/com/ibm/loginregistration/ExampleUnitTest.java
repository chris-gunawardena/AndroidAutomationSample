package com.ibm.loginregistration;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void should_be_valid_email() throws Exception {
        assertEquals(true, LoginFragment.isEmailValid("test@test.com"));
    }

    @Test
    public void should_be_invalid_when_no_at() throws Exception {
        assertEquals(false, LoginFragment.isEmailValid("testtest.com"));
    }


    @Test
    public void should_be_valid_with_single_quotes() throws Exception {
        assertEquals(true, LoginFragment.isEmailValid("o'conner@test.com"));
    }

    @Test
    public void should_be_invalid_when_special_chars() throws Exception {
        assertEquals(false, LoginFragment.isEmailValid("tes;ttest.com"));
    }
}