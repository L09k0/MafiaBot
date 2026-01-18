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
	private PreparedStatement pstmt;
	
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
	
	public People getUserTgID(long tg_id) throws SQLException
	{
		People pop = new People();
		
		String sql = "SELECT * FROM users WHERE tg_id = ?";
		PreparedStatement pstmt = conn.prepareStatement(sql);
		pstmt.setLong(1, tg_id);
		ResultSet result = pstmt.executeQuery();

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
	
	public void AddUserdb(long tg_id, String tg_username) throws SQLException
	{
		long public_id = ThreadLocalRandom.current().nextLong(10000, 10000000);
		String game_username = tg_username;
		
		sql = "SELECT tg_id FROM users WHERE tg_id = ?";
		pstmt = conn.prepareStatement(sql);
		pstmt.setLong(1, tg_id);
		ResultSet rs = pstmt.executeQuery();

		try (PreparedStatement checkStmt = conn.prepareStatement(sql)) 
		{
	        checkStmt.setLong(1, tg_id);
	        rs = checkStmt.executeQuery();
	        
	        if (!rs.next()) 
	        {
	            String insertSql = "INSERT INTO users (tg_id, public_id, tg_username, game_username) "
	                             + "VALUES (?, ?, ?, ?)";
	            
	            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) 
	            {
	                insertStmt.setLong(1, tg_id);
	                insertStmt.setLong(2, public_id);
	                insertStmt.setString(3, tg_username);
	                insertStmt.setString(4, game_username);
	                
	                if (insertStmt.executeUpdate() > 0) 
	                {
	                    System.out.println("Add user: " + tg_username + " " + tg_id + "\n");   
	                }
	            }
	        } 
	        else 
	        {
	        	 System.out.println("User already exist: " + tg_username + " " + tg_id + "\n");   
	        }
	    } 
	  	catch (SQLException e) 
	  	{
	        System.err.println(e.getMessage());
	    }
	}			
}










