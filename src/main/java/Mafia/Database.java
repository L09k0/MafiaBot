package Mafia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database 
{
	private String namedb = "jdbc:sqlite:mafia.db";
	
	public Database() 
	{
		System.out.println("Connect database !\n");
		
        try (Connection conn = DriverManager.getConnection(namedb)) 
        {
            if (conn != null) 
            {
        		System.out.println("Connect database successfully !\n");
            }
        } 
        catch (SQLException e) 
        {
            System.out.println(e.getMessage());
        }
	 
	}
}
