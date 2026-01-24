package Mafia;

public enum GameStep
{
	MAFIA_ACTION,
	DOCTOR_ACTION,
	DETECTIVE_ACTION,
	DAY_DISCUSSION,
	DAY_VOTING,
	DAY_VOTING_ENG,
	DAY_LAST_WORD;
	
    public GameStep next() 
    {
        GameStep[] steps = values();
        int nextIndex = (this.ordinal() + 1) % steps.length;
        return steps[nextIndex];
    }
    
    public GameStep previous() 
    {
        GameStep[] steps = values();
        int prevIndex = (this.ordinal() - 1 + steps.length) % steps.length;
        return steps[prevIndex];
    }
}