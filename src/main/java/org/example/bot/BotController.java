package org.example.bot;

import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import org.example.TgUser;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.example.bot.BotService.*;

public class BotController {

    public static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void start() {
        telegramBot.setUpdatesListener(updates -> {
            for (Update update : updates) {

                executorService.execute(() -> {

                    handleUpdate(update);
                });
            }
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private static void handleUpdate(Update update) {
        if (update.message() != null) {
            Message message = update.message();
            TgUser tgUser = getOrCreate(update.message().chat().id());
            if (message.text() != null){
                String text = message.text();
                if (text.equals("/start")){
                    showUsers(tgUser);
                }
            }
        }else if (update.callbackQuery() != null){
            CallbackQuery callbackQuery = update.callbackQuery();
            String data = callbackQuery.data();
            TgUser tgUser = getOrCreate(update.callbackQuery().from().id());
            if (tgUser.getState().equals(State.SHOW_USERS) && data.startsWith("post")){
                showUserPosts(tgUser,data);
            }else if (tgUser.getState().equals(State.SHOW_POSTS) && data.startsWith("comment")){
                showPostComments(tgUser,data);
            }else if (tgUser.getState().equals(State.SHOW_COMMENTS)){
                showUsers(tgUser);
            }
        }
    }
}
