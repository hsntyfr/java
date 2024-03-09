package application;
import java.io.File;
import java.util.regex.Matcher;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import java.nio.charset.StandardCharsets;

public class Analyser 
{
	public boolean isClassFile(String file) throws IOException
	{
		return false;

	}
	
	public int findLOC(String file)
	{
		int LOC = 0;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) 
        {
            while (reader.readLine() != null) 
            {
                LOC++;
            }
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }

		return LOC;
	}
	
	public int findPureCodeLine(String file) {
	    int pureCodeLine = 0;
	    boolean isCommentBlock = false;

	    try (BufferedReader reader = new BufferedReader(new FileReader(file))) 
	    {
	        String line;

	        while ((line = reader.readLine()) != null)
	        {
	            line = line.trim();
	            
	            if (line.isEmpty() || line.startsWith("//")) 
	            {
	                continue;
	            }

	            if (line.startsWith("/*")) 
	            {
	                isCommentBlock = true;
	                continue;
	            }

	            if (line.startsWith("/**")) 
	            {
	                isCommentBlock = true;
	                if (!line.endsWith("*/")) 
	                {
	                    continue;
	                }
	            }

	            if (line.endsWith("*/")) 
	            {
	                isCommentBlock = false;
	                continue;
	            }

	            if (!isCommentBlock) 
	            {
	                pureCodeLine++;
	            }
	        }
	    }
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    }

	    return pureCodeLine;
	}
	
	public int findFunctions(String file)
	{
		int functions = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) 
		{
            String line;

            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();

                if (line.startsWith("public") || line.startsWith("private") || line.startsWith("protected")) 
                {
                    if (line.contains("(") && line.contains(")")) 
                    {
                        functions++;
                    }
                }
            }
        }
		catch (IOException e) 
		{
            e.printStackTrace();
        }

		return functions;
	}
	public int findJavadoc(String file)
	{
		int javadoc = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) 
		{
            String line;
            boolean isCommentBlock = false;

            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();
                
                if (line.isEmpty())
                {
                    continue;
                }

                if (line.startsWith("/**")) 
                {
                    isCommentBlock = true;
                    javadoc++;
                    continue;
                }

                if (line.endsWith("*/")) 
                {
                    isCommentBlock = false;
                    javadoc++;
                    continue;
                } 
                
            }
        } 
		catch (IOException e) 
		{
            e.printStackTrace();
        }
		
		return javadoc;
	}
	public int findComment(String file)
	{
		int comment = 0;
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) 
		{
            String line;
            boolean isCommentBlock = false;

            while ((line = reader.readLine()) != null) 
            {
                line = line.trim();
                
                if (line.isEmpty())
                {
                    continue;
                }

                if (line.startsWith("//"))
                {
                    comment++;
                    continue;
                }

                if (line.startsWith("/*")) 
                {
                    isCommentBlock = true;
                    comment++;
                    continue;
                }

                if (line.endsWith("*/")) 
                {
                    isCommentBlock = false;
                    comment++;
                    continue;
                }

                if (isCommentBlock) 
                {
                    comment++;
                }
            }
        } 
		catch (IOException e) 
		{
            e.printStackTrace();
        }
		
		return comment;
	}

	public float calculateDeviationOfComment(int javadoc, int comment, int function, int line)
	{
		float f_javadoc = javadoc;
		float f_comment = comment;
		float f_function = function;
		float f_line = line;
		float deviation = 0;
		try
		{
			float YG = ((javadoc + comment) * 0.8f) / function;
			float YH = (line / function) * 0.3f;
			deviation = ((100 * YG) / YH - 100);
		}
		catch(ArithmeticException e)
		{
			
		}
		
		return deviation;
	}
	
	public String getFileName(String file)
	{
        String[] parts = file.split("/");
        String fileName = parts[parts.length - 1];
        
        return fileName;
    }
}

