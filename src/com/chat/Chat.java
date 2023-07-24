package com.chat;

import java.util.ArrayList;
import java.util.List;

import com.Connection;

public class Chat {

    // static methods
    public static Chat createChat(String title, String description, Connection con) {
        final Member creator = new Member(con);
        return new Chat(description, title, description, creator);
    }

    public final static String CHAT_PREFIX = "3120";

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

}
