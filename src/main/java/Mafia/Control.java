package Mafia;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;

public class Control 
{
	// bot
	private TelegramBot bot;
	private Update update;
	
	// Control
	private Database db;
	private BotCommandHendler commandHandler = new BotCommandHendler();
	
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
		         		if(!db.userExists(update.message().from().id()))
		         			db.AddUserdb(update.message().from().id(), update.message().from().firstName().toString());

		         		System.out.println(update.message().from().firstName()+": "+update.message().text());
						String[] agrc = update.message().text().split(" ");	
						
						commandHandler.execute(agrc, db,  bot, _updates);
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
}




























