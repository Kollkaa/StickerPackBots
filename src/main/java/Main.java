import bot.Bot;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException;

public class Main {
    public static void main(String... args) {
        System.out.println(System.getProperty("user.dir"));
        System.out.println("start");
        ApiContextInitializer.init();
        System.out.println("init");
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi();
        System.out.println("contunie");
        try//
        {
            Bot bot=new Bot();
            telegramBotsApi.registerBot(bot);


        } catch (TelegramApiRequestException e) {
            e.printStackTrace();
        }

    }

}

