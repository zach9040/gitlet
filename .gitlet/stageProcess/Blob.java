package gitletprocess;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Blob {

    private byte[] contents;

    public Blob(byte[] c) {
        contents = c;
    }

    public byte[] getContents() {
        return contents;
    }

    public String SHA1() {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(contents);
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public int hashCode() {
        return (this.SHA1() + "blob").hashCode();
    }

    public boolean equals(Object o) {
        return true;
    }
}