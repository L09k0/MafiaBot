package Mafia;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

public class Session 
{
	// Indicator
	private long sessionID;
	private SessionSettings settings = new SessionSettings();
	
	// State
	private GameState currentState;
	//private GameStep currentStep;
	//private int currentDay;
	
	// players
	private Map<Long, Player> players = new HashMap<>();
	Player leader = new  Player();
	
	public Session()
	{
		
	}
	
	public void setLobby()
	{
		currentState = GameState.LOBBY;
		System.out.println(currentState);
	}
	
	public void StartGame() throws Exception
	{
		if (currentState != GameState.LOBBY)
			throw new Exception("Вы не в лобби, чтобы начать игру !");
		
		
		
		currentState = GameState.NIGHT;
		System.out.println(currentState);
	}
	
	public void Step()
	{
		currentState = currentState.next();
		System.out.println(currentState);
	}
	
	public void EndGame()
	{
		currentState = currentState.next();
		System.out.println(currentState);
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
	
	public void setLeaderGame(Player leader)
	{
		this.leader = leader;
	}
	
	public Player getLeaderGame()
	{
		return this.leader;
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
		return this.settings.getPlayerCount();
	}
	
	public int getMafiaCount()
	{
		return this.settings.getMafiaCount();
	}
	
	public int getDoctorCount()
	{
		return this.settings.getDoctorCount();
	}
	
	public int getSheriffCount()
	{
		return this.settings.getSheriffCount();
	}
	
	public int getNeutralCount()
	{
		return this.settings.getNeutralCount();
	}
	
	public int getNightDuration()
	{
		return this.settings.getNightDuration();
	}
	
	public int getDayDuration()
	{
		return this.settings.getDayDuration();
	}
	
	public int getDiscussionTime()
	{
		return this.settings.getDiscussionTime();
	}
	
	public int getVotingTime()
	{
		return this.settings.getVotingTime();
	}
	
	public boolean getShowRolesDeath()
	{
		return this.settings.getShowRolesDeath();
	}
	
	public boolean getLastWords()
	{
		return this.settings.getLastWords();
	}
	
	public boolean getAnonymoVoting()
	{
		return this.settings.getAnonymoVoting();
	}
	
	public boolean getLeaderChoosesRoles() 
	{
		return this.settings.getLeaderChoosesRoles();
	}
	
	public void setPlayerCount(int playerCount)
	{
		this.settings.setPlayerCount(playerCount);
	}
	
	public void setMafiaCount(int mafiaCount)
	{
		this.settings.setMafiaCount(mafiaCount);
	}
	
	public void setDoctorCount(int doctorCount)
	{
		this.settings.setDoctorCount(doctorCount);
	}
	
	public void setSheriffCount(int sheriffCount)
	{
		this.settings.setSheriffCount(sheriffCount);
	}
	
	public void setNeutralCount(int neutralCount)
	{
		this.settings.setNeutralCount(neutralCount);
	}
	
	public void setNightDuration(int nightDuration)
	{
		this.settings.setNightDuration(nightDuration);
	}
	
	public void setDayDuration(int dayDuration)
	{
		this.settings.setDayDuration(dayDuration);
	}
	
	public void setDiscussionTime(int discussionTime)
	{
		this.settings.setDiscussionTime(discussionTime);
	}
	
	public void setVotingTime(int votingTime)
	{
		this.settings.setVotingTime(votingTime);
	}
	
	public void setShowRolesDeath(boolean showRolesDeath)
	{
		this.settings.setShowRolesDeath(showRolesDeath);
	}
	
	public void setLastWords(boolean lastWords)
	{
		this.settings.setLastWords(lastWords);
	}
	
	public void setAnonymoVoting(boolean anonymoVoting)
	{
		this.settings.setAnonymoVoting(anonymoVoting);
	}
	
	public boolean setLeaderChoosesRoles(boolean leaderChoosesRoles) 
	{
		return this.settings.setLeaderChoosesRoles(leaderChoosesRoles);
	}
}













