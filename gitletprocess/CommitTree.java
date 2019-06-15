package gitletprocess;

import java.io.File;
import java.io.Serializable;
import java.util.*;

public class CommitTree implements Serializable {
    private HashMap<String, Branch> branchList;
    private Branch currentBranch;
    public ArrayList<String> stageList;
    public ArrayList<String> removeList;
    private static final String GITLET = ".gitlet/";
    private static final String stageProcess = ".gitlet/stageProcess/";
    private static final String initialDate = "Thu Jan 1 00:00:00 UTC 1970";

    public CommitTree() {
        branchList = new HashMap<>();
        Commit firstCommit = new Commit("initial commit", initialDate, null, null);
        File first = new File(GITLET + firstCommit.getDirNum());
        first.mkdir();
        Branch master = new Branch(firstCommit, "master", this);
        branchList.put("master", master);
        currentBranch = master;
        stageList = new ArrayList<>();
        removeList = new ArrayList<>();
    }

    public void addToStage(String fileName) {
        File add = new File(fileName);
        File check = new File(GITLET + "stageProcess");
        if (!add.exists()) {
            System.out.println("File does not exist");
        } else if (!Center.checkInitialized()) {
            System.out.println("This directory is not a gitlet directory.");
        } else {
            if (!check.exists()) {
                check.mkdir();
            }
            byte[] copyArray = Utils.readContents(add);
            String filePath = GITLET + "stageProcess/" + fileName;
            File copy = new File(filePath);
            if (copy.exists()) {
                if (copyArray.equals(Utils.readContents(copy))) {
                    removeFromStage(filePath, add);
                    return;
                }
            }
            stageList.add(fileName);
            Utils.writeContents(copy, Utils.readContents(add));
        }
    }

    public void removeFromStage(String fileName, File remove) {
        stageList.remove(fileName);
        remove.deleteOnExit();
    }

    public void makeCommit(String msg) {
        currentBranch.addToBranch(msg);
    }

    public void remove(String fileName) {
        String filePath = stageProcess + fileName;
        File file = new File(filePath);
        if (file.exists() && stageList.contains(fileName)) {
            removeFromStage(fileName, file);
        }
        if (currentBranch.hasFile(fileName)) {
            removeList.add(fileName); //haven't implemented removeList
            File fileDir = new File(fileName);
            if (fileDir.exists()) {
                fileDir.deleteOnExit();
            }
        }
    }

    public void printHistory() {
        currentBranch.printHistory();
    }

    public void printAllHistory() {
        HashSet<Commit> commitSet = new HashSet<>();
        for (Branch b : branchList.values()) {
            Commit copy = b.head;
            while (copy != null) {
                if (!commitSet.contains(copy)) {
                    System.out.println(copy);
                    commitSet.add(copy);
                }
                copy = copy.getParent1();
            }
        }
    }

    public void findCommits(String msg) {
        HashSet<Commit> commitSet = new HashSet<>();
        boolean fileExist = false;
        for (Branch b : branchList.values()) {
            Commit copy = b.head;
            while (copy != null) {
                if (copy.getMessage().equals(msg)) {
                    if (!commitSet.contains(copy)) {
                        fileExist = true;
                        System.out.println(copy);
                        commitSet.add(copy);
                    }
                }
                copy = copy.getParent1();
            }
        }
        if (!fileExist) {
            System.out.println("Found no commit with that message.");
        }
    }

    public void printStatus() {
        System.out.println("=== Branches ===");
        System.out.println("*" + currentBranch.getTitle());
        for (String b : branchList.keySet()) {
            if (!currentBranch.getTitle().equals(b)) {
                System.out.println(b);
            }
        }
        System.out.println();
        System.out.println("=== Staged Files ===");
        for (String files : stageList) {
            System.out.println(files);
        }
        System.out.println();
        System.out.println("=== Removed Files ===");
        for (String files : removeList) {
            System.out.println(files);
        }
        System.out.println();
        System.out.println("=== Modifications Not Staged Fo Commit");
        //dont know what currently
        System.out.println();
        System.out.println("=== Untracked Files");
    }

    public void switchBranch(String branchName) {
        if (currentBranch.getTitle().equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            return;
        }
        if (!branchList.containsKey(branchName)) {
            System.out.println("The branch does not exist");
            return;
        }
        HashMap<String, Blob> fileMap = currentBranch.head.getFiles();
        for (String name : fileMap.keySet()) {
            Utils.writeContents(new File(name),fileMap.get(name).getContents());
        }
        Branch checkout = branchList.get(branchName);
        for (String file : fileMap.keySet()) {
            if (!checkout.head.getFiles().containsKey(file)) {
                File remove = new File(file);
                if (remove.exists()) {
                    remove.deleteOnExit();
                }
            }
        }
        currentBranch = checkout;
        stageList.clear();
    }

    public void removeBranch(String branchName) {
        if (!branchList.containsKey(branchName)) {
            System.out.println("A branch with that name does not exist.");
            return;
        }
        for (Branch b : branchList.values()) {
            if (b.getTitle().equals(branchName)) {
                if (!branchName.equals(currentBranch.getTitle())) {
                    branchList.remove(branchName);
                } else {
                    System.out.println("Can't remove the current branch.");
                }
            }
        }
    }

    public void createBranch(String branchName) {
        if (branchList.containsKey(branchName)) {
            System.out.println("A branch with that name already exists.");
            return;
        }
        Branch newBranch = new Branch(currentBranch.head, branchName, this);
        branchList.put(branchName, newBranch);
        System.out.println(branchList.keySet());
    }

    public Commit findCommit(String commitID) {
        for (Branch b : branchList.values()) {
            Commit copy = b.head;
            while (copy != null) {
                if (Integer.toString(copy.getDirNum()).equals(commitID)) {
                    return copy;
                }
                copy = copy.getParent1();
            }
        }
        return null;
    }

    public void overwriteFile(String check, String fileName) {
        overwriteFile(check, fileName, null);
    }

    public void overwriteFile(String check, String fileName, String commitID) {
        if (!check.equals("--")) {
            System.out.println("Not a valid command.");
            return;
        }
        Commit usedCommit = currentBranch.head;
        if (commitID != null) {
            usedCommit = findCommit(commitID);
            if (usedCommit == null) {
                System.out.println("No commit with this ID exxists.");
            }
        }
        byte[] copyArray = null;
        String newFilePath = "";
        for (String files : usedCommit.getFiles().keySet()) {
            if (fileName.equals(files)) {
                String dirPath = GITLET + usedCommit.getDirNum() + "/" + fileName;
                File file = new File(dirPath);
                if (file.exists()) {
                    newFilePath = files;
                    copyArray = Utils.readContents(file);
                }
            }
        }
        if (newFilePath != "" && copyArray != null) {
            Utils.writeContents(new File(newFilePath), copyArray);
        } else {
            System.out.println("File does not exist in this commit.");
        }
    }

    public void resetTree(String commitID) {
        Commit commit = findCommit(commitID);
        if (commit == null) {
            System.out.println("No commit with that ID exists");
            return;
        }
        //NEED TO TAKE INTO ACCOUNT UNTRACKED FILES AS WELL
        for (String files : currentBranch.head.getFiles().keySet()) {
            overwriteFile("--", files);
            if (!commit.getFiles().containsKey(files)) {
                File remove = new File(GITLET + commit.getDirNum());
                if (remove.exists()) {
                    remove.deleteOnExit();
                }
            }
        }
        currentBranch.head = commit;
        stageList.clear();
    }

    public void mergeBranches(String branchName) {
        if (branchName.equals(currentBranch.getTitle())) {
            System.out.println("Cannot merge branch with itself.");
        } else if (!stageList.isEmpty() || !removeList.isEmpty()) {
            System.out.println("You have uncommitted changes.");
        } else if (!branchList.containsKey(branchName)) {
            System.out.println("A branch of that name doesn't exist.");
        } else if (false) {
            //used to check for untracked files
        } else {
            Branch merge = branchList.get(branchName);
            if (merge.split.equals(currentBranch.head)) {
                System.out.println("Given branch is an ancestor of current branch.");
                return;
            }
            
        }
    }
}
