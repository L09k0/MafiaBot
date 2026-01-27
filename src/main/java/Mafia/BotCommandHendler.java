package Mafia;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

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
		CommandRegister("/close", this::Close);
		CommandRegister("/join", this::join);
		CommandRegister("/leave", this::Leave);
		CommandRegister("/list", this::List);
		CommandRegister("/kill", this::Kill);
		CommandRegister("/cure", this::Cure);
		CommandRegister("/check", this::Check);
		CommandRegister("/profile", this::Profile);
		CommandRegister("/editprofile", this::EditProfile);
		CommandRegister("/startgame", this::StartGame);
		CommandRegister("/endgame", this::EndGame);
		CommandRegister("/settings", this::Settings);
		CommandRegister("/vote", this::Vote);
		CommandRegister("/step", this::Step);
		CommandRegister("/tests", this::TestSessionsStart);
		CommandRegister("/teste", this::TestSessionsEnd);
		CommandRegister("/addtestplayer", this::AddTestPlayers);
		CommandRegister("/lobby", this::ShowLobbyInfo);
		CommandRegister("/setrole", this::SetRole);
		CommandRegister("/setrandomrole", this::SetRandomRole);
	}

	private void CommandRegister (String name, CommandInterface command)
	{
		commands.put(name, command);	
	}
	
	private	void Close(Database db, TelegramBot bot, Update upd) throws SQLException
	{
		if (!existPlayerSession(upd.message().from().id()))
		{
			bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !").parseMode(ParseMode.Markdown));
			return;
		}  
	
		Player plr = db.getUserTgID(upd.message().from().id());
		String lobbyid = getActiveSessionID(plr.getPublicID());

		if(lobbyid == null)
		{
			bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !").parseMode(ParseMode.Markdown));
			return;
		}
		
		if (upd.message().from().id() != activeSessions.get(lobbyid).getLeaderGame().getUserID())
		{
			bot.execute(new SendMessage(upd.message().from().id(), "Вы не ведущий !").parseMode(ParseMode.Markdown));
			return;
		}

        if (activeSessions.get(lobbyid) != null) 
        {
        	Map<Long, Player> playersToNotify = new HashMap<>(activeSessions.get(lobbyid).getPlayer());
        	
        	activeSessions.get(lobbyid).closeSession(); 
            activeSessions.remove(lobbyid); 
            System.out.println("Сессия " + lobbyid + " удалена из активных");
            
            for (Entry<Long, Player> plrs : playersToNotify.entrySet())
            {
            	bot.execute(new SendMessage(plrs.getValue().getUserID(), "Сессия `" + lobbyid + "` удалена !").parseMode(ParseMode.Markdown));            	
            }
        }
	}
	
	private	void SetRandomRole(Database db, TelegramBot bot, Update upd) throws SQLException
	{
		if (!existPlayerSession(upd.message().from().id()))
		{
			bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !").parseMode(ParseMode.Markdown));
			return;
		}  
	
		Player plr = db.getUserTgID(upd.message().from().id());
		String lobbyid = getActiveSessionID(plr.getPublicID());

		if(lobbyid == null)
		{
			bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !").parseMode(ParseMode.Markdown));
			return;
		}
		
		if (upd.message().from().id() != activeSessions.get(lobbyid).getLeaderGame().getUserID())
		{
			bot.execute(new SendMessage(upd.message().from().id(), "Вы не ведущий !").parseMode(ParseMode.Markdown));
			return;
		}
		
		activeSessions.get(lobbyid).assignRandomRoles();
		
		bot.execute(new SendMessage(upd.message().from().id(), "Рандомные роли рассчитаны !").parseMode(ParseMode.Markdown));
	}
	
	private	void SetRole(Database db, TelegramBot bot, Update upd) throws SQLException
	{
		try
		{
			if (!existPlayerSession(upd.message().from().id()))
			{
				bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !").parseMode(ParseMode.Markdown));
				return;
			}  
		
			Player plr = db.getUserTgID(upd.message().from().id());
			String lobbyid = getActiveSessionID(plr.getPublicID());
	
			if(lobbyid == null)
			{
				bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !").parseMode(ParseMode.Markdown));
				return;
			}
			
			if (upd.message().from().id() != activeSessions.get(lobbyid).getLeaderGame().getUserID())
			{
				bot.execute(new SendMessage(upd.message().from().id(), "Вы не ведущий !").parseMode(ParseMode.Markdown));
				return;
			}
			
			String[] agrc = upd.message().text().split(" ");
			
			if (agrc.length > 1)
			{
				long plrID = Long.valueOf(agrc[2]);
				
				if (agrc.length > 2)
				{
					switch (agrc[1]) 
					{
					case "mafia":
						activeSessions.get(lobbyid).setRole(plrID, PlayerRole.MAFIA);
						
			            for (Entry<Long, Player> plrs : activeSessions.get(lobbyid).getPlayer().entrySet())
			            {
			            	if (plrs.getValue().getPublicID() == plrID)
			            		bot.execute(new SendMessage(plrs.getValue().getUserID(), "Ведущий назначил вас на роль мафии !").parseMode(ParseMode.Markdown));            	
			            }
					break;
					case "doctor":
						activeSessions.get(lobbyid).setRole(plrID, PlayerRole.DOCTOR);
			           
						for (Entry<Long, Player> plrs : activeSessions.get(lobbyid).getPlayer().entrySet())
			            {
			            	if (plrs.getValue().getPublicID() == plrID)
			            		bot.execute(new SendMessage(plrs.getValue().getUserID(), "Ведущий назначил вас на роль доктора !").parseMode(ParseMode.Markdown));            	
			            }
					break;
					case "sheriff":
						activeSessions.get(lobbyid).setRole(plrID, PlayerRole.SHERIFF);	
						
			            for (Entry<Long, Player> plrs : activeSessions.get(lobbyid).getPlayer().entrySet())
			            {
			            	if (plrs.getValue().getPublicID() == plrID)
			            		bot.execute(new SendMessage(plrs.getValue().getUserID(), "Ведущий назначил вас на роль шерифа !").parseMode(ParseMode.Markdown));            	
			            }
					break;
					case "neutral":
						activeSessions.get(lobbyid).setRole(plrID, PlayerRole.NEUTRAL);	
						
			            for (Entry<Long, Player> plrs : activeSessions.get(lobbyid).getPlayer().entrySet())
			            {
			            	if (plrs.getValue().getPublicID() == plrID)
			            		bot.execute(new SendMessage(plrs.getValue().getUserID(), "Ведущий назначил вас на роль мирного жителя !").parseMode(ParseMode.Markdown));            	
			            }
					break;
					default:
						bot.execute(new SendMessage(upd.message().from().id(), "Нет такой роли !").parseMode(ParseMode.Markdown));
					break;
					}
				}
				else
				{
					bot.execute(new SendMessage(upd.message().from().id(), "Игрок не найден !").parseMode(ParseMode.Markdown));
					return;
				}
			}
			else
			{
				String str = "*Список всех ролей для выбора:*\n"
						  +  "Мифия: `mafia`\n"
						  +  "Доктор: `doctor`\n"
						  +  "Шериф: `sheriff`\n"
						  +  "Мирный житель: `neutral`\n";
				
				
				bot.execute(new SendMessage(upd.message().from().id(), str).parseMode(ParseMode.Markdown));
				return;
			}
		}
		catch (NullPointerException e)
		{
			System.out.println(e.getMessage());
			bot.execute(new SendMessage(upd.message().from().id(), "Игрок не найден или нет такой роли !").parseMode(ParseMode.Markdown));
			return;
		}
	}
	
	private	void ShowLobbyInfo(Database db, TelegramBot bot, Update upd) throws SQLException
	{	
		try
		{
			String str = "";
			Player curreplr = new Player();
			curreplr = db.getUserTgID(upd.message().from().id());
			
			str += "------------- Лобби -------------\n";
			str += 	"Индификатор игры: `" + getActiveSessionID(curreplr.getPublicID()) + "`\n";

			for (Entry<String, Session> list : activeSessions.entrySet()) 
	        {		
	            for (Entry<Long, Player> plrs : list.getValue().getPlayer().entrySet()) 
	            {
	                if (plrs.getValue().getUserID() == upd.message().from().id()) 
	                {
	                    str += "----------------------------------\n"
	                         + "*Игроки в игре:*\n";
	                    
	                    if (activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getPlayer().size() < 4)     
	                    	str +=  "\t\t\tВсего в игре: " + activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getPlayer().size() + " *(Не хватает игроков !)*\n";	                    	
	                    else
	                    	str +=  "\t\t\tВсего в игре: " + activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getPlayer().size() + "\n";
	                    
	                    for (Entry<Long, Player> plr : list.getValue().getPlayer().entrySet()) 
	                    {
	                    	str += "----------------------------------\n";
	                    	if (plr.getValue().getPublicID() == activeSessions.get(list.getKey()).getLeaderGame().getPublicID())
	                    	{
	                    		str += "\t\t\tНик: `" + plr.getValue().getGameUserNick() + "` (Ведущий)\n"
	                    			 + "\t\t\tИндификатор: `" + plr.getValue().getPublicID() + "`\n";

	                    		if (upd.message().from().id() == activeSessions.get(list.getKey()).getLeaderGame().getUserID())
                    			{
	                    			str += "\t\t\tРоль: `" + plr.getValue().getRole() + "`\n";
                    			}
	                    	}
	                    	else
	                    	{
	                    		str += "\t\t\tНик: `" + plr.getValue().getGameUserNick() + "`\n"
	                    			 + "\t\t\tИндификатор: `" + plr.getValue().getPublicID() + "`\n";
	                    		
 	                    		if (upd.message().from().id() == activeSessions.get(list.getKey()).getLeaderGame().getUserID())
                    			{
	                    			str += "\t\t\tРоль: `" + plr.getValue().getRole() + "`\n";
                    			}
	                    	}
	                    }
	                    break;
	                }
	            }
	        }
	        
			
	        str += "\n*Список настроек игры:*\n"
	        + "------------- Количество -------------\n"
		    + "Макс. игроков: " + String.valueOf(activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getPlayerCount()) + "\n"
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
		    + "Показ роли после смерти: " + String.valueOf(activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getShowRolesDeath()) + "\n"
		    + "Последние слово: " + String.valueOf(activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getLastWords()) + "\n"
		    + "Анонимные голосования: " + String.valueOf(activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getAnonymoVoting()) + "\n"
		    + "Ведущий выбирает роли: " + String.valueOf(activeSessions.get(getActiveSessionID(curreplr.getPublicID())).getLeaderChoosesRoles()) + "\n";
	        bot.execute(new SendMessage(upd.message().from().id(), str).parseMode(ParseMode.Markdown));	
		}
		catch (NullPointerException e)
		{
			System.out.println(e.getMessage());
			return;
		}
	}

	private	void AddTestPlayers(Database db, TelegramBot bot, Update upd) throws Exception
	{	
        Player asW = new Player();
        asW = db.getUserTgID(upd.message().from().id());
		
        if(asW.getUserID() != 1622884185)
        {
        	bot.execute(new SendMessage(upd.message().from().id(), "У ВАС НЕТУ ПРАВ НА ИСПОЛЬЗОВАНИЕ ДАННОЙ КОМАНДЫ !").parseMode(ParseMode.Markdown));
        	return;
        }
        
		String[] agrc = upd.message().text().split(" ");
	    int plrCount = 5; 
	    
	    try
	    {
		    if (agrc.length > 1) 
		    {
		    	plrCount = Integer.parseInt(agrc[1]);
		    }
	    }
	    catch (NumberFormatException e)
	    {
	    	System.out.println(e.getMessage());
	    	bot.execute(new SendMessage(upd.message().from().id(), "Давай строки не будем пихать пока я не сделал нормальный обработчик ?)").parseMode(ParseMode.Markdown));
	    	return;
	    }

        for (int i = 0; i < plrCount; i++) 
        { 
            Player plr = new Player();
            int uniqueId = i * 10 + i;
            plr.setUserDB(uniqueId, ((long) uniqueId), ("User_" + i), ("User_" + i));
            
            activeSessions.get(getActiveSessionID(asW.getPublicID())).AddPlayer(plr);
            
            System.out.println("Игрок: User_" + i + "_" + "(ID: " + uniqueId + ")");
        }
	}
	
	private	void TestSessionsStart(Database db, TelegramBot bot, Update upd) throws Exception
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
	
	private boolean existPlayerSession(long tgplrID)
	{
		if (activeSessions.isEmpty())
			return false;
		
	    for (Session list : activeSessions.values()) 
	    {
	        for (Player plr : list.getPlayer().values()) 
	        {
	            if (plr.getUserID() == tgplrID) 
	            {
	                return true;
	            }
	        }
	    }
		
		return false;
	} 

	private String getActiveSessionID(long pPlrID)
	{
		if (activeSessions.isEmpty())
			return null;
		
		for (Entry<String, Session> list : activeSessions.entrySet())
		{	
	        if (list.getValue().getPlayer().containsKey(pPlrID)) 
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
    
    private void Create (Database db, TelegramBot bot, Update upd) throws Exception 
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
		activeSessions.get(lobbyCode).setLobby();
		activeSessions.get(lobbyCode).AddPlayer(plr);
		activeSessions.get(lobbyCode).setLeaderGame(plr);
		
		bot.execute(new SendMessage(upd.message().from().id(), "*Лобби для игры создано !*\n"
				+ "Индификатор: `" + lobbyCode + "`").parseMode(ParseMode.Markdown));
    }
    
    private void join (Database db, TelegramBot bot, Update upd) throws Exception 
    {
		long chatId = upd.message().chat().id();
		String[] lobbyCode = upd.message().text().split(" ");	
		
		if (existPlayerSession(upd.message().from().id()))
		{
			bot.execute(new SendMessage(chatId, "Ты уже в игре, напиши /leave чтобы покинуть игру !"));
			return;
		}
		
		if(lobbyCode.length > 1)
		{
			if (activeSessions.containsKey(lobbyCode[1])) 
			{	
				Player plr = new Player();
				
				plr = db.getUserTgID(upd.message().from().id());
				
				if (activeSessions.get(lobbyCode[1]).AddPlayer(plr))
				{
					for (Entry<Long, Player> plrs : activeSessions.get(lobbyCode[1]).getPlayer().entrySet())
			        {
						System.out.println(plrs.getValue().getGameUserNick() + " присоединился в лобби \n");
			        	bot.execute(new SendMessage(plrs.getValue().getUserID(), "`" + plr.getGameUserNick() + "` присоединился в лобби !").parseMode(ParseMode.Markdown));				        	
			        }
				}
				else 
					bot.execute(new SendMessage(chatId, "Не удалось присоединился в лобби !"));
		    } 
			else 
			{
		        bot.execute(new SendMessage(chatId, "Лобби не найдено !"));
		        return;
		    }
		}
		else 
		{
			bot.execute(new SendMessage(chatId, "Лобби не найдено !"));
			return;
		}
    } 
    
    private void Leave (Database db, TelegramBot bot, Update upd) 
    {
    	if (!existPlayerSession(upd.message().from().id())) 
    	{
            bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !"));
            return;
        }
        
        String sessionKey = null;
        Long playerKey = null;
        String playerName = null;
        Session targetSession = null;
        
        for (Entry<String, Session> list : activeSessions.entrySet()) 
        {
            for (Entry<Long, Player> plr : list.getValue().getPlayer().entrySet()) 
            {
                if (plr.getValue().getUserID() == upd.message().from().id()) 
                {
                    sessionKey = list.getKey();
                    playerKey = plr.getKey();
                    playerName = plr.getValue().getGameUserNick();
                    targetSession = list.getValue();
                    break;
                }
            }
            
            if (sessionKey != null) 
            	break;
        }
        
        if (targetSession == null) 
        {
            bot.execute(new SendMessage(upd.message().from().id(), "Ошибка targetSession = null ! "));
            return;
        }
        

        if (targetSession.getLeaderGame() != null && targetSession.getLeaderGame().getUserID() == upd.message().from().id()) 
        {
            java.util.List<Player> otherPlayers = targetSession.getPlayer().values().stream().filter(p -> p.getUserID() != upd.message().from().id()).collect(Collectors.toList());
            
            if (!otherPlayers.isEmpty()) 
            {
                Player newLeader = otherPlayers.get(ThreadLocalRandom.current().nextInt(otherPlayers.size()));
                targetSession.setLeaderGame(newLeader);
                bot.execute(new SendMessage(newLeader.getUserID(), "Вы теперь ведущий игры !"));
            }
        }
        
        targetSession.RemovePlayer(playerKey);
        
        for (Player remainingPlayer : targetSession.getPlayer().values()) 
        {
            bot.execute(new SendMessage(remainingPlayer.getUserID(), "`" + playerName + "` вышел из игры !").parseMode(ParseMode.Markdown));
        }
        
        if (targetSession.getPlayer().isEmpty()) 
        {
            activeSessions.remove(sessionKey);
            bot.execute(new SendMessage(upd.message().from().id(), "Сессия удалена (не осталось игроков) !"));
        }
        else 
        {
            bot.execute(new SendMessage(upd.message().from().id(), "Вы вышли из игры !"));
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
								str += "\t\t\tНик: `" + plrs.getValue().getGameUserNick() + "`\n" ;
									// + "\t\t\tРоль: `" + plrs.getValue().getRole() + "`\n";
								   //+ "\t\t\tИндификатор: `" + plrs.getValue().getPublicID() + "`\n";
							}
						}
					}
				break;
				case "players":
					if (!existPlayerSession(upd.message().from().id()))
					{
						bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !").parseMode(ParseMode.Markdown));
						return;
					}
	              
	                for (Entry<String, Session> list : activeSessions.entrySet()) 
	                {
	                    Session session = list.getValue();

	                    for (Entry<Long, Player> plrs : session.getPlayer().entrySet()) 
	                    {
	                        if (plrs.getValue().getUserID() == upd.message().from().id()) 
	                        {
	                        		str +=   "*Игроки в игре:*\n";
	         	                    for (Entry<Long, Player> plr : list.getValue().getPlayer().entrySet()) 
	         	                    {
	         	                    	str += "----------------------------------\n";
	         	                    	if (plr.getValue().getPublicID() == activeSessions.get(list.getKey()).getLeaderGame().getPublicID())
	         	                    	{
	         	                    		str += "\t\t\tНик: `" + plr.getValue().getGameUserNick() + "` (Ведущий)\n"
	         	                    			 + "\t\t\tИндификатор: `" + plr.getValue().getPublicID() + "`\n";

	         		                    		if (upd.message().from().id() == activeSessions.get(list.getKey()).getLeaderGame().getUserID())
	         	                    			{
	         		                    			str += "\t\t\tРоль: `" + plr.getValue().getRole() + "`\n";
	         	                    			}
	         	                    	}
	         	                    	else
	         	                    	{
	         	                    		str += "\t\t\tНик: `" + plr.getValue().getGameUserNick() + "`\n"
	         	                    			 + "\t\t\tИндификатор: `" + plr.getValue().getPublicID() + "`\n";

	         		                    		if (upd.message().from().id() == activeSessions.get(list.getKey()).getLeaderGame().getUserID())
	         	                    			{
	         		                    			str += "\t\t\tРоль: `" + plr.getValue().getRole() + "`\n";
	         	                    			}
	         	                    	}
	         	                    }
	         	                    break;
	                        }
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
		String str = "----------------------------------------------\n@ - Вы\n0 - Выход на этажи\n\n                         \n                         \n                         \n                         \n                         \n                         \n                         \n                         \n                         \n                         \n          ##  #          \n          ###@##         \n           #0###         \n              ##         \n               #         \n               #         \n               #         \n                         \n                         \n                         \n                         \n                         \n                         \n                         \n                         \n";		
		bot.execute(new SendMessage(upd.message().from().id(), str).parseMode(ParseMode.Markdown));
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
	
	private void Step (Database db, TelegramBot bot, Update upd) throws SQLException 
	{
		if (!existPlayerSession(upd.message().from().id()))
		{
			bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !").parseMode(ParseMode.Markdown));
			return;
		}  
	
		Player plr = db.getUserTgID(upd.message().from().id());
		String lobbyid = getActiveSessionID(plr.getPublicID());
		
		if(lobbyid == null)
			bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !").parseMode(ParseMode.Markdown));

		if (upd.message().from().id() != activeSessions.get(lobbyid).getLeaderGame().getUserID())
		{
			bot.execute(new SendMessage(upd.message().from().id(), "Вы не ведущий !").parseMode(ParseMode.Markdown));
			return;
		}
		
		activeSessions.get(lobbyid).Step();
	}
	
	private void StartGame (Database db, TelegramBot bot, Update upd) throws SQLException 
	{
		if (!existPlayerSession(upd.message().from().id()))
		{
			bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !").parseMode(ParseMode.Markdown));
			return;
		}  
	
		Player plr = db.getUserTgID(upd.message().from().id());
		String lobbyid = getActiveSessionID(plr.getPublicID());

		if(lobbyid == null)
		{
			bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !").parseMode(ParseMode.Markdown));
			return;
		}
		
		if (upd.message().from().id() != activeSessions.get(lobbyid).getLeaderGame().getUserID())
		{
			bot.execute(new SendMessage(upd.message().from().id(), "Вы не ведущий !").parseMode(ParseMode.Markdown));
			return;
		}
		
		try 
		{
			activeSessions.get(lobbyid).StartGame();		
		}
		catch (Exception e)
		{
			bot.execute(new SendMessage(upd.message().from().id(), e.getMessage()).parseMode(ParseMode.Markdown));
		}
	}

	private void EndGame (Database db, TelegramBot bot, Update upd) throws SQLException 
	{
		if (!existPlayerSession(upd.message().from().id()))
		{
			bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !").parseMode(ParseMode.Markdown));
			return;
		}  
	
		Player plr = db.getUserTgID(upd.message().from().id());
		String lobbyid = getActiveSessionID(plr.getPublicID());

		if(lobbyid == null)
		{
			bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !").parseMode(ParseMode.Markdown));
			return;
		}
		
		if (upd.message().from().id() != activeSessions.get(lobbyid).getLeaderGame().getUserID())
		{
			bot.execute(new SendMessage(upd.message().from().id(), "Вы не ведущий !").parseMode(ParseMode.Markdown));
			return;
		}

		activeSessions.get(lobbyid).EndGame();	
	}
	
	private void Settings (Database db, TelegramBot bot, Update upd)  throws Exception
	{	
		if (!existPlayerSession(upd.message().from().id()))
		{
			bot.execute(new SendMessage(upd.message().from().id(), "Вас нету в игре !").parseMode(ParseMode.Markdown));
			return;
		}
		
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
