package org.example.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

public class BotTest {
    public static void main(String[] args) {
        String token = "7159360255:AAF7b_jkxV6_slJ59bD_WJpi1Rilkn8iz4c";
        String chatId = "1694577531";

        TelegramBot bot = new TelegramBot(token);
        System.out.println("Bot initialized with token: " + token);

        SendMessage message = new SendMessage(chatId, "Hello from my bot!");
        System.out.println("Preparing to send message to chat ID: " + chatId);

        SendResponse response = bot.execute(message);
        System.out.println("Response received: " + response);

        if (response.isOk()) {
            System.out.println("Message sent successfully!");
        } else {
            System.out.println("Error: " + response.errorCode() + " - " + response.description());
        }
    }
}
