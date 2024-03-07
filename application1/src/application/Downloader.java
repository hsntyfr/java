package application;
import java.util.Scanner;
import java.io.File;
import java.io.IOException;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;


public class Downloader {
	private String getUrl() {
		System.out.print("İndirilecek repo url'sini giriniz: ");
		Scanner input = new Scanner(System.in);
		return input.nextLine();
	}
	
	public void downloadRepo() {
		String target = System.getProperty("user.dir") + "/file";
		try {
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("git", "clone", getUrl(), target);

            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Repo başarıyla indirildi.");
            } 
            else {
                System.out.println("Repo indirme başarısız oldu.");
            }
        } 
		catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
	}
		
	

}
