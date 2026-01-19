package Mafia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ThreadLocalRandom;

public class Database 
{
	private String namedb;
	private Connection conn = null;
	private String sql;
	
	public Database(String nameDB) 
	{
		this.namedb = nameDB;
	}
	
	public void Connect()
	{
		System.out.println("Connect database !\n");
		
        try  
        {
        	conn = DriverManager.getConnection("jdbc:sqlite:"+namedb+".db");
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
	
	public void CreateTable()
	{
        String sql = "CREATE TABLE IF NOT EXISTS users ("
        		   + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
        		   + "tg_id BIGINT NOT NULL,"
        		   + "public_id BIGINT NOT NULL,"
        		   + "tg_username VARCHAR(64) NOT NULL,"
        		   + "game_username VARCHAR(64) NOT NULL"
        		   + ");";
	
        try (Statement stmt = conn.createStatement()) 
        {
            stmt.execute(sql);
            System.out.println("Table create !\n");
        } 
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
	}
	
	public boolean setUserEditProfile(String game_username, long tg_id)
	{    	
    	try
    	{
    		sql = "SELECT game_username FROM users WHERE tg_id = ?";
    		PreparedStatement stmt = conn.prepareStatement(sql);
    		stmt.setLong(1, tg_id);
    		ResultSet result = stmt.executeQuery();
    		
    		if (!result.next()) 
    		{
    			System.out.println("User not found: " + tg_id);
    			return false;
    		}
    		
        	sql = "UPDATE users SET game_username = ? WHERE tg_id = ?";
	    	stmt = conn.prepareStatement(sql);
	    	stmt.setString(1, game_username);
	    	stmt.setLong(2, tg_id);
			
			
			String oldNick = result.getString("game_username");
	        if (stmt.executeUpdate() > 0) 
	            System.out.println("Changed Nick: " + oldNick + " to " + game_username + "\n");   
	        else
	        	return false;
    	}
    	catch(SQLException e)
    	{
    		System.out.println(e.getMessage());
    		return false;
    	}
		
    	return true;
	}
	
	public Player getUserTgID(long tg_id) throws SQLException
	{
		Player pop = new Player();
		
		String sql = "SELECT * FROM users WHERE tg_id = ?";
		PreparedStatement stmt = conn.prepareStatement(sql);
		stmt.setLong(1, tg_id);
		ResultSet result = stmt.executeQuery();

		if (result.next()) 
		{
			pop.setUserDB(result.getLong("id"), result.getLong("public_id"), result.getString("game_username"));
			return pop;
		}
		else 
		{
			System.out.println("People init error !\n");
			return pop.NULL();
		}
	}
	
	public boolean userExists(long tg_id) throws SQLException 
	{
	    sql = "SELECT tg_id FROM users WHERE tg_id = ?";
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) 
	    {
	    	stmt.setLong(1, tg_id);
	        ResultSet result = stmt.executeQuery();
	        return result.next(); 
	    }
	}
	
	private boolean publicIdExists(long public_id) throws SQLException 
	{
	    sql = "SELECT public_id FROM users WHERE public_id = ?";
	    
	    try (PreparedStatement stmt = conn.prepareStatement(sql)) 
	    {
	    	stmt.setLong(1, public_id);
	        ResultSet result = stmt.executeQuery();
	        return result.next();
	    }
	}
	
	public void AddUserdb(long tg_id, String tg_username) throws SQLException
	{
		long public_id = ThreadLocalRandom.current().nextLong(10000, 10000000);
		String game_username = tg_username;
		PreparedStatement stmt;

		try 
		{
	        if (userExists(tg_id)) 
	        {
	        	System.out.println("User already exist: " + tg_username + " " + tg_id + "\n");
	        }
	        else
	        {

			    do {
			        public_id = ThreadLocalRandom.current().nextLong(10000, 10000000);   
			    } while (publicIdExists(public_id));
			
				
	        	sql = "INSERT INTO users (tg_id, public_id, tg_username, game_username) "
	                             + "VALUES (?, ?, ?, ?)";
	            
	        	stmt = conn.prepareStatement(sql);

				stmt.setLong(1, tg_id);
				stmt.setLong(2, public_id);
				stmt.setString(3, tg_username);
				stmt.setString(4, game_username);
                
                if (stmt.executeUpdate() > 0) 
                {
                    System.out.println("Add user: " + tg_username + " " + tg_id + "\n");   
		        } 
	        }
		}
		catch (SQLException e) 
	  	{
	        System.err.println(e.getMessage());
	    }
	}			
}










