package com.pgault04.utilities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestChartUtil {

    @Test
    public void testChartColourGenerate() {
        String colour = ChartUtil.chartColourGenerate();
        assertTrue(colour.length() <= 22);
        assertEquals("rgb(", colour.substring(0, 4));
    }
}
