package Mafia;

public enum GameState
{
	LOBBY,
	NIGHT,
	DAY, 
	GAME_END;
	
    public GameState next() 
    {
    	GameState[] steps = values();
        int nextIndex = (this.ordinal() + 1) % steps.length;
        return steps[nextIndex];
    }
    
    public GameState previous() 
    {
    	GameState[] steps = values();
        int prevIndex = (this.ordinal() - 1 + steps.length) % steps.length;
        return steps[prevIndex];
    }
}