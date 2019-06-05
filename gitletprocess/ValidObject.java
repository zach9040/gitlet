package gitletprocess;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public abstract class ValidObject implements Serializable {

    public String SHA1() {
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutput out = new ObjectOutputStream(bos);
            out.writeObject(this);

            byte[] result = mDigest.digest(bos.toByteArray());
            StringBuffer sb = new StringBuffer();
            for (byte element : result) {
                sb.append(Integer.toString((element & 0xff) + 0x100, 16)
                        .substring(1));
            }

        } catch (NoSuchAlgorithmException | IOException n) {
            System.out.println("Something went wrong.");
        }
        return null;
    }

    public int hashCode() {
        if (this.getClass().equals(new Blob(null))) {
            return (this.SHA1() + "blob").hashCode();
        } else {
            return (this.SHA1() + "commit").hashCode();
        }
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        ValidObject obj = (ValidObject) o;
        return this.hashCode() == obj.hashCode();
    }
}
