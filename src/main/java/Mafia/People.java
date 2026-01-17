package Mafia;

import com.pengrad.telegrambot.TelegramBot;

@SuppressWarnings("unused")
public class People
{
	private TelegramBot bot;

    public People(TelegramBot bot) 
    {
        this.bot = bot;
    }
    
	public People() 
	{
		System.out.println("People");
	}
	
	private enum Suits
	{
		PEOPLE,
		MAFIA,
		DORTOR,
		SHERIFF
	}
	
	private enum State
	{
		DIE,
		LIVE
	}
	
	private enum Status
	{
		CURED,
		CHECK
	}
	
	private String name;
	private Suits suit;
	private State state;
	private int id;
}
