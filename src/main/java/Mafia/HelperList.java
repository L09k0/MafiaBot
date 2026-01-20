package Mafia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class HelperList 
{
	
	public HelperList() {}
	
	private String ParserHelpList(String dirToHelpList) throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		BufferedReader buff = new BufferedReader(new FileReader(currentDirectory + dirToHelpList));
		StringBuilder content = new StringBuilder();
		String line;
		
		while ((line = buff.readLine()) != null) 
		{
		    content.append(line).append("\n");  // типа для переноса, не	 паноса !
		}

		buff.close();
		return content.toString();
	}
	
	public String HelpList(String agrc) throws IOException
	{
		String dirToHelpList = "";
		
        if (agrc == null) 
        {
            dirToHelpList = "\\resources\\Loc\\ru\\helplist\\help.md";
        } 
        else 
        {
            switch (agrc) 
            {
                case "help":
                    dirToHelpList = "\\resources\\Loc\\ru\\helplist\\help.md";
                break;
                case "check":
                    dirToHelpList = "\\resources\\Loc\\ru\\helplist\\check.md"; 
                break;
                case "create":
                    dirToHelpList = "\\resources\\Loc\\ru\\helplist\\create.md"; 
                break;
                case "cure":
                    dirToHelpList = "\\resources\\Loc\\ru\\helplist\\cure.md"; 
                break;
                case "editprofile":
                    dirToHelpList = "\\resources\\Loc\\ru\\helplist\\editprofile.md"; 
                break;
                case "end":
                    dirToHelpList = "\\resources\\Loc\\ru\\helplist\\end.md"; 
                break;
                case "join":
                    dirToHelpList = "\\resources\\Loc\\ru\\helplist\\join.md"; 
                break;
                case "kill":
                    dirToHelpList = "\\resources\\Loc\\ru\\helplist\\kill.md"; 
                break;
                case "leave":
                    dirToHelpList = "\\resources\\Loc\\ru\\helplist\\leave.md"; 
                break;
                case "profile":
                    dirToHelpList = "\\resources\\Loc\\ru\\helplist\\profile.md"; 
                break;
                case "settings":
                    dirToHelpList = "\\resources\\Loc\\ru\\helplist\\settings.md"; 
                break;
                case "start":
                    dirToHelpList = "\\resources\\Loc\\ru\\helplist\\start.md"; 
                break;
                case "step":
                    dirToHelpList = "\\resources\\Loc\\ru\\helplist\\step.md"; 
                break;
                case "vote":
                    dirToHelpList = "\\resources\\Loc\\ru\\helplist\\vote.md"; 
                break;
                default:
                    dirToHelpList = "\\resources\\Loc\\ru\\helplist\\help.md";
                    break;
            }
        }
		
//		if (dirToHelpList.equals("") | dirToHelpList.equals(null))
//			dirToHelpList += "\\resources\\Loc\\ru\\helplist\\help.md";
		
		return ParserHelpList(dirToHelpList);
	}
}












