package Mafia;

public class People
{
	private String game_username;
	private long id;
	private long public_id;
	
	public void setUserDB(long id, long public_id, String game_username)
	{
		this.id = id;
		this.public_id = public_id;
		this.game_username = game_username;
	}
	
	public long getID()
	{
		return this.id;	
	}
	
	public long getPublicID()
	{
		return this.public_id;	
	}
	
	public String getGameUserNick()
	{
		return this.game_username;	
	}
	
	public People NULL()
	{
		this.id = 0;
		this.public_id = 0;
		this.game_username = null;
		
		return this;
	}
	
}








