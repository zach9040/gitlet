package gitletprocess;

import java.io.*;
import java.util.HashSet;

public class Center {
    private static final String GITLET = ".gitlet/";

    public static void initCommand(CommitTree repo) {
        File dir = new File(".gitlet");
        if (!dir.exists() || repo == null) {
            dir.mkdir(); //create .gitlet
            repo = new CommitTree();
            Center.saveRepo(repo);
        } else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        }
    }


    public static boolean checkInitialized() {
        File check = new File(".gitlet");
        return check.exists();
    }

    public static CommitTree retrieveRepo() {
        File repo = new File(".gitlet/repo.ser");
        if (repo.exists()) {
            try{
                FileInputStream fileIn = new FileInputStream(repo);
                ObjectInputStream objectIn = new ObjectInputStream(fileIn);
                CommitTree repository = (CommitTree) objectIn.readObject();
                fileIn.close();
                objectIn.close();
                return repository;
            } catch (IOException e){
                System.out.println("IOException while loading repo.");
            } catch (ClassNotFoundException e){
                System.out.println("ClassNotFoundException while loading repo");
            }
        }
        return null;
    }

    public static void saveRepo(CommitTree repo) {
        if(repo == null){
            return;
        }
        try{
            File commitTreeFile = new File(GITLET + "repo.ser");
            FileOutputStream fileOut = new FileOutputStream(commitTreeFile);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(repo);
            objectOut.close();
            fileOut.close();
        }catch (IOException e){
            System.out.println("IOException while saving repo.");
        }
    }

    public static void main(String[] args) {
        String command = "";
        String file = "";

        if (args.length > 0) {
            command = args[0];
        } else {
            System.out.println("Please enter a command");
            return;
        }
        if (args.length > 1) {
            file = args[1];
        }
        CommitTree repository = retrieveRepo();
        switch (command) {
            case "init" :
                initCommand(repository);
                break;
            case "add":
                if (args.length == 2) {
                    repository.addToStage(file);
                    saveRepo(repository);
                } else {
                    System.out.println("Incorrect amount of operands.");
                }
                break;
            case "commit":
                if (args.length == 2 && file != null) {
                    repository.makeCommit(file);
                    saveRepo(repository);
                }
                break;
            case "rm":
                if (args.length == 2 && file != null) {
                    repository.remove(file);
                }
                break;
            case "log":
                repository.printHistory();
                break;
            case "global-log":
                repository.printAllHistory();
                break;
            case "find":
                if (args.length == 2 && file != null) {
                    repository.findCommits(file);
                }
                break;
            case "branch":
                if (args.length == 2 && file != null) {
                    repository.createBranch(file);
                }
                break;
            case "rm-branch":
                if (args.length == 2 && file != null) {
                    repository.removeBranch(file);
                }
                break;
            case "status":
                repository.printStatus();
                break;
            case "reset":
                break;
            case "checkout":
                break;
            case "merge":
                break;
            default :
                System.out.println("Please enter a real command.");
                return;

        }
    }
}
