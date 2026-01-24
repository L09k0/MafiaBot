package Mafia;

public class SessionSettings 
{
	// players count
	private int playerCount;
	private int mafiaCount;
	private int doctorCount;
	private int sheriffCount;
	private int neutralCount;  
	
	// Time 
	private int nightDuration;        
    private int dayDuration;          
    private int discussionTime;       
    private int votingTime;    
	
    // other
    private boolean showRolesDeath;
    private boolean lastWords; 
    private boolean anonymoVoting;
    private boolean leaderChoosesRoles;
    
	public SessionSettings() 
	{
		SetDefault();
	}
	
	public void SetDefault()
	{
		// players
		this.playerCount = 10;
		this.mafiaCount = 2;
		this.doctorCount = 1;
		this.sheriffCount = 1;
		this.neutralCount = 6;
		
		// time (*second)
		this.nightDuration = 30;
		this.dayDuration = 180;
		this.discussionTime = 120;
		this.votingTime = 60;
		
		// other
		this.showRolesDeath = false;
		this.lastWords = true;
		this.anonymoVoting = false;
		this.leaderChoosesRoles = false;
	}

	public int getPlayerCount() 
	{
		return this.playerCount;
	}
	
	public int getMafiaCount() 
	{
		return this.mafiaCount;
	}
	
	public int getDoctorCount() 
	{
		return this.doctorCount;
	}
	
	public int getSheriffCount()
	{
		return this.sheriffCount;
	}
	
	public int getNeutralCount() 
	{
		return this.neutralCount;
	}
	
	public int getNightDuration() 
	{
		return this.nightDuration;
	}
	
	public int getDayDuration() 
	{
		return this.dayDuration;
	}
	
	public int getDiscussionTime() 
	{
		return this.discussionTime;
	}

	public int getVotingTime() 
	{
		return this.votingTime;
	}
	
	public boolean getShowRolesDeath() 
	{
		return this.showRolesDeath;
	}
	
	public boolean getLastWords() 
	{
		return this.lastWords;
	}
	
	public boolean getAnonymoVoting() 
	{
		return this.anonymoVoting;
	}
	
	public boolean getLeaderChoosesRoles() 
	{
		return this.leaderChoosesRoles;
	}
	
	public void setPlayerCount(int playerCount) 
	{
		this.playerCount = playerCount;
	}
	
	public void setMafiaCount(int mafiaCount) 
	{
		this.mafiaCount = mafiaCount;
	}
	
	public void setDoctorCount(int doctorCount) 
	{
		this.doctorCount = doctorCount;
	}
	
	public void setSheriffCount(int sheriffCount)
	{
		this.sheriffCount = sheriffCount;
	}
	
	public void setNeutralCount(int neutralCount) 
	{
		this.neutralCount = neutralCount;
	}
	
	public void setNightDuration(int nightDuration) 
	{
		this.nightDuration = nightDuration;
	}
	
	public void setDayDuration(int dayDuration) 
	{
		this.dayDuration = dayDuration;
	}
	
	public void setDiscussionTime(int discussionTime) 
	{
		this.discussionTime = discussionTime;
	}

	public void setVotingTime(int votingTime) 
	{
		this.votingTime = votingTime;
	}
	
	public void setShowRolesDeath(boolean showRolesDeath) 
	{
		this.showRolesDeath = showRolesDeath;
	}
	
	public void setLastWords(boolean lastWords) 
	{
		this.lastWords = lastWords;
	}
	
	public void setAnonymoVoting(boolean anonymoVoting) 
	{
		this.anonymoVoting = anonymoVoting;
	}
	
	public boolean setLeaderChoosesRoles(boolean leaderChoosesRoles) 
	{
		return this.leaderChoosesRoles = leaderChoosesRoles;
	}
}
