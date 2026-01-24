package Mafia;

public enum PlayerRole
{
	DEFAULT,
	MAFIA,
	DOCTOR,
	SHERIFF,
	NEUTRAL;
	
    public PlayerRole next() 
    {
    	PlayerRole[] steps = values();
        int nextIndex = (this.ordinal() + 1) % steps.length;
        return steps[nextIndex];
    }
    
    public PlayerRole previous() 
    {
    	PlayerRole[] steps = values();
        int prevIndex = (this.ordinal() - 1 + steps.length) % steps.length;
        return steps[prevIndex];
    }
}
