package application;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;

import application.FileManager;
import java.util.ArrayList;
import java.util.List;

public class main 
{

	public static void main(String[] args) throws IOException 
	{
		FileManager fileManager = new FileManager();
		Analyser analyser = new Analyser();
		File Repo = fileManager.downloadFile();
        List<String> javaFiles = fileManager.findJavaFiles(Repo);
        System.out.print("-----------------------");
        for (String filePath : javaFiles)
        {
            System.out.println(filePath);
        }
        System.out.println("-----------------------");
        for (String filePath : javaFiles) 
        {
        	System.out.println("Sınıf: " + analyser.getFileName(filePath));
        	int javadoc = analyser.findJavadoc(filePath);
        	int comment = analyser.findComment(filePath);
        	int pureCodeLine = analyser.findPureCodeLine(filePath);
        	int LOC = analyser.findLOC(filePath);
        	int functions = analyser.findFunctions(filePath);
        	float deviation = analyser.calculateDeviationOfComment(javadoc, comment, functions, pureCodeLine);
        	System.out.println("Javadoc Satır Sayısı: " + javadoc);
        	System.out.println("Yorum Satır Sayısı: " + comment);
        	System.out.println("LOC: " + LOC);
        	System.out.println("Fonksiyon Sayısı: " + functions);
        	System.out.println("Yorum Sapma Yüzdesi: %" + deviation);
        	System.out.println("----------------------------");
        }
        
        

	
		fileManager.deleteFile(Repo);

	}
}
