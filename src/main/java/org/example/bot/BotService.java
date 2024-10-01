package org.example.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.request.DeleteMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import org.example.DB;
import org.example.Post;
import org.example.TgUser;


import java.util.List;
import java.util.Optional;

import static org.example.DB.*;

public class BotService {
    public static TelegramBot telegramBot = new TelegramBot("7159360255:AAF7b_jkxV6_slJ59bD_WJpi1Rilkn8iz4c");

    public static TgUser getOrCreate(Long id) {
        Optional<TgUser> first = TG_USERS.stream().filter(tgUser -> tgUser.getChatId().equals(id)).findFirst();
        if (first.isPresent()){
            return first.get();
        }else {
            TgUser tgUser = new TgUser(id);

            TG_USERS.add(tgUser);
            return tgUser;
        }
    }

    public static void showUsers(TgUser tgUser) {
        SendMessage sendMessage = new SendMessage(tgUser.getChatId(),"Users:");
        sendMessage.replyMarkup(generateUsersBtns());
        SendResponse response = telegramBot.execute(sendMessage);

        if (!response.isOk()) {
            System.out.println("Error: " + response.errorCode() + " - " + response.description());
            return;
        }
        tgUser.setMessageId(response.message().messageId());
        tgUser.setState(State.SHOW_USERS);
    }

    private static InlineKeyboardMarkup generateUsersBtns() {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        USERS.forEach(user -> {
            inlineKeyboardMarkup.addRow(new InlineKeyboardButton(user.getName()).callbackData("user/"+user.getId()),
                    new InlineKeyboardButton("Posts").callbackData("post/"+user.getId())
                    );
        });
        return inlineKeyboardMarkup;
    }

    public static void showUserPosts(TgUser tgUser, String data) {
        Integer userId = Integer.parseInt(data.split("/")[1]);
        telegramBot.execute(new DeleteMessage(tgUser.getChatId(),tgUser.getMessageId()));
        SendMessage sendMessage = new SendMessage(tgUser.getChatId(),"UserPosts:");
        sendMessage.replyMarkup(generatePostBtns(userId));
        SendResponse response = telegramBot.execute(sendMessage);
        tgUser.setMessageId(response.message().messageId());
        tgUser.setState(State.SHOW_POSTS);
    }

    private static InlineKeyboardMarkup generatePostBtns(Integer userId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        POSTS.stream().filter(post -> post.getUserId().equals(userId)).forEach(post ->
                inlineKeyboardMarkup.addRow(
                        new InlineKeyboardButton(post.getBody()).callbackData("post/"+post.getId()),
                        new InlineKeyboardButton("Comments").callbackData("comment/"+post.getId())
                )
        );
        return inlineKeyboardMarkup;
    }

    public static void showPostComments(TgUser tgUser, String data) {
        int postId = Integer.parseInt(data.split("/")[1]);
        telegramBot.execute(new DeleteMessage(tgUser.getChatId(),tgUser.getMessageId()));
        SendMessage sendMessage = new SendMessage(tgUser.getChatId(),"Post's comments");
        sendMessage.replyMarkup(generateCommentBtns(postId));
        SendResponse response = telegramBot.execute(sendMessage);
        tgUser.setMessageId(response.message().messageId());
        tgUser.setState(State.SHOW_COMMENTS);
    }

    private static InlineKeyboardMarkup generateCommentBtns(int postId) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        COMMENTS.stream().filter(comment -> comment.getPostId().equals(postId)).forEach(comment ->
                inlineKeyboardMarkup.addRow(
                        new InlineKeyboardButton(comment.getBody()).callbackData("comment/"+comment.getId())
                )
                );
        return inlineKeyboardMarkup;
    }
}
