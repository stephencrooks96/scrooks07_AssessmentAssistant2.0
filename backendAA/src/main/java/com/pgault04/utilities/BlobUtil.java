package com.pgault04.utilities;

import java.util.Base64;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;

public class BlobUtil {

    public static SerialBlob baseToBlob(String base64) throws SQLException {
        String newBase64 = base64.substring(22);
        byte[] bytes = Base64.getDecoder().decode(newBase64);
        return new SerialBlob(bytes);
    }

    public static String blobToBase(Blob blob) throws SQLException {
        byte[] bytes;
        String base = null;
        if (blob != null) {
            bytes = blob.getBytes(1, (int) blob.length());
            base = Base64.getEncoder().encodeToString(bytes);
        }
        return base;
    }
}
