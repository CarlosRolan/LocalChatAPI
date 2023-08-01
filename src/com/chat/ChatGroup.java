package com.chat;

import java.util.ArrayList;
import java.util.List;

import com.comunication.Msg;

/**
 * Description:
 * Program Name: LocalChatApi
 * 
 * @author Carlos Rolán Díaz
 * @version beta
 */
public class ChatGroup {

    public final static String CHAT_PREFIX = "3120";

    /**
     * The data in the Msg is store like:
     * Emisor: creator ID.
     * Parameters: { TITLE, DESCRIPTION } (of the chat)
     * Body: creator nick name
     * 
     * The chat ID is constructed as CHAT_PREFIX + creator ID
     * 
     * @param chatInfo Msg object that contain all info to construct and register a
     *                 chat in the SERVER
     * 
     * @return new instance of Chat object
     */
    public static ChatGroup createChatGroupAsAdmin(Msg chatInfo) {
        List<Member> members = new ArrayList<Member>();
        members.add(Member.initCreator(chatInfo.getEmisor(), chatInfo.getBody()));

        return new ChatGroup(CHAT_PREFIX + chatInfo.getEmisor(), chatInfo.getParameter(0), chatInfo.getParameter(1),
                members);
    }

    /**
     * The data in the Msg is store like:
     * Emisor: Chat ID.
     * Receptor: Chat Title
     * Parameters: { members as a string }
     * Body: Chat Description
     * 
     * @param chatInfo Msg object that contain all info of a Chat
     * 
     * @return new instance of Chat previously constructed in Server and sended as a
     *         Msg to the cleint
     */
    public static ChatGroup instanceChatGroup(Msg chatInfo) {
        String[] membersRaw = chatInfo.getParameters();

        List<Member> members = new ArrayList<Member>();

        for (int i = 0; i < membersRaw.length; i++) {
            members.add(new Member(membersRaw[i]));
        }

        return new ChatGroup(chatInfo.getEmisor(), chatInfo.getReceptor(), chatInfo.getBody(), members);

    }

    private final List<Member> mMembers;
    private String mId;
    private String mTitle;
    private String mDesc;

    /* GETTERS */
    public String getChatGroupId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDesc;
    }

    public List<Member> getMembers() {
        return mMembers;
    }

    public String[] getmembersToString() {

        String[] membersString;

        try {
            membersString = new String[mMembers.size()];
            for (int i = 0; i < membersString.length; i++) {
                membersString[i] = mMembers.get(i).toString();
            }
        } catch (NullPointerException e) {
            return null;
        }

        return membersString;
    }

    public void addMember(Member newMember) {
        if (mMembers != null) {
            mMembers.add(newMember);
        }
    }

    /* SETTERS */
    public void setTitle(String newTitle) {
        mTitle = newTitle;
    }

    public void setDescription(String newDescription) {
        mDesc = newDescription;
    }

    public boolean isMemberInChat(Member check) {
        for (Member member : mMembers) {
            if (member.getConnectionId().equals(check.getConnectionId())) {
                return true;
            }
        }

        return false;
    }

    public boolean isMemberInChat(String conId) {
        for (Member member : mMembers) {
            if (member.getConnectionId().equals(conId)) {
                return true;
            }
        }

        return false;
    }


    private ChatGroup(String chatId, String title, String description, List<Member> members) {
        mId = chatId;
        mTitle = title;
        mDesc = description;
        mMembers = members;
    }

}
