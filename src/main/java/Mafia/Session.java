package Mafia;

import java.util.concurrent.ThreadLocalRandom;

public class Session 
{
	// Indicator
	private long sessionID;
	//private SessionSettings sSettings = new SessionSettings();
	
	// State
	//private GameState currentState;
	//private GameStep currentStep;
	//private int currentDay;
	
	// players
	//private Map<Long, Player> players;
	
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
}
