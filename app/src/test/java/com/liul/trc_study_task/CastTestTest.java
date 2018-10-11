package com.liul.trc_study_task;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class CastTestTest {

    private CastTest castTest;

    @Before
    public void setUp() throws Exception {
        castTest = new CastTest();
    }

    @Test
    public void testCast() {
        assertEquals(castTest.testCast(),1);
    }
}