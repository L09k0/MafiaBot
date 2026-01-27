package Mafia;

public class Player
{
	private String tg_name;
	private String nickname;
	private long tg_id;
	private long public_id;
	private PlayerRole rp;
	private boolean live;
	
	public Player() 
	{
		this.NULL();
	}
	
	public void setUserDB(long tg_id, long public_id, String nickname, String tg_name)
	{
		this.tg_id = tg_id;
		this.public_id = public_id;
		this.nickname = nickname;
		this.tg_name = tg_name;
	}
	
	public void setUser(long tg_id, String nickname, String tg_name)
	{
		this.tg_id = tg_id;
		this.nickname = nickname;
		this.tg_name = tg_name;
	}
	
	public void setRole(PlayerRole rp)
	{
		this.rp = rp;
	}
	
	public PlayerRole getRole()
	{
		return this.rp;
	}
	
	public long getUserID()
	{
		return this.tg_id;	
	}
	
	public long getPublicID()
	{
		return this.public_id;	
	}
	
	public String getGameUserNick()
	{
		return this.nickname;	
	}
	
	public String getTGUserName()
	{
		return this.tg_name;	
	}
	
	public boolean getLiveStatus()
	{
		return this.live;	
	}
	
	public void setLiveStatus(boolean live)
	{
		this.live = live;	
	}
	
	
	public Player NULL()
	{
		this.tg_id = 0;
		this.public_id = 0;
		this.tg_name = null;
		this.nickname = null;
		this.rp = PlayerRole.DEFAULT;
		this.live = true;
		
		return this;
	}	
}








