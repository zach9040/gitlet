package gitletprocess;

import java.io.File;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;


public class Branch implements Serializable {
    private String title;
    public Commit head;
    private CommitTree repo;
    public Commit split;
    private static final String GITLET = ".gitlet/";
    private static final String stageProcess = ".gitlet/stageProcess/";

    public Branch(Commit commit, String title, CommitTree repo) {
        this.title = title;
        head = commit;
        this.repo = repo;
        split = commit;
    }

    public void addToBranch(String message) { //commit
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        String newDate = dateFormat.format(date); // get date
        File stage = new File(stageProcess);
        HashMap<String, Blob> blobList = new HashMap<>();
        for (String files : repo.stageList) {
            blobList.put(files, new Blob(Utils.readContents(new File(stageProcess + files))));
        }

        Commit par = head;
        head = new Commit(message, newDate, blobList, par);

        String dirPath = GITLET + head.getDirNum();
        File commitFolder = new File(dirPath);
        commitFolder.mkdir();
        for (String name : blobList.keySet()) {
            File next = new File(dirPath + "/" + name);
            Utils.writeContents(next, blobList.get(name).getContents());
        }
        File[] files = stage.listFiles();
        for (File f : files) {
            f.deleteOnExit();
        }
        repo.stageList.clear();
    }

    public boolean hasFile(String fileName) {
        for (String files : head.getFiles().keySet()) {
            if (fileName.equals(files)) {
                return true;
            }
        }
        return false;
    }

    public void printHistory() {
        Commit copy = head;
        while (copy != null) {
            copy.toString();
            copy = copy.getParent1();
        }
    }

    public String getTitle() {
        return title;
    }

}
