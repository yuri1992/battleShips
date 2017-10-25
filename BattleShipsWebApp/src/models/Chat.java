package models;

import engine.model.multi.User;

import java.util.ArrayList;

public class Chat {
    private int id;
    private int matchId;

    public int getId() {
        return id;
    }

    public int getMatchId() {
        return matchId;
    }

    public ArrayList<ChatMessage> getMessages() {
        return messages;
    }

    private ArrayList<ChatMessage> messages;

    public Chat(int id, int matchId) {
        this.id = id;
        this.matchId = matchId;
        this.messages = new ArrayList<>();
    }

    public ChatMessage addMessage(String message, User user) {
        ChatMessage out = new ChatMessage(message, user);
        messages.add(out);
        return out;
    }

}

