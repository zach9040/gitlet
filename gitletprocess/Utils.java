package gitletprocess;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Formatter;
import java.util.List;

class Utils {

    /* SHA-1 HASH VALUES. */

    /**
     * The length of a complete SHA-1 UID as a hexadecimal numeral.
     */
    static final int UID_LENGTH = 40;

    /**
     * Returns the SHA-1 hash of the concatenation of VALS, which may
     * be any mixture of byte arrays and Strings.
     */
    static String sha1(Object... vals) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            for (Object val : vals) {
                if (val instanceof byte[]) {
                    md.update((byte[]) val);
                } else if (val instanceof String) {
                    md.update(((String) val).getBytes(StandardCharsets.UTF_8));
                } else {
                    throw new IllegalArgumentException("improper type to sha1");
                }
            }
            Formatter result = new Formatter();
            for (byte b : md.digest()) {
                result.format("%02x", b);
            }
            return result.toString();
        } catch (NoSuchAlgorithmException excp) {
            throw new IllegalArgumentException("System does not support SHA-1");
        }
    }

    /**
     * Returns the SHA-1 hash of the concatenation of the strings in
     * VALS.
     */
    static String sha1(List<Object> vals) {
        return sha1(vals.toArray(new Object[vals.size()]));
    }

    /* FILE DELETION */

    /**
     * Deletes FILE if it exists and is not a directory.  Returns true
     * if FILE was deleted, and false otherwise.  Refuses to delete FILE
     * and throws IllegalArgumentException unless the directory designated by
     * FILE also contains a directory named .gitl`et.
     */
    static boolean restrictedDelete(File file) {
        if (!(new File(file.getParentFile(), ".gitlet")).isDirectory()) {
            throw new IllegalArgumentException("not .gitlet working directory");
        }
        if (!file.isDirectory()) {
            return file.delete();
        } else {
            return false;
        }
    }

    /**
     * Deletes the file named FILE if it exists and is not a directory.
     * Returns true if FILE was deleted, and false otherwise.  Refuses
     * to delete FILE and throws IllegalArgumentException unless the
     * directory designated by FILE also contains a directory named .gitlet.
     */
    static boolean restrictedDelete(String file) {
        return restrictedDelete(new File(file));
    }

    /* READING AND WRITING FILE CONTENTS */

    /**
     * Return the entire contents of FILE as a byte array.  FILE must
     * be a normal file.  Throws IllegalArgumentException
     * in case of problems.
     */
    static byte[] readContents(File file) {
        if (!file.isFile()) {
            throw new IllegalArgumentException("must be a normal file");
        }
        try {
            return Files.readAllBytes(file.toPath());
        } catch (IOException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }


    /**
     * Write the result of concatenating the bytes in CONTENTS to FILE,
     * creating or overwriting it as needed.  Each object in CONTENTS may be
     * either a String or a byte array.  Throws IllegalArgumentException
     * in case of problems.
     */
    static void writeContents(File file, Object... contents) {
        try {
            if (file.isDirectory()) {
                throw
                        new IllegalArgumentException("cannot overwrite directory");
            }
            BufferedOutputStream str =
                    new BufferedOutputStream(Files.newOutputStream(file.toPath()));
            for (Object obj : contents) {
                if (obj instanceof byte[]) {
                    str.write((byte[]) obj);
                } else {
                    str.write(((String) obj).getBytes(StandardCharsets.UTF_8));
                }
            }
            str.close();
        } catch (IOException | ClassCastException excp) {
            throw new IllegalArgumentException(excp.getMessage());
        }
    }

}
