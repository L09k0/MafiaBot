package Mafia;

public class Player
{
	private String nickname;
	private long userId;
	private long public_id;
	
	public void setUserDB(long userId, long public_id, String game_username)
	{
		this.userId = userId;
		this.public_id = public_id;
		this.nickname = game_username;
	}
	
	public void setUser(long userId, String game_username)
	{
		this.userId = userId;
		this.nickname = game_username;
	}
	
	public long getUserID()
	{
		return this.userId;	
	}
	
	public long getPublicID()
	{
		return this.public_id;	
	}
	
	public String getGameUserNick()
	{
		return this.nickname;	
	}
	
	public Player NULL()
	{
		this.userId = 0;
		this.public_id = 0;
		this.nickname = null;
		
		return this;
	}	
}








