package org.example;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.bot.State;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class TgUser {
    private Long chatId;
    private State state = State.START;
    private Integer messageId;

    public TgUser(Long chatId) {
        this.chatId = chatId;
    }
}
