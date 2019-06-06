package gitletprocess;

import java.io.File;
import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;

import static gitletprocess.Start.convToBytes;

public class Commit implements Serializable {
    private final String direct = ".gitlet/";
    private String commitDate;
    private String commitMessage;
    private Commit parent;
    private Commit parent2;
    private HashSet<Blob> contents;

    public Commit(String date, String message, Commit par, Commit par2) {
        commitDate = date;
        commitMessage = message;
        parent = par;
        parent2 = par2;
        File stageProcess = new File(".gitlet/stageProcess");
        File[] files = stageProcess.listFiles();
        contents = new HashSet<>();
    }

    public String SHA1() {
        try {
            String input = commitDate + commitMessage + parent.toString() + parent2.toString();
            // getInstance() method is called with algorithm SHA-1
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            for (Blob b : contents) {
                hashtext += b.SHA1();
            }
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            // return the HashText
            System.out.println(hashtext);
            return hashtext;
        }

        // For specifying wrong message digest algorithms
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public int hashCode() {
        return (this.SHA1() + "commit").hashCode();
    }

    public boolean equals(Object o) {
        return true;
    }

    public void setParent(Commit newParent) {
        parent = newParent;
    }


}