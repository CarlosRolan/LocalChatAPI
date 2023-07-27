package com.chat;

import java.util.ArrayList;
import java.util.List;

public class Chat {

    public final static String CHAT_PREFIX = "3120";

    public static Chat createInstance(String chatId, String title, String description, List<Member> members) {
        return new Chat(chatId, title, description, members);
    }

    private final List<Member> mMembers;
    private String mId;
    private String mTitle;
    private String mDesc;

    // getters
    public String getChatId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDesc;
    }

    public String[] getMembers() {
        return (String[]) mMembers.toArray();
    }

    // setters
    public void setTitle(String newTitle) {
        mTitle = newTitle;
    }

    public void setDescription(String newDescription) {
        mDesc = newDescription;
    }

    private Chat(String creatorId, String title, String description, final Member creator) {
        mId = CHAT_PREFIX + creatorId;
        mTitle = title;
        mDesc = description;
        mMembers = new ArrayList<>();
        mMembers.add(0, creator);
    }

    private Chat(String chatId, String title, String description, List<Member> members) {
        mId = chatId;
        mTitle = title;
        mDesc = description;
        mMembers = members;
    }

}
