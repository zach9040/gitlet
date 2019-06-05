package gitletprocess;

import org.wildfly.swarm.config.IO;

import java.io.*;
import java.util.HashMap;
import java.util.HashSet;

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
            Commit newCommit = new Commit(initialDate, "initial commit", "master", null, null);
            dir.mkdir(); // need to figure out how to do commits
        } else {
            System.out.println("A Gitlet version-control system already exists in the current directory.");
        }

    }

    private static void addCommand(String file) {
        File add = new File(file);
        if (!add.exists()) {
            System.out.println("File does not exist");
        } else if (!new File(".gitlet").exists()) {
            System.out.println("This directory is not a gitlet directory");
        } else {
            try {
                byte[] bytesArray = new byte[(int) add.length()];

                FileInputStream fis = new FileInputStream(file);
                fis.read(bytesArray); //read file into bytes[]
                fis.close();

                Blob newFile = new Blob(bytesArray);

                //add to staging area in .gitlet which should exist

            } catch (IOException f) {
                System.out.println("File does not exist.");
            }
        }
    }

    private static void commitCommand() {

    }

    public static void main(String[] args) {
        String command = "";
        String file = "";
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
                case "add":
                    addCommand(file);
                case "commit":
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
