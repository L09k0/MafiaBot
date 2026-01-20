package Mafia;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Session 
{
	// Indicator
	private long sessionID;
	private SessionSettings Settings = new SessionSettings();
	
	// State
	//private GameState currentState;
	//private GameStep currentStep;
	//private int currentDay;
	
	// players
	private Map<Long, Player> players = new HashMap<>();
	
	public Session()
	{
		// TODO Auto-generated constructor 
	}
	
	public long generateCode()
	{
		return ThreadLocalRandom.current().nextLong(10000, 1000000);
	}
	
	public long NewSession()	
	{
		sessionID = generateCode();
		return this.sessionID;
	}
	
	public long getSessionID()
	{
		return this.sessionID;
	}
	
	public Map<Long, Player> getPlayer()
	{
		return this.players;
	}
	
	public boolean AddPlayer(Player plr)
	{
		players.put(plr.getPublicID(), plr);
		
		return true;
	}
	
	public boolean RemovePlayer(long public_id)
	{
		players.remove(public_id);
		
		return true;
	}
	
	public int getPlayerCount()
	{
		return Settings.getPlayerCount();
	}
}













