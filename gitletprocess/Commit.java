package gitletprocess;

import java.io.Serializable;
import java.util.HashMap;

public class Commit implements Serializable {
    private String message;
    private String dateTime;
    private HashMap<String, Blob> fileToBlob;
    private Commit parent1;
    private Commit parent2;
    private int dir;

    public Commit(String msg, String dT, HashMap<String, Blob> fTb, Commit par1, Commit par2) { // for merges
        message = msg;
        dateTime = dT;
        fileToBlob = fTb;
        parent1 = par1;
        parent2 = par2;
    }

    public Commit(String msg, String dT, HashMap<String, Blob> fTB, Commit par1) {
        message = msg;
        dateTime = dT;
        fileToBlob = fTB;
        parent1 = par1;
        parent2 = null;
    }

    public HashMap<String, Blob> getFiles() {
        return fileToBlob;
    }

    @Override
    public String toString() {
        System.out.println("===");
        System.out.println("commit " + getDirNum());
        System.out.println("Date: " + dateTime);
        System.out.println(message);
        return "";
    }

    public int getDirNum() {
        return dateTime.hashCode();
    }

    public Commit getParent1() {
        return parent1;
    }

    public String getMessage() {
        return message;
    }


}
