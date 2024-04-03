package project;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File;
import java.io.IOException;
import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
public class FileManager 
{
	private String getUrl() 
	{
		String url;
		System.out.print("İndirilecek repo url'sini giriniz: ");
		Scanner input = new Scanner(System.in);
        url = input.nextLine();
        String[] partsUrl = url.split("/");
        if (partsUrl[partsUrl.length - 1].equals("main") && partsUrl[partsUrl.length - 2].equals("tree"))
        {
        	partsUrl[partsUrl.length - 1] = "";
        	partsUrl[partsUrl.length - 2] = "";
        }
        url = String.join("/", partsUrl);
        System.out.println(url);
		return url;
	}

	public File downloadFile() 
	{
		String target = System.getProperty("user.dir") + "/file";
		try
		{
            ProcessBuilder processBuilder = new ProcessBuilder();
            processBuilder.command("git", "clone", getUrl(), target);

            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) 
            {
                System.out.println("Repo başarıyla indirildi.");
            } 
            else 
            {
                System.out.println("Repo indirme başarısız oldu.");
            }
        } 
		catch (IOException | InterruptedException e) 
		{
            e.printStackTrace();
        }
		File file = new File(target);
		return file;
	}
	
	public boolean deleteFile(File file) 
	{
		if (file.exists())
		{
            File[] files = file.listFiles();
            if (files != null) 
            {
                for (File filesa : files) 
                {
                    if (filesa.isDirectory()) 
                    {
                        deleteFile(filesa);
                    } else {
                        filesa.delete();
                    }
                }
            }
            file.delete();
            return true;
        }
		else 
		{
            System.out.println("Belirtilen dizin bulunamadı.");
            return false;

        }
	}
	
	public List<String> findJavaFiles(File directory)
	{
		List<String> javaFilesList = new ArrayList<>();
        findJavaFilesHelper(directory, javaFilesList);
        return javaFilesList;		
	}
	
	private static void findJavaFilesHelper(File directory, List<String> javaFilesList)
	{
        File[] files = directory.listFiles();
        if (files != null)
        {
            for (File file : files)
            {
                if (file.isDirectory()) 
                {
                    findJavaFilesHelper(file, javaFilesList);
                } 
                else if (file.getName().endsWith(".java") && !file.getName().startsWith("I")) 
                {
                	if(isJavaFile(file))
                    javaFilesList.add(file.getAbsolutePath());
                }
            }
        }
    }
	
	private static boolean isJavaFile(File file)
	{
		Pattern pattern = Pattern.compile("(?<=\\bclass\\s)\\w+");
		try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    return true;
                }
            }
            scanner.close();
        } 
		catch (FileNotFoundException e) 
		{
            e.printStackTrace();
        }
		return false;	
	}
	
}
	


