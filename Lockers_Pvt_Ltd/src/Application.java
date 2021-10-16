import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Application {
    private Set<String> filepaths;

    public Application() {
        this.filepaths = retrieveFilePaths();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice = 0;
        char cont = 'y';
        Application app = new Application();

        while(true) {
            app.printMenuOptions();
            System.out.print("\nEnter your choice: ");
            try {
                choice = sc.nextInt();
                app.execute(choice);
            }
            catch (Exception e) {
                System.out.print("\nIncorrect choice");
            }
            System.out.print("\nDo you wish to continue (y/n): ");
            cont = sc.next().charAt(0);
            switch(cont) {
                case 'y':
                    continue;
                case 'n':
                    System.out.print("\nBye");
                    app.storeFilePaths();
                    System.exit(0);
                default:
                    System.out.print("\nIncorrect choice, exiting the program");
                    app.storeFilePaths();
                    System.exit(0);
            }
        }
    }

    private void execute(int choice) {
        switch (choice) {
            case 1:
                viewAllFiles();
                break;
            case 2:
                createNewFile();
                break;
            case 3:
                insertRecordsInFile();
                break;
            case 4:
                searchFile();
                break;
            case 5:
                deleteFile();
                break;

        }
    }

    private void insertRecordsInFile() {
        Scanner sc = new Scanner(System.in);
        String filepath;
        String contentToAppend;
        System.out.print("\nEnter your filename (absolute path): ");
        try {
            filepath = sc.next();
            if(!this.filepaths.contains(filepath)) {
                System.out.print("\nIncorrect filename or file doest not exist");
                return;
            }
            sc.nextLine();
            System.out.print("\nEnter content to append: ");
            contentToAppend = sc.nextLine();
            Files.write(Paths.get(filepath), contentToAppend.getBytes(), StandardOpenOption.APPEND);
            System.out.print("\nContent Appended successfully");
        } catch (IOException e) {
            System.out.print("\nUnable to append, check and try again");
        }
    }

    private void deleteFile() {
        Scanner sc = new Scanner(System.in);
        String filepath;

        System.out.print("\nEnter your filename (absolute path): ");
        try {
            filepath = sc.next();
            if(!this.filepaths.contains(filepath)) {
                System.out.print("\nIncorrect filename or file doest not exist");
                return;
            }
            File f = new File(filepath);
            f.delete();
            this.filepaths.remove(filepath);
            System.out.print("File deleted successfully");
           

        } catch (Exception e) {
            System.out.print("\nUnable to delete the file, check and try again");
        }
    }

    private void createNewFile() {
        Scanner sc = new Scanner(System.in);
        String filepath;

        System.out.print("\nEnter your filename (absolute path): ");
        try {
            filepath = sc.next();
            if(this.filepaths.contains(filepath)) {
                System.out.print("\nFile already exists");
            }
            File f = new File(filepath);
            f.getParentFile().mkdirs();
            f.createNewFile();
            this.filepaths.add(filepath);
            System.out.print("\nFile created successfully");
            
        } catch (IOException e) {
            System.out.print("\nUnable to create the file, check and try again");
        }

    }

    private void viewAllFiles() {
        System.out.print("\n" + this.filepaths.stream().sorted().collect(Collectors.joining("\n")));
    }

    private void searchFile() {
        Scanner sc = new Scanner(System.in);
        String filepath;

        System.out.print("\nEnter your filename (absolute path): ");
        try {
            filepath = sc.next();
            if (this.filepaths.contains(filepath)) {
                System.out.print("\nFile exists");
            }
            else {
                System.out.print("\nFile does not exist");
            }
           
        }
        catch (Exception ex) {
            System.out.print("\nSome error occurred, try again");
        }
    }

    private void printMenuOptions() {
        System.out.print(
                "\n1. View all files\n2. Create new file \n3. append records in file\n4. search file\n5. delete file");
    }

    private void storeFilePaths() {
        String content = this.filepaths.stream().sorted().collect(Collectors.joining("\n"));
        try {
            Files.deleteIfExists(Paths.get("filepath_store.txt"));
            Files.write(Paths.get("filepath_store.txt"), content.getBytes(), StandardOpenOption.CREATE);
        }
        catch (Exception e) {
            System.out.print("\nApp Internal: Unable to store the paths");
        }
    }

    private Set<String> retrieveFilePaths() {
        Set<String> filepaths = new HashSet<>();
        if(Files.exists(Paths.get("filepath_store.txt"))) {
            try (Stream<String> stream = Files.lines(Paths.get("filepath_store.txt"))) {
                stream.forEach(path->filepaths.add(path));
            } catch (IOException e) {
                System.out.print("\nApp Internal: Unable to retrieve the paths");
            }
        }
        return filepaths;
    }


}
