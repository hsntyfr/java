package application;
import java.util.Scanner;
import java.io.File;
import application.FileManager;
import java.util.ArrayList;
import java.util.List;

public class main {

	public static void main(String[] args) {
		FileManager fileManager = new FileManager();
		Analyser analyser = new Analyser();
		File Repo = fileManager.downloadFile();
        List<String> javaFiles = fileManager.findJavaFiles(Repo);
        System.out.print("-----------------------");
        for (String filePath : javaFiles) {
            System.out.println(filePath);
        }

		fileManager.findJavaFiles(Repo);
		
		
		
		
		
		
		fileManager.deleteFile(Repo);

	}

}
