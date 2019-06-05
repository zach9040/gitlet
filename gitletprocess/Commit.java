package gitletprocess;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;

public class Commit extends ValidObject implements Serializable {
    private static final String direct = "./gitlet";
    private static String commitDate;
    private static String commitMessage;
    private static String parent;
    private static String parent2;
    private static HashMap<String, String> mapping;
    private static HashSet<Blob> contents;

    public Commit(String date, String message, String par, HashMap<String, String> map, String par2) {
        commitDate = date;
        commitMessage = message;
        parent = par;
        mapping = map;
        parent2 = par2;
        contents = null;
    }


}
