package com.chat;

public class ChatGroupRef {

    private String chatGroupRefId;

    private int hashCode;

    private ChatGroupRef(Chat chat) {
        chatGroupRefId = chat.getChatId();
        hashCode = chat.hashCode();
    }

    public String getHashCode() {
        return String.valueOf(hashCode);
    }

    public String getChatRefId() {
        return chatGroupRefId;
    }
}
