package com.pgault04.utilities;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TestBlobUtil {

    @Test
    public void testBaseToBlobAndBlobToBase() throws SQLException {
        String base = "01234567891011121314151617181920212223";
        SerialBlob blob = BlobUtil.baseToBlob(base);
        assertEquals(base.substring(22), BlobUtil.blobToBase(blob));
    }
}
