package Mafia;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

public class SettingsCommandHandler 
{
	public interface CommandInterface 
	{
		void execute(String[] mgs, Database db, TelegramBot bot, Update upd) throws Exception;
	}
	
	private Map<String, CommandInterface> commands = new HashMap<>();
	private Map<String, Session> activeSessions = new HashMap<>();
	
	public SettingsCommandHandler(Map<String, Session> activeSessions) 
	{
		this.activeSessions = activeSessions;
		CommandRegister("playerCount", this::PlayerCount);
	}
	
	private void PlayerCount (String[] mgs, Database db, TelegramBot bot, Update upd) throws SQLException 
	{
		Player plr = new Player();
		plr = db.getUserTgID(upd.message().from().id());
		
		if(existPlayerSession(plr.getPublicID()))
		{
			bot.execute(new SendMessage(upd.message().from().id(), String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getPlayerCount())));
		}
	}
	
	private boolean existPlayerSession(long plrID)
	{
		if (activeSessions.isEmpty())
			return false;
		
		for (Entry<String, Session> list : activeSessions.entrySet())
		{		
			if(list.getValue().getPlayer().containsKey(plrID))
				return true;
		}
		
		return false;
	}

	private String getActiveSessionID(long plrID)
	{
		if (activeSessions.isEmpty())
			return null;
		
		for (Entry<String, Session> list : activeSessions.entrySet())
		{	
	        if (list.getValue().getPlayer().containsKey(plrID)) 
	        {
	            return list.getKey();
	        }
		}
		return null;
	}
	
	private void CommandRegister (String name, CommandInterface command)
	{
		commands.put(name, command);	
	}
	
    public void execute (String[] mgs, Database db, TelegramBot bot, Update upd) throws Exception 
    {
    	String command = mgs[1];
    	CommandInterface _command = commands.get(command);
    	if (_command != null) 
            _command.execute(mgs, db, bot, upd);
    	else
    		bot.execute(new SendMessage(upd.message().from().id(), "Нет такого команды !"));
    }
}
