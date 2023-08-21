package com.chat;

import java.util.ArrayList;
import java.util.List;

import com.comunication.MSG;

/**
 * Description:
 * Program Name: LocalChatApi
 * 
 * @author Carlos Rolán Díaz
 * @version beta
 */
public class Chat {

    public final static String CHAT_PREFIX = "3120";

    /**
     * The data in the Msg is store like:
     * Emisor: creator ID.
     * Parameters: { TITLE, DESCRIPTION } (of the chat)
     * Body: creator nick name
     * 
     * The chat ID is constructed as CHAT_PREFIX + creator ID +
     * numberOfChatsOfCreator
     * 
     * @param chatInfo Msg object that contain all info to construct and register a
     *                 chat in the SERVER
     * 
     * @return new instance of Chat object
     */
    public static Chat createChatAsAdmin(MSG chatInfo) {
        List<Member> members = new ArrayList<Member>();
        members.add(Member.initCreator(chatInfo.getEmisor(), chatInfo.getBody()));

        return new Chat(CHAT_PREFIX + chatInfo.getEmisor() + chatInfo.getReceptor(),
                chatInfo.getParameter(0),
                chatInfo.getParameter(1),
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
    public static Chat instanceChat(MSG chatInfo) {
        String[] membersRaw = chatInfo.getParameters();

        List<Member> members = new ArrayList<Member>();

        for (int i = 0; i < membersRaw.length; i++) {
            members.add(new Member(membersRaw[i]));
        }

        return new Chat(chatInfo.getEmisor(), chatInfo.getReceptor(), chatInfo.getBody(), members);
    }

    private final List<Member> mMembers;
    private String mId;
    private String mTitle;
    private String mDesc;

    /* GETTERS */
    public String getChatId() {
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
        return mMembers.contains(check);
    }

    public boolean isMemberInChat(String conId) {
        for (Member member : mMembers) {
            if (member.getConnectionId().equals(conId)) {
                return true;
            }
        }

        return false;
    }

    private Chat(String chatId, String title, String description, List<Member> members) {
        mId = chatId;
        mTitle = title;
        mDesc = description;
        mMembers = members;
    }

    public String toXML() {
        String toret = "<chat id=" + mId + "title=" + mTitle + "desc=" + mDesc + ">";

        for (Member member : mMembers) {
            toret += "\n" + member.toString();
        }

        return toret;
    }

}
