package gitletprocess;

import java.io.*;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class Start {
    private static String initialDate = "00:00:00UTC,01/1970";
    private static HashSet<String> commandList = new HashSet<>();

    private static void setCommandList() {
        if (commandList.isEmpty()) {
            commandList.add("init");
            commandList.add("add");
            commandList.add("commit");
            commandList.add("rm");
            commandList.add("log");
            commandList.add("global-log");
            commandList.add("status");
            commandList.add("checkout");
            commandList.add("branch");
            commandList.add("find");
        }
    }

    private static boolean isCommandValid(String command) {
        return commandList.contains(command);
    }

    private static void initCommand() {
        File dir = new File(".gitlet");
        if (!dir.exists()) {
            //Commit newCommit = new Commit(initialDate, "initial commit", "master", null, null);
            dir.mkdir(); // need to figure out how to do commits
        } else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        }

    }

    private static byte[] convToBytes(File file) {
        try {
            byte[] copyArray = new byte[(int) file.length()];
            FileInputStream f = new FileInputStream(file);
            f.read(copyArray); //read file into bytes[]
            f.close(); //replace with method later
            return copyArray;
        } catch (IOException f) {
            System.out.println("File does not exist.");
        }
        return null;
    }

    private static String getPrefix(String name) {
        int cutoff = name.indexOf('.');
        return name.substring(0, cutoff);
    }

    private static String getSuffix (String name) {
        int cutoff = name.indexOf('.');
        return name.substring(cutoff);
    }

    private static void copyFile(File source, File destination) throws IOException {
        InputStream is = null;
        OutputStream os = null;
        try {
            String prefix = getPrefix(source.getName());
            String suffix = getSuffix(source.getName());
            if (!destination.exists()) {
                File.createTempFile(prefix, suffix, destination);
            }
            is = new FileInputStream(source);
            os = new FileOutputStream(destination + "/" + prefix + suffix);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            is.close();
            os.close();
        }
    }

    private static void addCommand(String file) {
        File add = new File(file);
        if (!add.exists()) {
            System.out.println("File does not exist");
        } else if (!new File(".gitlet").exists()) {
            System.out.println("This directory is not a gitlet directory");
        } else {
            if (!(new File(".gitlet/stageProcess").exists())) {
                File stageProcess = new File(".gitlet/stageProcess");
                stageProcess.mkdir();
            } //create new staging area if it does not already exist
            File check = new File(".gitlet/stageProcess");
            if (check.exists()) {
                try {
                    copyFile(add, check);
                } catch (IOException i) {
                    System.out.println("File does not exist");
                }
            }

        }
    }

    private static void commitCommand() {

    }

    public static void main(String[] args) {
        String command = "";
        String file = "";
        setCommandList();
        if (args.length == 0) {
            System.out.println("Please enter a command");
            return;
        }
        if (args.length > 0) {
            command = args[0];
        }
        if (args.length > 1) {
            file = args[1];
        }
        if (isCommandValid(command)) {
            switch (command) {
                case "init" :
                    initCommand();
                    break;
                case "add":
                    addCommand(file);
                    break;
                case "commit":
                    commitCommand();
                    break;
                case "rm":
                case "log":
                case "global-log":
                case "find":
                case "branch":
                case "status":
                case "checkout":
            }
        } else {
            System.out.println("Please enter a real command");
            return;
        }
    }
}
