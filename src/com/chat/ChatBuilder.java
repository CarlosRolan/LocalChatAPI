package com.chat;

import java.util.ArrayList;
import java.util.List;

import com.chat.Member.Permission;
import com.controller.Connection;
import com.data.MSG;

public class ChatBuilder {

    public static Chat initChat(String chatRef) {
        String[] data = chatRef.split("_");
        String[] membersData = data[3].split("-");
        List<Member> membersList = new ArrayList<>();

        for (int i = 0; i < membersData.length; i++) {
            Member fromRef = initMember(membersData[i]);
            membersList.add(fromRef);
        }
        return new Chat(data[0], data[1], data[2], membersList);
    }

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
        members.add(initCreator(chatInfo.getEmisor(), chatInfo.getBody()));

        return new Chat(Chat.CHAT_PREFIX + chatInfo.getEmisor() + chatInfo.getReceptor(),
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
    public static Chat newChat(MSG chatInfo) {
        String[] membersRaw = chatInfo.getParameters();
        List<Member> members = new ArrayList<Member>();

        for (int i = 0; i < membersRaw.length; i++) {
            members.add(initMember(membersRaw[i]));
        }

        return new Chat(chatInfo.getEmisor(), chatInfo.getReceptor(), chatInfo.getBody(), members);
    }

    /* MEMBER METHODS */
    /**
     * 
     * @param memberRef comes with the follow strucute
     *                  ClientID_ClientNick_ClientPermission
     */
    public static Member initMember(String memberRef) {
        String[] data = memberRef.split("-");
        return new Member(data[0], data[1], Permission.assing(data[2]));
    }
    
    public static Member initCreator(String conId, String name) {
        return new Member(conId, name, Permission.ADMIN);
    }

    public static Member newRegular(String conId, String name) {
        return new Member(conId, name, Permission.REGULAR);
    }

    public static Member newRegular(Connection con) {
        return new Member(con.getConId(), con.getNick(), Permission.REGULAR);
    }

    public static Member admin(Connection con) {
        return new Member(con.getConId(), con.getNick(), Permission.ADMIN);
    }

    public static Member newMember(Connection con, String permissions) {
        return new Member(con.getConId(), con.getNick(), Permission.assing(permissions));
    }

}
