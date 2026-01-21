package Mafia;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;

public interface CommandInterface 
{
	void execute(Database db, TelegramBot bot, Update upd) throws Exception;
}
