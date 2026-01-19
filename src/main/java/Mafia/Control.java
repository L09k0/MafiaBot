package Mafia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;

@SuppressWarnings("deprecation")
public class Control 
{
	// bot
	private TelegramBot bot;
	private Update update;
	
	// Control
	private long id_lobby = 0;
	private Database db;
	private Map<String, Session> activeSessions = new HashMap<>();

	private String getBotToken() throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		//System.out.println(currentDirectory+"\\token.txt");
		BufferedReader buff = new BufferedReader(new FileReader(currentDirectory+"\\token.txt"));
		String token = buff.readLine();
		buff.close();
        return token.toString();
	}
	
	public void Run() throws IOException
	{
		 System.out.println("Initializing bot\n");
		 
		 db = new Database("Mafia");
		 db.Connect();
		 db.CreateTable();
		 
		 bot = new TelegramBot(getBotToken().toString());
		 System.out.println("Initializing bot successfully !\n");
		 
		 bot.setUpdatesListener(updates -> {
		 	try 
		     {
		     	for (Update _updates : updates) 
		         {
		     		 this.update = _updates;
		     		 
		         	 if (update.message() != null && update.message().text() != null)
		         	 {
						System.out.println(update.message().from().firstName()+": "+update.message().text());
						 
						String messageText = update.message().text();
						String[] agrc = messageText.split(" ");	
						String command = agrc[0]; 
						
						long chatId = update.message().chat().id();
						
						if(!db.userExists(update.message().from().id()))
							db.AddUserdb(update.message().from().id(), update.message().from().firstName().toString());
						
						switch (command) 
						{
							// Start
							case "/start":
								Start();
							break;

							// help 
							case "/help":
								Helper();
							break;
	
							// People use
							case "/join":
								if(agrc.length > 1)
								{
									join(agrc[1]);
								}
								else
									bot.execute(new SendMessage(chatId, "Введите индификатор лобби !" + String.valueOf(id_lobby)));
							break;
							case "/vote":
								Vote();
							break;
							case "/kill":
								Kill();
							break;
							case "/cure":
								Cure();
							break;
							case "/check":
								Check();
							break;
							case "/leave":
								Leave();
							break;
							case "/profile":
								Profile();
							break;
							case "/editprofile":
								if(agrc.length > 1)
								{
									EditProfile(agrc[1]);
								}
								else 
								{
									bot.execute(new SendMessage(chatId, "Введите новый ник !"));
								}
							break;
							case "/list":
								ListManager(agrc);
							break;
							
							// Leading use 
							case "/step":
								Step();
							break;
							case "/create":
								Create();
							break;
							case "/settings":
								Settings();
							break;
							case "/startGame":
								StartGame();
							break;
							case "/end":
								EndGame();
							break;
							// Default
							default:
								bot.execute(new SendMessage(chatId, "Напиши /help чтобы узнать список команд !"));
							break;
						}
		         	 }
		         }
		     } 
		     catch (Exception e) 
		     {
		             e.printStackTrace();
		     }
		     return UpdatesListener.CONFIRMED_UPDATES_ALL;
		 });
	}
	
	private void Start() throws SQLException
	{
		if(!db.userExists(update.message().from().id()))
			db.AddUserdb(update.message().from().id(), update.message().from().firstName().toString());
	}
	
	private void Helper()
	{
		String str = "Вы можете использовать\n"
				+ "/help - Помощь в управлении\n"
				+ "/create - Создание лобби\n"
				+ "/end - Закончить игру\n"
				+ "/start - Начать игру\n"
				+ "/leave - Покинуть лобби/игру\n"
				+ "/join - Присоединиться в лобби\n"
				+ "/settings - Настройки лобби/игры\n"
				+ "/step - Управление ходом\n"
				+ "/vote - Голосование\n"
				+ "/kill - Убить игрока\n"
				+ "/cure - Лечить игрока\n"
				+ "/check - Проверить игрока\n"
				+ "/profile - Посмотреть ваш профиль\n"
				+ "/editprofile - Редактировать ваш профиль\n";
		
		bot.execute(new SendMessage(update.message().from().id(), str));
	}

	private void ListManager(String[] agrc) 
	{
		String str = "";
		
		if(agrc.length > 1)
		{
			switch (agrc[1])
			{
				case "session":
					if(activeSessions.values().isEmpty())
					{
						bot.execute(new SendMessage(update.message().from().id(), "Нету активных игр !"));
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
				default:
					str += "Неправильный аргумент !";
				break;
			}
			
			bot.execute(new SendMessage(update.message().from().id(), str).parseMode(ParseMode.Markdown));
		}
		else
			bot.execute(new SendMessage(update.message().from().id(), "Напиши /help чтобы узнать список команд !"));	
	}
	
	private void Vote()
	{
		bot.execute(new SendMessage(update.message().from().id(), "Vote !"));
	}
	
	private void Kill()
	{
		bot.execute(new SendMessage(update.message().from().id(), "kill !"));
	}
	
	private void Cure()
	{
		bot.execute(new SendMessage(update.message().from().id(), "Cure !"));
	}
	
	private void Check()
	{
		bot.execute(new SendMessage(update.message().from().id(), "Check !"));
	}
	
	private void Leave()
	{
		bot.execute(new SendMessage(update.message().from().id(), "Leave !"));
	}
	
	private void Profile() throws SQLException
	{
		Player pop = new Player();	
		pop = db.getUserTgID(update.message().from().id());
		
		String str = "*Ваш профиль:*\n\n"
				+ "Ник: `" + pop.getGameUserNick() + "`\n"
				+ "Индификатор: `" + pop.getPublicID() + "`";
		
		bot.execute(new SendMessage(update.message().from().id(), str).parseMode(ParseMode.Markdown));
	}
	
	private void EditProfile(String chenNick)
	{
		if(db.setUserEditProfile(chenNick, update.message().from().id()))
			bot.execute(new SendMessage(update.message().from().id(), "Ник изменен !"));
		else
			bot.execute(new SendMessage(update.message().from().id(), "Не удалось поменять ник !"));
	}
	
	private void Step()
	{
		bot.execute(new SendMessage(update.message().from().id(), "Step !"));
	}
	
	private void StartGame()
	{
		bot.execute(new SendMessage(update.message().from().id(), "Start !"));
	}

	private void EndGame()
	{
		bot.execute(new SendMessage(update.message().from().id(), "EndGame !"));
	}
	
	private void Settings()
	{
		bot.execute(new SendMessage(update.message().from().id(), "Creating new lobby dfdsf !" + id_lobby));	
		
	}
	
	private void Create() throws SQLException 
	{
		Player plr = new Player();
		plr = db.getUserTgID(update.message().from().id());
		
		for (Entry<String, Session> list : activeSessions.entrySet())
		{
			for (Entry<Long, Player> pl : list.getValue().getPlayer().entrySet())
			{
				if(pl.getValue().getUserID() == update.message().from().id() || pl.getValue().getUserID() == 0)
				{
					bot.execute(new SendMessage(update.message().from().id(), "Вы уже в игре ! `" + String.valueOf(list.getValue().getSessionID()) + "`").parseMode(ParseMode.Markdown));
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
		
		bot.execute(new SendMessage(update.message().from().id(), "Creating new lobby `" + lobbyCode + "`").parseMode(ParseMode.Markdown));
	}
	
	private void join(String lobbyCode) throws SQLException
	{
		long chatId = update.message().chat().id();
		
		if (activeSessions.containsKey(lobbyCode)) 
		{	
			Player plr = new Player();
			
			plr = db.getUserTgID(update.message().from().id());
			
			if (activeSessions.get(lobbyCode).AddPlayer(plr))
				bot.execute(new SendMessage(chatId, "Ты присоединился в лобби !"));
			else 
				bot.execute(new SendMessage(chatId, "Не удалось присоединился в лобби !"));
	    } 
		else 
		{
	        bot.execute(new SendMessage(chatId, "Лобби не найдено !"));
	    }
	}
}




























