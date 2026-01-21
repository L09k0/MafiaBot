package Mafia;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

@SuppressWarnings("deprecation")
public class BotCommandHendler 
{
	private Map<String, CommandInterface> commands = new HashMap<>();
	private Map<String, Session> activeSessions = new HashMap<>();
	
	public BotCommandHendler () 
	{
		CommandRegister("/help", this::Help);
		CommandRegister("/start", this::Start);
		CommandRegister("/create", this::Create);
		CommandRegister("/join", this::join);
		CommandRegister("/leave", this::Leave);
		CommandRegister("/list", this::List);
		CommandRegister("/kill", this::Kill);
		CommandRegister("/cure", this::Cure);
		CommandRegister("/check", this::Check);
		CommandRegister("/profile", this::Profile);
		CommandRegister("/editProfile", this::EditProfile);
		CommandRegister("/start", this::StartGame);
		CommandRegister("/end", this::EndGame);
		CommandRegister("/settings", this::Settings);
		CommandRegister("/vote", this::Vote);
		CommandRegister("/step", this::Step);
	}

	private void CommandRegister (String name, CommandInterface command)
	{
		commands.put(name, command);	
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
	
    private void Help (Database db, TelegramBot bot, Update upd) throws IOException 
    {
    	HelperList hpl = new HelperList();
    	String[] agrc = upd.message().text().split(" ");	
    	String helpText = null;
    	
    	if (agrc.length > 1)
    		helpText = hpl.HelpList(agrc[1]); 
    	else
    		helpText = hpl.HelpList("help");
    	
	    bot.execute(new SendMessage(upd.message().from().id(), helpText).parseMode(ParseMode.Markdown));
    }
    
    private void Start (Database db, TelegramBot bot, Update upd) throws SQLException 
    {
		if(!db.userExists(upd.message().from().id()))
			db.AddUserdb(upd.message().from().id(), upd.message().from().firstName().toString());
    }
    
    private void Create (Database db, TelegramBot bot, Update upd) throws SQLException 
    {
		Player plr = new Player();
		plr = db.getUserTgID(upd.message().from().id());
		
		for (Entry<String, Session> list : activeSessions.entrySet())
		{
			for (Entry<Long, Player> pl : list.getValue().getPlayer().entrySet())
			{
				if(pl.getValue().getUserID() == upd.message().from().id())
				{
					bot.execute(new SendMessage(upd.message().from().id(), "Вы уже в игре ! `" + String.valueOf(list.getValue().getSessionID()) + "`").parseMode(ParseMode.Markdown));
					return;
				}
			}
		}
		
		Session newSession = new Session();
		String lobbyCode = Long.toString(newSession.NewSession());
		
		do {
		    lobbyCode = Long.toString(newSession.NewSession());
		} while (activeSessions.containsKey(lobbyCode));
		
		activeSessions.put(lobbyCode, newSession);
		activeSessions.get(lobbyCode).AddPlayer(plr);
		
		bot.execute(new SendMessage(upd.message().from().id(), "Creating new lobby `" + lobbyCode + "`").parseMode(ParseMode.Markdown));
    }
    
    private void join (Database db, TelegramBot bot, Update upd) throws SQLException 
    {
		long chatId = upd.message().chat().id();
		String[] lobbyCode = upd.message().text().split(" ");	
		
		if (activeSessions.containsKey(lobbyCode[1])) 
		{	
			Player plr = new Player();
			
			plr = db.getUserTgID(upd.message().from().id());
			
			if (activeSessions.get(lobbyCode[1]).AddPlayer(plr))
				bot.execute(new SendMessage(chatId, "Ты присоединился в лобби !"));
			else 
				bot.execute(new SendMessage(chatId, "Не удалось присоединился в лобби !"));
	    } 
		else 
		{
	        bot.execute(new SendMessage(chatId, "Лобби не найдено !"));
	    }
    } 
    
    private void Leave (Database db, TelegramBot bot, Update upd) 
    {
		if (activeSessions.isEmpty())
			bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !").parseMode(ParseMode.Markdown));
		
		for (Entry<String, Session> list : activeSessions.entrySet())
		{		
			for (Entry<Long, Player> plr : list.getValue().getPlayer().entrySet())
			{
				if(plr.getValue().getUserID() == upd.message().from().id())
				{
					activeSessions.get(String.valueOf(list.getValue().getSessionID())).RemovePlayer(plr.getKey());
					
					if(list.getValue().getPlayer().isEmpty())
					{
						activeSessions.remove(String.valueOf(list.getValue().getSessionID()));
						bot.execute(new SendMessage(upd.message().from().id(), "Вы вышли из игры !").parseMode(ParseMode.Markdown));
						return;
					}
				}
			}
		}
    }
    
    private void List (Database db, TelegramBot bot, Update upd) 
    {
    	String str = "";
    	String[] agrc = upd.message().text().split(" ");	
    	
		if(agrc.length > 1)
		{
			switch (agrc[1])
			{
				case "session":
					if(activeSessions.values().isEmpty())
					{
						bot.execute(new SendMessage(upd.message().from().id(), "Нету активных игр !"));
						return;
					}
					
					str += "*Список активных игр:*\n";
					for (Entry<String, Session> list : activeSessions.entrySet())
					{
						for (Entry<Long, Player> plr : list.getValue().getPlayer().entrySet())
						{
							str += 
								  "----------------------------------\n"
								+ "Индификатор: `" + String.valueOf(list.getValue().getSessionID()) + "`\n"					
							    + "Игроки в игре:\n" 
							    + "\t\t\tНик: `" + plr.getValue().getGameUserNick() + "`\n" 
							    + "\t\t\tИндификатор: `" + plr.getValue().getPublicID() + "`\n";
						}
					}
				break;
				case "players":
					
					if (activeSessions.isEmpty())
					{
						bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !").parseMode(ParseMode.Markdown));
						return;
					}
					
					str += "*Список игроков в игре:*\n";
					for (Entry<String, Session> list : activeSessions.entrySet())
					{		
						for (Entry<Long, Player> plr : list.getValue().getPlayer().entrySet())
						{
							if(plr.getValue().getUserID() == upd.message().from().id())
							{
								activeSessions.get(String.valueOf(list.getValue().getSessionID())).RemovePlayer(plr.getKey());
								
								if(list.getValue().getPlayer().isEmpty())
								{
									str += 
											  "----------------------------------\n"				
										    + "Ник: `" + plr.getValue().getGameUserNick() + "`\n" 
										    + "Индификатор: `" + plr.getValue().getPublicID() + "`\n";
								}
							}
							else
								str += "Вас нету в игре !";
						}
					}
				break;
				default:
					str += "Неправильный аргумент !";
				break;
			}
			
			bot.execute(new SendMessage(upd.message().from().id(), str).parseMode(ParseMode.Markdown));
		}
		else
			bot.execute(new SendMessage(upd.message().from().id(), "Напиши /help чтобы узнать список команд !"));
    }
    
    private void Vote (Database db, TelegramBot bot, Update upd) 
    {
    	bot.execute(new SendMessage(upd.message().from().id(), "Vote !"));
    }

	private void Kill (Database db, TelegramBot bot, Update upd) 
	{
		bot.execute(new SendMessage(upd.message().from().id(), "kill !"));
	}
	
	private void Cure (Database db, TelegramBot bot, Update upd) 
	{
		bot.execute(new SendMessage(upd.message().from().id(), "Cure !"));
	}
	
	private void Check (Database db, TelegramBot bot, Update upd) 
	{
		bot.execute(new SendMessage(upd.message().from().id(), "Check !"));
	}
	
	private void Profile (Database db, TelegramBot bot, Update upd) throws SQLException
	{
		Player pop = new Player();	
		pop = db.getUserTgID(upd.message().from().id());
		
		String str = "*Ваш профиль:*\n\n"
				+ "Ник: `" + pop.getGameUserNick() + "`\n"
				+ "Индификатор: `" + pop.getPublicID() + "`";
		
		bot.execute(new SendMessage(upd.message().from().id(), str).parseMode(ParseMode.Markdown));
	}
	
	private void EditProfile (Database db, TelegramBot bot, Update upd) 
	{
		String[] agrc = upd.message().text().split(" ");	
		
		if(db.setUserEditProfile(agrc[1], upd.message().from().id()))
			bot.execute(new SendMessage(upd.message().from().id(), "Ник изменен !"));
		else
			bot.execute(new SendMessage(upd.message().from().id(), "Не удалось поменять ник !"));
	}
	
	private void Step (Database db, TelegramBot bot, Update upd) 
	{
		bot.execute(new SendMessage(upd.message().from().id(), "Step !"));
	}
	
	private void StartGame (Database db, TelegramBot bot, Update upd) 
	{
		bot.execute(new SendMessage(upd.message().from().id(), "Start !"));
	}

	private void EndGame (Database db, TelegramBot bot, Update upd) 
	{
		bot.execute(new SendMessage(upd.message().from().id(), "EndGame !"));
	}
	
	private void Settings (Database db, TelegramBot bot, Update upd)  throws SQLException
	{	
		Player plr = new Player();
		plr = db.getUserTgID(upd.message().from().id());
		
		if(existPlayerSession(plr.getPublicID()))
		{
			bot.execute(new SendMessage(upd.message().from().id(), String.valueOf(activeSessions.get(getActiveSessionID(plr.getPublicID())).getPlayerCount())));
		}
	}
	
    public void execute (String command, Database db, TelegramBot bot, Update upd) throws Exception 
    {
    	CommandInterface _command = commands.get(command);
        
    	if (_command != null) 
            _command.execute(db, bot, upd);
    	else
    		bot.execute(new SendMessage(upd.message().from().id(), "Нет такой команды !"));
    }
}
