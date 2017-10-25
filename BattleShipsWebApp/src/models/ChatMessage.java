package models;

import engine.model.multi.User;
import java.util.Date;

public class ChatMessage {
    private int id;
    private User sentBy;
    private Date sentAt;
    private String msg;
    private static int messageIndex = 0;

    public ChatMessage(String msg, User sentBy) {
        this.sentBy = sentBy;
        this.sentAt = new Date();
        this.msg = msg;
        this.id = messageIndex++;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getSentBy() {
        return sentBy;
    }

    public void setSentBy(User sentBy) {
        this.sentBy = sentBy;
    }

    public Date getSentAt() {
        return sentAt;
    }

    public void setSentAt(Date sentAt) {
        this.sentAt = sentAt;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static int getMessageIndex() {
        return messageIndex;
    }

    public static void setMessageIndex(int messageIndex) {
        ChatMessage.messageIndex = messageIndex;
    }
}
