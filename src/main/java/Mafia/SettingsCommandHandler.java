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
	
	public SettingsCommandHandler() 
	{
		CommandRegister("playercount", this::PlayerCount);
		CommandRegister("mafiacount", this::MafiaCount);
		CommandRegister("doctorcount", this::DoctorCount);
		CommandRegister("sheriffcount", this::SheriffCount);
		CommandRegister("neutralcount", this::NeutralCount);
		
		CommandRegister("nightduration", this::NightDuration);
		CommandRegister("dayduration", this::DayDuration);
		CommandRegister("discussiontime", this::DiscussionTime);
		CommandRegister("votingtime", this::VotingTime);
		
		CommandRegister("showrolesDeath", this::ShowRolesDeath);
		CommandRegister("lastwords", this::LastWords);
		CommandRegister("anonymovoting", this::AnonymoVoting);
	}
	
	private void PlayerCount (String[] mgs, Database db, TelegramBot bot, Update upd) throws SQLException 
	{
		Player plr = new Player();
		plr = db.getUserTgID(upd.message().from().id());
		
		if(existPlayerSession(plr.getPublicID()))
		{
			if (mgs.length == 2)	
			{
				bot.execute(new SendMessage(upd.message().from().id(), String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getPlayerCount())));				
			}
			else
			{
				activeSessions.get(getActiveSessionID(plr.getPublicID())).setPlayerCount(Integer.parseInt(mgs[2]));
				String str = String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getPlayerCount());
				bot.execute(new SendMessage(upd.message().from().id(), "Количество игроков изменено на " + str));
			}
		}
	}
	
	private void MafiaCount (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		
	}
	
	private void DoctorCount (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		
	}
	
	private void SheriffCount (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		
	}
	
	private void NeutralCount (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		
	}
	
	private void NightDuration (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		
	}
	
	private void DayDuration (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		
	}
	
	private void DiscussionTime (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		
	}
	
	private void VotingTime (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		
	}
	
	private void ShowRolesDeath (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		
	}
	
	private void LastWords (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		
	}
	
	private void AnonymoVoting (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		
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
	
    public void execute (String[] mgs, Database db, TelegramBot bot, Update upd, Map<String, Session> activeSessions) throws Exception 
    {
    	this.activeSessions = activeSessions;
    	String command = mgs[1];
    	CommandInterface _command = commands.get(command);
    	if (_command != null) 
            _command.execute(mgs, db, bot, upd);
    	else
    		bot.execute(new SendMessage(upd.message().from().id(), "Нет такого команды !"));
    }
}
