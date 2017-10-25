package utils;

import models.Chat;

import java.util.ArrayList;
import java.util.HashMap;

public class ChatManager {

    private static ChatManager instance;
    private final HashMap<Integer, Chat> chatsByMatchId;
    private static int chatIndex = 0;

    private ChatManager() {
        this.chatsByMatchId = new HashMap<>();
    }

    public static ChatManager sharedInstance() {
        if (instance == null)
            instance = new ChatManager();
        return instance;
    }

    public Chat addChat(int matchId) {
        Chat chat = chatsByMatchId.get(matchId);
        if (chat != null)
            return chat;

        chat = new Chat(chatIndex, matchId);
        chatsByMatchId.put(matchId, chat);
        chatIndex++;
        return chat;
    }

    public void removeChat(int matchId) {
        chatsByMatchId.remove(matchId);
    }

    public ArrayList<Chat> getUsers() {
        return new ArrayList<Chat>(chatsByMatchId.values());
    }

    public Chat getOrCreateChat(int matchId) {
        Chat chat = chatsByMatchId.get(matchId);
        if (chat == null)
            chat = this.addChat(matchId);
        return chat;
    }

}
