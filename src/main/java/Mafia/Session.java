package Mafia;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
			// publicid
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
		
		if (players.size() < 4)
			throw new Exception("В лобби слишком мало участников, чтобы начать игру !");
		
		if (settings.getLeaderChoosesRoles())
		{
			for (Entry<Long, Player> plrs : players.entrySet()) 
			{
				if (plrs.getValue().getRole() == PlayerRole.DEFAULT)
				{
					throw new Exception("Не у всех участников назначены роли, чтобы начать игру !");
				}
			}
			
			this.currentState = GameState.NIGHT;
		}
		else
		{
			assignRandomRoles();
		}
		
	//	System.out.println(players.size());
	}
	
	public void closeSession()
	{
		this.players.clear();
		this.leader = null;
		System.out.println("Сессия " + sessionID + " закрыта !");
	}
	
/*	для теста
	public void setFindСoefficient() throws Exception
	{
		int P = players.size();
		if (P < 0)
		{
			System.out.println("Игроков не найдено !\n"); 
			return;
		}
		
	    int M = P <= 6 ? 1 : P <= 12 ? 2 : P <= 17 ? 3 : 4;
	    int D = P < 5 ? 0 : P < 15 ? 1 : 2;
	    int S = P < 6 ? 0 : P < 15 ? 1 : 2;
	    
	    List<PlayerRole> roles = new ArrayList<>();
	    for (int i = 0; i < M; i++) 
	    	roles.add(PlayerRole.MAFIA);
	    
	    for (int i = 0; i < D; i++) 
	    	roles.add(PlayerRole.DOCTOR);
	    
	    for (int i = 0; i < S; i++) 
	    	roles.add(PlayerRole.SHERIFF);
	    
	    for (int i = 0; i < (P - M - D - S); i++) 
	    	roles.add(PlayerRole.NEUTRAL);
	    
	    Collections.shuffle(roles);
	    
	    int idx = 0;
	    for (Player p : players.values()) 
	    {
	        p.setRole(roles.get(idx++));
	    }
		
	}
	 */
	public void assignRandomRoles() 
	{
		int P = players.size();
		if (P < 0)
		{
			System.out.println("Игроков не найдено !\n");
			return;
		}
		
		// Хз как я это сделал, но оно работает блин
	    int M = P <= 6 ? 1 : P <= 12 ? 2 : P <= 17 ? 3 : 4;
	    int D = P < 5 ? 0 : P < 15 ? 1 : 2;
	    int S = P < 6 ? 0 : P < 15 ? 1 : 2;
	    
	    List<PlayerRole> roles = new ArrayList<>();
	    for (int i = 0; i < M; i++) 
	    	roles.add(PlayerRole.MAFIA);
	    
	    for (int i = 0; i < D; i++) 
	    	roles.add(PlayerRole.DOCTOR);
	    
	    for (int i = 0; i < S; i++) 
	    	roles.add(PlayerRole.SHERIFF);
	    
	    for (int i = 0; i < (P - M - D - S); i++) 
	    	roles.add(PlayerRole.NEUTRAL);
	    
	    Collections.shuffle(roles);
	    
	    int idx = 0;
	    for (Player p : players.values()) 
	    {
	        p.setRole(roles.get(idx++));
	    }
	    
	    logRoleDistribution();
	}

	private void logRoleDistribution() 
	{
	    Map<PlayerRole, Integer> count = new HashMap<>();
	    
	    for (Player player : players.values()) 
	    {
	        PlayerRole role = player.getRole();
	        count.put(role, count.getOrDefault(role, 0) + 1);
	    }
	    
	    System.out.println("=== Распределение ролей ===");
	    for (PlayerRole role : PlayerRole.values()) 
	    {
	        int roleCount = count.getOrDefault(role, 0);
	        if (roleCount > 0) 
	        {
	            System.out.println(role + ": " + roleCount + " игроков");
	        }
	    }
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
	
	public void setRole(long pPlrID, PlayerRole rp)
	{
		System.out.println("Userid: " + pPlrID + " set role \"" + rp.toString() + "\"\n");
		players.get(pPlrID).setRole(rp);
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
	
	public boolean AddPlayer(Player plr) throws Exception
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













