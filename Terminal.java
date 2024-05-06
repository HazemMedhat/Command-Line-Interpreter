import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Terminal {
    Parser parser;

    private List<String> commandHistory = new ArrayList<>();

    public Terminal(Parser parser) {
        this.parser = parser; // Use the parser passed from Main
    }

    public void ls() {
        String currentDirectory = System.getProperty("user.dir");
        File directory = new File(currentDirectory);

        if (directory.exists() && directory.isDirectory()) {
            String[] files = directory.list();
            if (files != null) {
                for (String file : files) {
                    System.out.println(file);
                }
            }
        } else {
            System.out.println("can not list the contents of the current directory.");
        }
    }

    public void ls_r() {
        String currentDirectory = System.getProperty("user.dir");
        File directory = new File(currentDirectory);

        if (directory.exists() && directory.isDirectory()) {
            String[] files = directory.list();
            if (files != null) {
                // Sort the files in reverse order
                List<String> fileList = Arrays.asList(files);
                Collections.reverse(fileList);

                for (String file : fileList) {
                    System.out.println(file);
                }
            }
        } else {
            System.out.println("can not list the contents of the current directory.");
        }
    }

    public void rm(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid ");
            return;
        }

        String fileName = args[0];
        File file = new File(fileName);

        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                System.out.println("Removed file: " + fileName);
            } else {
                System.out.println("Failed to remove the file: " + fileName);
            }
        } else {
            System.out.println("file does not exist in this directory: " + fileName);
        }
    }

    public void cat(String[] args) {
        if (args.length == 1) {
            // Display the content of a single file
            String fileName = args[0];
            File file = new File(fileName);

            if (file.exists() && file.isFile()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println("failed to read the file: " + e.getMessage());
                }
            } else {
                System.out.println("file dose not exist: " + fileName);
            }
        } else if (args.length == 2) {
            // Concatenate the content of two files
            String firstFileName = args[0];
            String secondFileName = args[1];
            File firstFile = new File(firstFileName);
            File secondFile = new File(secondFileName);

            if (firstFile.exists() && firstFile.isFile() && secondFile.exists() && secondFile.isFile()) {
                try (BufferedReader reader1 = new BufferedReader(new FileReader(firstFile));
                     BufferedReader reader2 = new BufferedReader(new FileReader(secondFile))) {
                    String line;
                    while ((line = reader1.readLine()) != null) {
                        System.out.println(line);
                    }
                    while ((line = reader2.readLine()) != null) {
                        System.out.println(line);
                    }
                } catch (IOException e) {
                    System.out.println("failed to read the file: " + e.getMessage());
                }
            } else {
                System.out.println("wrong with input files.");
            }
        } else {
            System.out.println("Invalid");
        }
    }

    public void echo(String[] args) {
        if (args.length > 0) {
            String message = String.join(" ", args);
            System.out.println(message);
        } else {
            System.out.println("error occurred");
        }

    }

    public void pwd() {
        String currentDirectory = System.getProperty("user.dir");
        System.out.println(currentDirectory);
    }

    public void cd() {
        String homeDirectory = System.getProperty("user.home");
        System.setProperty("user.dir", homeDirectory);
        System.out.println("Changed directory to home: " + homeDirectory);
    }

    public void cd(String[] args) {
        if (args.length == 1) {
            String path = args[0];
            if (path.equals("..")) {
                // Case 2: Change to the parent directory
                String currentDirectory = System.getProperty("user.dir");
                File currentDir = new File(currentDirectory);
                String parentDirectory = currentDir.getParent();
                if (parentDirectory != null) {
                    System.setProperty("user.dir", parentDirectory);
                    System.out.println("Changed directory to: " + parentDirectory);
                } else {
                    System.out.println("this directory is the root .");
                }
            } else {
                File newDirectory = new File(path);

                if (newDirectory.isAbsolute()) {
                    // Case 3: Change to an absolute path
                    if (newDirectory.isDirectory()) {
                        System.setProperty("user.dir", newDirectory.getAbsolutePath());
                        System.out.println("Changed directory to: " + newDirectory.getAbsolutePath());
                    } else {
                        System.out.println("Invalid directory: " + path);
                    }
                } else {
                    // Case 3: Change to a relative path
                    File currentDir = new File(System.getProperty("user.dir"));
                    File resolvedDir = new File(currentDir, path);

                    if (resolvedDir.isDirectory()) {
                        System.setProperty("user.dir", resolvedDir.getAbsolutePath());
                        System.out.println("Changed directory to: " + resolvedDir.getAbsolutePath());
                    } else {
                        System.out.println("Invalid directory: " + path);
                    }
                }
            }
        } else {
            System.out.println("Invalid");
        }
    }

    public void touch(String[] args) {
        if (args.length == 1) {
            String filePath = args[0];

            File file = new File(filePath);

            try {
                if (file.createNewFile()) {
                    System.out.println("File created: " + file.getAbsolutePath());
                } else {
                    System.out.println("File already exists: " + file.getAbsolutePath());
                }
            } catch (IOException e) {
                System.out.println("error while creating the file: " + e.getMessage());
            }
        } else {
            System.out.println("error occurred");
        }
    }

    public void mkdir(String[] args) {
        if (args.length > 0) {
            for (String arg : args) {
                File newDirectory = new File(arg);

                if (!newDirectory.isAbsolute()) {
                    // If the path is not absolute, it's relative to the current directory
                    newDirectory = new File(System.getProperty("user.dir"), arg);
                }

                if (newDirectory.mkdirs()) {
                    System.out.println("Directory created: " + newDirectory.getAbsolutePath());
                } else {
                    System.out.println("Directory already exists or an error occurred: " + newDirectory.getAbsolutePath());
                }
            }
        } else {
            System.out.println("error occurred");
        }
    }

    public void cp(String[] args) {
        if (args.length == 2) {
            String sourcePath = args[0];
            String destinationPath = args[1];

            File sourceFile = new File(sourcePath);
            File destinationFile = new File(destinationPath);

            if (!sourceFile.exists()) {
                System.out.println("Source file does not exist: " + sourcePath);
            } else if (destinationFile.exists()) {
                System.out.println("Destination file already exists: " + destinationPath);
            } else {
                try {
                    Files.copy(sourceFile.toPath(), destinationFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("File copied from " + sourcePath + " to " + destinationPath);
                } catch (IOException e) {
                    System.out.println("Error while copying the file: " + e.getMessage());
                }
            }
        } else {
            System.out.println("error occurred");
        }
    }

    public void cpR(String[] args) {
        if (args.length == 2) {
            String sourceDirPath = args[0];
            String destinationDirPath = args[1];

            Path sourcePath = Paths.get(sourceDirPath);
            Path destinationPath = Paths.get(destinationDirPath);

            if (!Files.exists(sourcePath) || !Files.isDirectory(sourcePath)) {
                System.out.println("Source directory does not exist: " + sourceDirPath);
            } else if (Files.exists(destinationPath) && !Files.isDirectory(destinationPath)) {
                System.out.println("Destination is not a directory: " + destinationDirPath);
            } else {
                try {
                    Files.walkFileTree(sourcePath, new SimpleFileVisitor<Path>() {
                        public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                            Path relativePath = sourcePath.relativize(dir);
                            Path targetPath = destinationPath.resolve(relativePath);

                            Files.createDirectories(targetPath);
                            return FileVisitResult.CONTINUE;
                        }
                        public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                            Path relativePath = sourcePath.relativize(file);
                            Path targetPath = destinationPath.resolve(relativePath);

                            Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
                            return FileVisitResult.CONTINUE;
                        }
                    });

                    System.out.println("Directory copied from " + sourceDirPath + " to " + destinationDirPath);
                } catch (IOException e) {
                    System.out.println("Error while copying the directory: " + e.getMessage());
                }
            }
        } else {
            System.out.println("error occurred");
        }
    }

    public void wc(String[] args) {
        if (args.length == 1) {
            String filePath = args[0];

            try {
                BufferedReader reader = new BufferedReader(new FileReader(filePath));
                int lines = 0;
                int words = 0;
                int characters = 0;

                String line;
                while ((line = reader.readLine()) != null) {
                    lines++;
                    words += line.split("\\s+").length;
                    characters += line.length();
                }

                reader.close();

                System.out.println(lines + " " + words + " " + characters + " " + filePath);
            } catch (IOException e) {
                System.out.println("Error while processing the file: " + e.getMessage());
            }
        } else {
            System.out.println("error occurred");
        }
    }

    public void history() {
        int index = 1;
        for (String command : commandHistory) {
            System.out.println(index + " " + command);
            index++;
        }
        if (commandHistory.size()==0){
            System.out.println("There is no history");
        }
    }


    //This method will choose the suitable command method to be called
    public void chooseCommandAction(){
        if("pwd".equals(parser.getCommandName())){
            pwd();
            commandHistory.add("pwd");
        }

        else if("echo".equals(parser.getCommandName())){
            echo(parser.getArgs());
            commandHistory.add("echo");
        }

        else if("cd".equals(parser.getCommandName())){
            if (parser.args.length == 0) {
                cd();
            } else {
                cd(parser.getArgs());
            }
            commandHistory.add("cd");
        }

        else if("1s".equals(parser.getCommandName())){
            pwd();
            ls();
            commandHistory.add("1s");
        }
        else if("1s-r".equals(parser.getCommandName())){
            pwd();
            ls_r();
            commandHistory.add("1s-r");
        }

        else if("touch".equals(parser.getCommandName())){
            touch(parser.getArgs());
            commandHistory.add("touch");
        }

        else if("cp".equals(parser.getCommandName())){
            cp(parser.getArgs());
            commandHistory.add("cp");
        }
        else if("cp-r".equals(parser.getCommandName())){
            cpR(parser.getArgs());
            commandHistory.add("cp-r");
        }

        else if("rm".equals(parser.getCommandName())){
            rm(parser.getArgs());
            commandHistory.add("rm");
        }

        else if("cat".equals(parser.getCommandName())){
            cat(parser.getArgs());
            commandHistory.add("cat");
        }

        else if("wc".equals(parser.getCommandName())){
            wc(parser.getArgs());
            commandHistory.add("wc");
        }

        else if("mkdir".equals(parser.getCommandName())){
            mkdir(parser.getArgs());
            commandHistory.add("mkdir");
        }

        else if("history".equals(parser.getCommandName())){
            history();
        }

    }

}