import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

// To run program, ensure you are in the Project1 directory before running these commands
// To compile files: javac -d bin src/*.java
// To run program: java -cp bin Main

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<File> ballotFiles = new ArrayList<File>();
        File file;
        if (args.length == 0) {
            ballotFiles = ElectionMain.promptFileName();
        }
        else {
            for (String filename : args) {
                file = new File(filename);
                if (!file.exists()) {
                    System.out.println("'" + filename + "'" +  " does not exist");
                    // file = ElectionMain.promptFileName();
                }
                else {
                    if (ballotFiles.contains(file)) {
                        System.out.println("'" + filename + "'" + " was already added.");
                    }
                    ballotFiles.add(file);
                }
            }
        }
        if (ballotFiles.size() > 0) {
            System.out.println(ElectionMain.startElection(ballotFiles));
        }
        else {
            System.out.println("No valid ballot files were entered, restart the program after checking file spelling and location.");
        }
    }
}