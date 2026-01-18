package Mafia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.ThreadLocalRandom;

public class Database 
{
	private String namedb;
	private Connection conn = null;
	
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
	
	public void AddUserdb(long tg_id, String tg_username)
	{
		long public_id = ThreadLocalRandom.current().nextLong(10000, 10000000);
		String game_username = tg_username;
		
        String sql = "INSERT OR IGNORE INTO users (tg_id, public_id, tg_username, game_username)"
        		   + "VALUES (?, ?, ?, ?);";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) 
        {
            pstmt.setLong(1, tg_id);
            pstmt.setLong(2, public_id);
            pstmt.setString(3, tg_username);
            pstmt.setString(4, game_username);
        } 
        catch (SQLException e) 
        {
            System.out.println(e.getMessage());
        }
				
	}
}










