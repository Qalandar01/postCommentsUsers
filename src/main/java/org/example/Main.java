package org.example;

import org.example.bot.BotController;

public class Main {
    static {
        DB.upload();
    }
    public static void main(String[] args) {

        BotController.start();

    }
}