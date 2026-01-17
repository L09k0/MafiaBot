package Mafia;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;

@SuppressWarnings("unused")
public class Control 
{
	// bot
	private TelegramBot bot;
	private Update update;

	// People
	private int people_length;
	private People leading = new People();
	
	// Control
	private boolean day;
	private long id_lobby = 0;
	private boolean is_game; 
	
	// MOVE step
	private enum StepPlay
	{
	   MAFIA,
	   DOCTOR,
	   SHERIFF
	}

	private String getBotToken() throws IOException
	{
		String currentDirectory = System.getProperty("user.dir");
		System.out.println(currentDirectory+"\\token.txt");
		BufferedReader buff = new BufferedReader(new FileReader(currentDirectory+"\\token.txt"));
		String token = buff.readLine();
		buff.close();
        return token.toString();
	}
	
	public void Run() throws IOException
	{
		 System.out.println("Initializing bot\n");

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
						
						
						switch (command) 
						{
							// help 
							case "/help":
								Helper();
							break;
	
							// People use
							case "/join":
								if(agrc.length > 1)
								{
									if(agrc[1].equals(String.valueOf(id_lobby)))
									{
										bot.execute(new SendMessage(chatId, "Ты присоединился в лобби !"));
									}
									else
										bot.execute(new SendMessage(chatId, "Лобби не найдено !"));
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
							case "/start":
								Start();
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
				+ "/check - Проверить игрока\n";
		
		bot.execute(new SendMessage(update.message().from().id(), str));
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
	
	private void Step()
	{
		bot.execute(new SendMessage(update.message().from().id(), "Step !"));
	}
	
	private void Start()
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
	
	public void Create() 
	{
		id_lobby = ThreadLocalRandom.current().nextLong(10000, 1000000);
		bot.execute(new SendMessage(update.message().from().id(), "Creating new lobby "+id_lobby));
	}
}




























