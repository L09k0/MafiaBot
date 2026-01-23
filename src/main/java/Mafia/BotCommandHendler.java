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
	public interface CommandInterface 
	{
		void execute(Database db, TelegramBot bot, Update upd) throws Exception;
	}
	
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
		CommandRegister("/tests", this::TestSessionsStart);
		CommandRegister("/teste", this::TestSessionsEnd);
		CommandRegister("/lobby", this::ShowLobbyInfo);
	}

	private void CommandRegister (String name, CommandInterface command)
	{
		commands.put(name, command);	
	}
	
	private	void ShowLobbyInfo(Database db, TelegramBot bot, Update upd) throws SQLException
	{	
		try
		{
			String str = "";
			Player curreplr = new Player();
			curreplr = db.getUserTgID(upd.message().from().id());
			
			str += "------------- Лобби -------------\n";
	        for (Entry<String, Session> list : activeSessions.entrySet()) 
	        {	
	        	str += 	"Индификатор игры: `" + list.getValue().getSessionID() + "`\n";
	        	
	            for (Entry<Long, Player> plrs : list.getValue().getPlayer().entrySet()) 
	            {
	                if (plrs.getValue().getUserID() == upd.message().from().id()) 
	                {
	                    str += "----------------------------------\n";
	                    for (Entry<Long, Player> plr : list.getValue().getPlayer().entrySet()) 
	                    {
							str += 	"*Игроки в игре:*\n" 
								    + "\t\t\tНик: `" + plr.getValue().getGameUserNick() + "`\n" ;
								//    + "\t\t\tИндификатор: `" + plr.getValue().getPublicID() + "`\n";
	                    }
	                    break;
	                }
	            }
	        }
	        
			
	        str += "\n*Список настроек игры:*\n"
	        + "------------- Количество -------------\n"
		    + "Игроков: " + String.valueOf(activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getPlayerCount()) + "\n"
		    + "Мафий: " + String.valueOf(activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getMafiaCount()) + "\n"
		    + "Докторов: " + String.valueOf(activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getDoctorCount()) + "\n"
		    + "Шерифов: " + String.valueOf(activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getSheriffCount()) + "\n"
		    + "Мирных: " + String.valueOf(activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getNeutralCount()) + "\n"
		    + "\n------------- Время -------------\n"
		    + "На ночь: " + String.valueOf(activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getNightDuration()) + "\n"
		    + "На день: "  + String.valueOf(activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getDayDuration()) + "\n"
		    + "На обсуждение: " + String.valueOf(activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getDiscussionTime()) + "\n"
		    + "На голосование: " + String.valueOf(activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getVotingTime()) + "\n"
		    + "\n------------- Другое -------------\n"
		    + "Показ роми после смерти: " + String.valueOf(activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getShowRolesDeath()) + "\n"
		    + "Последние слово: " + String.valueOf(activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getLastWords()) + "\n"
		    + "Анонимные голосования: " + String.valueOf(activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getAnonymoVoting()) + "\n";
	        bot.execute(new SendMessage(upd.message().from().id(), str).parseMode(ParseMode.Markdown));	
		}
		catch (NullPointerException e)
		{
			System.out.println(e.getMessage());
			bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !").parseMode(ParseMode.Markdown));	
			return;
		}
	}
	
	private	void TestSessionsStart(Database db, TelegramBot bot, Update upd) throws SQLException
	{	
        Player asW = new Player();
        asW = db.getUserTgID(upd.message().from().id());
		
        if(asW.getUserID() != 1622884185)
        {
        	bot.execute(new SendMessage(upd.message().from().id(), "У ВАС НЕТУ ПРАВ НА ИСПОЛЬЗОВАНИЕ ДАННОЙ КОМАНДЫ !").parseMode(ParseMode.Markdown));
        	return;
        }
        
		String[] agrc = upd.message().text().split(" ");
	    int sessionCount = 5; 
	    int minPlayers = 1;   
	    
	    try
	    {
		    if (agrc.length > 1) 
		    {
		        sessionCount = Integer.parseInt(agrc[1]);
		    }
		    
		    if (agrc.length > 2) 
		    {
		        minPlayers = Integer.parseInt(agrc[2]);
		    }
	    }
	    catch (NumberFormatException e)
	    {
	    	System.out.println(e.getMessage());
	    	bot.execute(new SendMessage(upd.message().from().id(), "Давай строки не будем пихать пока я не сделал нормальный обработчик ?)").parseMode(ParseMode.Markdown));
	    	return;
	    }
		
		for (int i = 0; i < sessionCount; ++i) 
		{
			Session newSession = new Session();
			String lobbyCode = Long.toString(newSession.NewSession());
			
			do {
			    lobbyCode = Long.toString(newSession.NewSession());
			} while (activeSessions.containsKey(lobbyCode));
			
			activeSessions.put(lobbyCode, newSession);
			
			int playersInSession = minPlayers + i;
			System.out.println("\nCreate session #" + (i + 1) + " ID: " + lobbyCode + " wtih " + playersInSession + " players");
			
	        for (int j = 0; j < playersInSession; j++) 
	        { 
	            Player plr = new Player();
	            int uniqueId = i * 10 + j;
	            plr.setUserDB(uniqueId, ((long) uniqueId), ("User_" + i + "_" + j), ("User_" + i + "_" + j));
	            
	            activeSessions.get(lobbyCode).AddPlayer(plr);
	            
	            System.out.println("  Игрок: User_" + i + "_" + j + " (ID: " + uniqueId + ")");
	        }
		}
	}

	private	void TestSessionsEnd(Database db, TelegramBot bot, Update upd) throws SQLException
	{	
        Player plr = new Player();
        plr = db.getUserTgID(upd.message().from().id());
		
        if(plr.getUserID() == 1622884185)
			activeSessions.clear();
        else
        	bot.execute(new SendMessage(upd.message().from().id(), "У ВАС НЕТУ ПРАВ НА ИСПОЛЬЗОВАНИЕ ДАННОЙ КОМАНДЫ !").parseMode(ParseMode.Markdown));
	}
	
/*	private boolean existPlayerSession(long plrID)
	{
		if (activeSessions.isEmpty())
			return false;
		
		for (Entry<String, Session> list : activeSessions.entrySet())
		{		
			if(list.getValue().getPlayer().containsKey(plrID))
				return true;
		}
		
		return false;
	} */

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
						str += "----------------------------------\n"
						+ "Индификатор: `" + String.valueOf(list.getValue().getSessionID()) + "`\n";	
						
						Map<Long, Player> plr = list.getValue().getPlayer();
						if (plr.isEmpty()) 
						{
							str += "Нету игроков !";
						}
						else
						{
							str += "Игроки в игре:\n";
							for (Entry<Long, Player> plrs : list.getValue().getPlayer().entrySet())
							{
								str += "\t\t\tНик: `" + plrs.getValue().getGameUserNick() + "`\n" 
									 + "\t\t\tИндификатор: `" + plrs.getValue().getPublicID() + "`\n";
							}
						}
					}
				break;
				case "players":
					boolean found = false;
	                
	                for (Entry<String, Session> list : activeSessions.entrySet()) 
	                {
	                    Session session = list.getValue();

	                    for (Entry<Long, Player> plrs : session.getPlayer().entrySet()) 
	                    {
	                        if (plrs.getValue().getUserID() == upd.message().from().id()) 
	                        {
	                            found = true;
	                            str += "----------------------------------\n";
	                            for (Entry<Long, Player> plr : session.getPlayer().entrySet()) 
	                            {
									str += 	"Игроки в игре:\n" 
										    + "\t\t\tНик: `" + plr.getValue().getGameUserNick() + "`\n" 
										    + "\t\t\tИндификатор: `" + plr.getValue().getPublicID() + "`\n";
	                            }
	                            break;
	                        }
	                    }
	                }
	                
	                if (!found) 
	                {
	                	str += "Вы не участвуете в активных играх";
	                }
	                
	                bot.execute(new SendMessage(upd.message().from().id(), str.toString()).parseMode(ParseMode.Markdown));
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
	
	private void Settings (Database db, TelegramBot bot, Update upd)  throws Exception
	{	
		SettingsCommandHandler setComHand = new SettingsCommandHandler();
    	String[] agrc = upd.message().text().split(" ");
		
		setComHand.execute(agrc, db, bot, upd, activeSessions);
	}
	
    public void execute (String[] mgs, Database db, TelegramBot bot, Update upd) throws Exception 
    {
    	String command = mgs[0];
    	CommandInterface _command = commands.get(command);
        
    	if (_command != null) 
            _command.execute(db, bot, upd);
    	else
    		bot.execute(new SendMessage(upd.message().from().id(), "Нет такой команды !"));
    }
}
