package Mafia;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

@SuppressWarnings("deprecation")
public class SettingsCommandHandler 
{
	public interface CommandInterface 
	{
		void execute(String[] mgs, Database db, TelegramBot bot, Update upd) throws Exception, SQLException;
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
	
	private void PlayerCount (String[] mgs, Database db, TelegramBot bot, Update upd)
	{		
		try 
		{
			Player plr = new Player();
			plr = db.getUserTgID(upd.message().from().id());
			if(existPlayerSession(plr.getPublicID()))
			{
				if (mgs.length >= 3)	
				{
					activeSessions.get(getActiveSessionID(plr.getPublicID())).setPlayerCount(Integer.parseInt(mgs[2]));
					String str = String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getPlayerCount());
					bot.execute(new SendMessage(upd.message().from().id(), "Количество игроков изменено на " + str));
				}
				else
					bot.execute(new SendMessage(upd.message().from().id(), String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getPlayerCount())));				
			}
			else 
				bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре чтобы использовать настройки !")); 
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	    catch (NumberFormatException e)
	    {
	    	System.out.println(e.getMessage());
	    	bot.execute(new SendMessage(upd.message().from().id(), "Давай строки не будем пихать пока я не сделал нормальный обработчик ?)").parseMode(ParseMode.Markdown));
	    	return;
	    }
	}
	
	private void MafiaCount (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		try 
		{
			Player plr = new Player();
			plr = db.getUserTgID(upd.message().from().id());
			
			if(existPlayerSession(plr.getPublicID()))
			{
				if (mgs.length >= 3)	
				{
					activeSessions.get(getActiveSessionID(plr.getPublicID())).setMafiaCount(Integer.parseInt(mgs[2]));
					String str = String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getMafiaCount());
					bot.execute(new SendMessage(upd.message().from().id(), "Количество мафий изменено на " + str));
				}
				else
					bot.execute(new SendMessage(upd.message().from().id(), String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getMafiaCount())));				
			}
			else 
				bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре чтобы использовать настройки !")); 
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	    catch (NumberFormatException e)
	    {
	    	System.out.println(e.getMessage());
	    	bot.execute(new SendMessage(upd.message().from().id(), "Давай строки не будем пихать пока я не сделал нормальный обработчик ?)").parseMode(ParseMode.Markdown));
	    	return;
	    }
	}
	
	private void DoctorCount (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		try 
		{
			Player plr = new Player();
			plr = db.getUserTgID(upd.message().from().id());
			
			if(existPlayerSession(plr.getPublicID()))
			{
				if (mgs.length >= 3)	
				{
					activeSessions.get(getActiveSessionID(plr.getPublicID())).setDoctorCount(Integer.parseInt(mgs[2]));
					String str = String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getDoctorCount());
					bot.execute(new SendMessage(upd.message().from().id(), "Количество докторов изменено на " + str));
				}
				else
					bot.execute(new SendMessage(upd.message().from().id(), String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getDoctorCount())));				
			}
			else 
				bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре чтобы использовать настройки !")); 
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	    catch (NumberFormatException e)
	    {
	    	System.out.println(e.getMessage());
	    	bot.execute(new SendMessage(upd.message().from().id(), "Давай строки не будем пихать пока я не сделал нормальный обработчик ?)").parseMode(ParseMode.Markdown));
	    	return;
	    }
	}
	
	private void SheriffCount (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		try 
		{
			Player plr = new Player();
			plr = db.getUserTgID(upd.message().from().id());
			
			if(existPlayerSession(plr.getPublicID()))
			{
				if (mgs.length >= 3)	
				{
					activeSessions.get(getActiveSessionID(plr.getPublicID())).setSheriffCount(Integer.parseInt(mgs[2]));
					String str = String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getSheriffCount());
					bot.execute(new SendMessage(upd.message().from().id(), "Количество шерифов изменено на " + str));
				}
				else
					bot.execute(new SendMessage(upd.message().from().id(), String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getSheriffCount())));				
			}
			else 
				bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре чтобы использовать настройки !")); 
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	    catch (NumberFormatException e)
	    {
	    	System.out.println(e.getMessage());
	    	bot.execute(new SendMessage(upd.message().from().id(), "Давай строки не будем пихать пока я не сделал нормальный обработчик ?)").parseMode(ParseMode.Markdown));
	    	return;
	    }
	}
	
	private void NeutralCount (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		try 
		{	
			Player plr = new Player();
			plr = db.getUserTgID(upd.message().from().id());
			
			if(existPlayerSession(plr.getPublicID()))
			{
				if (mgs.length >= 3)	
				{
					activeSessions.get(getActiveSessionID(plr.getPublicID())).setNeutralCount(Integer.parseInt(mgs[2]));
					String str = String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getNeutralCount());
					bot.execute(new SendMessage(upd.message().from().id(), "Количество мирных жителей изменено на " + str));
				}
				else
					bot.execute(new SendMessage(upd.message().from().id(), String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getNeutralCount())));				
			}
			else 
				bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре чтобы использовать настройки !")); 
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	    catch (NumberFormatException e)
	    {
	    	System.out.println(e.getMessage());
	    	bot.execute(new SendMessage(upd.message().from().id(), "Давай строки не будем пихать пока я не сделал нормальный обработчик ?)").parseMode(ParseMode.Markdown));
	    	return;
	    }
	}

	private void NightDuration (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		try 
		{	
			Player plr = new Player();
			plr = db.getUserTgID(upd.message().from().id());
			
			if(existPlayerSession(plr.getPublicID()))
			{
				if (mgs.length >= 3)	
				{
					activeSessions.get(getActiveSessionID(plr.getPublicID())).setNightDuration(Integer.parseInt(mgs[2]));
					String str = String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getNightDuration());
					bot.execute(new SendMessage(upd.message().from().id(), "Продолжительность ночи изменено на " + str));
				}
				else
					bot.execute(new SendMessage(upd.message().from().id(), String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getNightDuration())));				
			}
			else 
				bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре чтобы использовать настройки !")); 
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	    catch (NumberFormatException e)
	    {
	    	System.out.println(e.getMessage());
	    	bot.execute(new SendMessage(upd.message().from().id(), "Давай строки не будем пихать пока я не сделал нормальный обработчик ?)").parseMode(ParseMode.Markdown));
	    	return;
	    }
	}
	
	private void DayDuration (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		try 
		{	
			Player plr = new Player();
			plr = db.getUserTgID(upd.message().from().id());
			
			if(existPlayerSession(plr.getPublicID()))
			{
				if (mgs.length >= 3)	
				{
					activeSessions.get(getActiveSessionID(plr.getPublicID())).setDayDuration(Integer.parseInt(mgs[2]));
					String str = String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getDayDuration());
					bot.execute(new SendMessage(upd.message().from().id(), "Продолжительность дня изменено на " + str));
				}
				else
					bot.execute(new SendMessage(upd.message().from().id(), String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getDayDuration())));				
			}
			else 
				bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре чтобы использовать настройки !")); 
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	    catch (NumberFormatException e)
	    {
	    	System.out.println(e.getMessage());
	    	bot.execute(new SendMessage(upd.message().from().id(), "Давай строки не будем пихать пока я не сделал нормальный обработчик ?)").parseMode(ParseMode.Markdown));
	    	return;
	    }
	}
	
	private void DiscussionTime (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		try
		{
			Player plr = new Player();
			plr = db.getUserTgID(upd.message().from().id());
			
			if(existPlayerSession(plr.getPublicID()))
			{
				if (mgs.length >= 3)	
				{
					activeSessions.get(getActiveSessionID(plr.getPublicID())).setDayDuration(Integer.parseInt(mgs[2]));
					String str = String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getDayDuration());
					bot.execute(new SendMessage(upd.message().from().id(), "Продолжительность дня изменено на " + str));
				}
				else
					bot.execute(new SendMessage(upd.message().from().id(), String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getDayDuration())));				
			}
			else 
				bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре чтобы использовать настройки !")); 
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	    catch (NumberFormatException e)
	    {
	    	System.out.println(e.getMessage());
	    	bot.execute(new SendMessage(upd.message().from().id(), "Давай строки не будем пихать пока я не сделал нормальный обработчик ?)").parseMode(ParseMode.Markdown));
	    	return;
	    }
	}
	
	private void VotingTime (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		try
		{
			Player plr = new Player();
			plr = db.getUserTgID(upd.message().from().id());
			
			if(existPlayerSession(plr.getPublicID()))
			{
				if (mgs.length >= 3)	
				{
					activeSessions.get(getActiveSessionID(plr.getPublicID())).setDiscussionTime(Integer.parseInt(mgs[2]));
					String str = String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getDiscussionTime());
					bot.execute(new SendMessage(upd.message().from().id(), "Время на обсуждения изменено на " + str));
				}
				else
					bot.execute(new SendMessage(upd.message().from().id(), String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getDiscussionTime())));				
			}
			else 
				bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре чтобы использовать настройки !")); 
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	    catch (NumberFormatException e)
	    {
	    	System.out.println(e.getMessage());
	    	bot.execute(new SendMessage(upd.message().from().id(), "Давай строки не будем пихать пока я не сделал нормальный обработчик ?)").parseMode(ParseMode.Markdown));
	    	return;
	    }
	}
	
	private void ShowRolesDeath (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		try
		{
			Player plr = new Player();
			plr = db.getUserTgID(upd.message().from().id());
			
			if(existPlayerSession(plr.getPublicID()))
			{
				if (mgs.length >= 3)	
				{
					activeSessions.get(getActiveSessionID(plr.getPublicID())).setShowRolesDeath(Boolean.valueOf(mgs[2]));
					String str = String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getShowRolesDeath());
					bot.execute(new SendMessage(upd.message().from().id(), "Показывание роли игрока после смерти изменено на " + str));
				}
				else
					bot.execute(new SendMessage(upd.message().from().id(), String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getShowRolesDeath())));				
			}
			else 
				bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре чтобы использовать настройки !")); 
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	private void LastWords (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		try
		{
			Player plr = new Player();
			plr = db.getUserTgID(upd.message().from().id());
			
			if(existPlayerSession(plr.getPublicID()))
			{
				if (mgs.length >= 3)	
				{
					activeSessions.get(getActiveSessionID(plr.getPublicID())).setLastWords(Boolean.parseBoolean(mgs[2]));
					String str = String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getLastWords());
					bot.execute(new SendMessage(upd.message().from().id(), "Показ последнего слова игрока изменено на " + str));
				}
				else
					bot.execute(new SendMessage(upd.message().from().id(), String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getLastWords())));				
			}
			else 
				bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре чтобы использовать настройки !")); 
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	}
	
	private void AnonymoVoting (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		try
		{
			Player plr = new Player();
			plr = db.getUserTgID(upd.message().from().id());
			
			if(existPlayerSession(plr.getPublicID()))
			{
				if (mgs.length >= 3)	
				{
					activeSessions.get(getActiveSessionID(plr.getPublicID())).setAnonymoVoting(Boolean.parseBoolean(mgs[2]));
					String str = String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getAnonymoVoting());
					bot.execute(new SendMessage(upd.message().from().id(), "Анонимные голосования изменено на " + str));
				}
				else
					bot.execute(new SendMessage(upd.message().from().id(), String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getAnonymoVoting())));				
			}
			else 
				bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре чтобы использовать настройки !")); 
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
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
	
	private void PrintAllSettings (String[] mgs, Database db, TelegramBot bot, Update upd)
	{
		try
		{
			Player plr = new Player();
			plr = db.getUserTgID(upd.message().from().id());
			String str = "*Список всех настроек игры:*\n"
					   + "------------- Количество -------------\n"
					   + "Игроков: " + String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getPlayerCount()) + "\n"
					   + "Мафий: " + String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getMafiaCount()) + "\n"
					   + "Докторов: " + String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getDoctorCount()) + "\n"
					   + "Шерифов: " + String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getSheriffCount()) + "\n"
					   + "Мирных: " + String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getNeutralCount()) + "\n"
					   + "\n------------- Время -------------\n"
					   + "На ночь: " + String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getNightDuration()) + "\n"
					   + "На день: "  + String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getDayDuration()) + "\n"
					   + "На обсуждение: " + String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getDiscussionTime()) + "\n"
					   + "На голосование: " + String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getVotingTime()) + "\n"
					   + "\n------------- Другое -------------\n"
					   + "Показ роми после смерти: " + String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getShowRolesDeath()) + "\n"
					   + "Последние слово: " + String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getLastWords()) + "\n"
					   + "Анонимные голосования: " + String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getAnonymoVoting()) + "\n";
			
			bot.execute(new SendMessage(upd.message().from().id(), str).parseMode(ParseMode.Markdown));	
		}
		catch (SQLException e)
		{
			System.out.println(e.getMessage());
		}
	    catch (NumberFormatException e)
	    {
	    	System.out.println(e.getMessage());
	    	bot.execute(new SendMessage(upd.message().from().id(), "Давай строки не будем пихать пока я не сделал нормальный обработчик ?)").parseMode(ParseMode.Markdown));
	    	return;
	    }
	}
	
	private void CommandRegister (String name, CommandInterface command)
	{
		commands.put(name, command);	
	}
	
    public void execute (String[] mgs, Database db, TelegramBot bot, Update upd, Map<String, Session> activeSessions) throws Exception 
    {
    	this.activeSessions = activeSessions;
    	
    	if(mgs.length > 1)
    	{
    		String command = mgs[1];
    		CommandInterface _command = commands.get(command);
    		if (_command != null) 
    			_command.execute(mgs, db, bot, upd);
    		else
    			bot.execute(new SendMessage(upd.message().from().id(), "Нет такого команды !"));
    	}
    	else
    	{
    		PrintAllSettings(mgs, db, bot, upd);    		
    		return;
    	}
    	
    }
}
