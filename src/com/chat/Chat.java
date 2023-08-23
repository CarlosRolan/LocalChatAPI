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
            members.add(Member.makeFromRef(membersRaw[i]));
        }

        return new Chat(chatInfo.getEmisor(), chatInfo.getReceptor(), chatInfo.getBody(), members);
    }

    /* PROPs */
    private final List<Member> pMembers;
    private String pId;
    private String pTitle;
    private String pDesc;

    /* GETTERS */
    public String getChatId() {
        return pId;
    }

    public String getTitle() {
        return pTitle;
    }

    public String getDescription() {
        return pDesc;
    }

    public List<Member> getMembers() {
        return pMembers;
    }

    public String[] getmembersToString() {

        String[] membersString;

        try {
            membersString = new String[pMembers.size()];
            for (int i = 0; i < membersString.length; i++) {
                membersString[i] = pMembers.get(i).toString();
            }
        } catch (NullPointerException e) {
            return null;
        }

        return membersString;
    }

    public boolean isMemberInChat(Member check) {
        return pMembers.contains(check);
    }

    public boolean isMemberInChat(String conId) {
        for (Member member : pMembers) {
            if (member.getConnectionId().equals(conId)) {
                return true;
            }
        }

        return false;
    }

    /* SETTERS */
    public void setTitle(String newTitle) {
        pTitle = newTitle;
    }

    /* UTILs */
    public void addMember(Member newMember) {
        if (pMembers != null) {
            pMembers.add(newMember);
        }
    }

    public void setDescription(String newDescription) {
        pDesc = newDescription;
    }

    /* CONSTRUCTOR */
    private Chat(String chatId, String title, String description, List<Member> members) {
        pId = chatId;
        pTitle = title;
        pDesc = description;
        pMembers = members;
    }

    public String toXML() {
        String toret = "<chat id=" + pId + " title='" + pTitle + "'' desc='" + pDesc + "''>";

        for (Member member : pMembers) {
            toret += "\n\t" + member.toXML();
        }

        toret += "\n</chat>";

        return toret;
    }

    @Override
    public String toString() {
        return pId + "_" + pTitle + "_" + pDesc + "_" + getmembersToString();
    }
}
