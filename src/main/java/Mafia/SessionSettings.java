package Mafia;

@SuppressWarnings("unused")
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
	}

	public int getPlayerCount() 
	{
		return this.playerCount;
	}
}
