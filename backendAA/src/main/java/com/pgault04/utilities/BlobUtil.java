package com.pgault04.utilities;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

/**
 * @author Paul Gault - 40126005
 * @since December 2018
 * Allows for conversion from Blob type to Base64 and back
 */
public class BlobUtil {

    /**
     * Converts from base64 String to Blob (binary encoding)
     *
     * @param base64 - the base64 string
     * @return the converted blob
     * @throws SQLException - thrown if string is not convertible
     */
    public static SerialBlob baseToBlob(String base64) throws SQLException {
        String newBase64 = base64.substring(22);
        byte[] bytes = Base64.getDecoder().decode(newBase64);
        return new SerialBlob(bytes);
    }

    /**
     * Converts from blob String to base64
     *
     * @param blob - the blob object
     * @return the base64 string
     * @throws SQLException - thrown if blob is not convertible
     */
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