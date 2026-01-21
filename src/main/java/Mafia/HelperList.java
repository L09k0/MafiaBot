package Mafia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class HelperList 
{
	
	public HelperList() {}
	
	private String ParserHelpList(String dirToHelpList) throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		File file = new File(currentDirectory + dirToHelpList);
		
		if (!file.exists())
		{
			System.out.println("File not found: " + file.getPath());			
			return null;
		}
				
		  try (BufferedReader buff = new BufferedReader(new FileReader(file))) 
		  {
		        StringBuilder content = new StringBuilder();
		        String line;
		        
		        while ((line = buff.readLine()) != null) 
		        {
		            content.append(line).append("\n"); // типа для переноса, не	 паноса !
		        }
		        
		        return content.toString();
		  } 
		  catch (IOException e) 
		  {
		        e.printStackTrace();
		        return "Error readFile: " + e.getMessage();
		  }
	}
	
	public String HelpList(String agrc) throws IOException
	{
		String str = ParserHelpList("\\resources\\Loc\\ru\\helplist\\" + agrc + ".md"); 
        
		if (str != null)
			return str; 
		else 
			return "Нет такой команды !";
	}
}












