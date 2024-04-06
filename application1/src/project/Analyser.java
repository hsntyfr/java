package project;
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
	public int findLOC(String file) 
	{
	    int LOC = 0;
	    BufferedReader reader = null;
	    
	    try 
	    {
	        reader = new BufferedReader(new FileReader(file));
	        while (reader.readLine() != null) 
	        {
	            LOC++;
	        }
	    }
	    catch (IOException e) 
	    {
	        e.printStackTrace();
	    } 
	    finally 
	    {
	        if (reader != null) 
	        {
	            try 
	            {
	                reader.close();
	            } 
	            catch (IOException e)
	            {
	                e.printStackTrace();
	            }
	        }
	    }
	    return LOC;
	}
	
	public int findPureCodeLine(String file)
	{
	    int pureCodeLine = 0;
	    boolean isCommentBlock = false;
	    BufferedReader reader = null;

	    try 
	    {
	        reader = new BufferedReader(new FileReader(file));
	        String line;

	        while ((line = reader.readLine()) != null)
	        {
	            line = line.trim();

	            if (line.contains("*/") && line.contains("/*") && !line.endsWith(";")) 
	            {
	                isCommentBlock = false;
	                continue;
	            }
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
	    finally 
	    {
	        if (reader != null) 
	        {
	            try 
	            {
	                reader.close();
	            }
	            catch (IOException e) 
	            {
	                e.printStackTrace();
	            }
	        }
	    }
	    return pureCodeLine;
	}

	public int findFunctions(String file) 
	{
	    int functions = 0;
	    BufferedReader reader = null;

	    try {
	        reader = new BufferedReader(new FileReader(file));
	        String line;

	        while ((line = reader.readLine()) != null) 
	        {
	            line = line.trim();

	            if (line.startsWith("public") || line.startsWith("private") || line.startsWith("protected") || line.startsWith("static")) 
	            {
	                if (line.contains("(") && line.contains(")") && !line.contains("new")) 
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
	    finally
	    {
	        if (reader != null) 
	        {
	            try 
	            {
	                reader.close();
	            } 
	            catch (IOException e)
	            {
	                e.printStackTrace();
	            }
	        }
	    }
	    return functions;
	}

	public int findJavadoc(String file) 
	{
	    int javadoc = 0;
	    BufferedReader reader = null;

	    try {
	        reader = new BufferedReader(new FileReader(file));
	        String line;
	        boolean inJavadoc = false;

	        while ((line = reader.readLine()) != null) 
	        {
	            line = line.trim();
	            if (line.startsWith("/**")) 
	            {
	                inJavadoc = true;
	            }
	            else if (line.startsWith("*/")) 
	            {
	                inJavadoc = false;
	            } 
	            else if (inJavadoc) 
	            {
	                javadoc++;
	            }
	        }
	    } 
	    catch (IOException e)
	    {
	        e.printStackTrace();
	    } 
	    finally 
	    {
	        if (reader != null) 
	        {
	            try {
	                reader.close();
	            } 
	            catch (IOException e) 
	            {
	                e.printStackTrace();
	            }
	        }
	    }
	    return javadoc;
	}

	public int findComment(String file)
	{
	    int comment = 0;
	    Pattern pattern = Pattern.compile("^\\s*//.*");
	    BufferedReader reader = null;
	    try
	    {
	        reader = new BufferedReader(new FileReader(file));
	        String line;
	        boolean isCommentBlock = false;
	        while ((line = reader.readLine()) != null)
	        {
	            line = line.trim();
	            //System.out.print("\n"+line);
	            Matcher matcher = pattern.matcher(line);

	            if (line.isEmpty()) 
	            {
	                continue;
	            }

	            if (line.contains("//"))
	            {
	                comment++;
	                continue;
	            }

	            if (line.startsWith("/*") && line.length() == 2) 
	            {
	                isCommentBlock = true;
	                continue;
	            }

	            if (line.endsWith("*/"))
	            {
	                isCommentBlock = false;

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
	    finally 
	    {
	        if (reader != null) 
	        {
	            try 
	            {
	                reader.close();
	            } catch (IOException e) 
	            {
	                e.printStackTrace();
	            }
	        }
	    }
	    return comment;
	}

	public double calculateDeviationOfComment(double javadoc, double comment, double function, double line)
	{
		double f_javadoc = javadoc;
		double f_comment = comment;
		double f_function = function;
		double f_line = line;
		double deviation = 0;
		try
		{
			double YG = ((javadoc + comment) * 0.8) / function;
			double YH = (line / function) * 0.3;
			deviation = ((100 * YG) / YH - 100);
		}
		catch(ArithmeticException e)
		{
			
		}
		deviation = Math.round(deviation * 100.0) / 100.0;
		return deviation;
	}
	
	public String getFileName(String file)
	{
		File File = new File(file);
        return File.getName();
    }
	
	private static boolean isRealComment(String line)
	{
		line = line.trim();
	    return !(line.isEmpty() || line.equals("*"));
	}
}

