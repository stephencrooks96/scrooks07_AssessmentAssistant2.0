package com.pgault04.utilities;

import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

public class BlobUtil {

    public static SerialBlob baseToBlob(String base64) throws SQLException, Base64DecodingException {
        com.sun.org.apache.xml.internal.security.Init.init();
        String newBase64 = base64.substring(22);

        byte[] bytes = Base64.decode(newBase64);
        return new SerialBlob(bytes);
    }

    public static String blobToBase(Blob blob) throws SQLException {
        com.sun.org.apache.xml.internal.security.Init.init();
        byte[] bytes;
        String base = null;
        if (blob != null) {
            bytes = blob.getBytes(1, (int) blob.length());
            base = Base64.encode(bytes);
        }
        return base;
    }
}
