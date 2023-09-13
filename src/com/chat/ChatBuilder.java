package com.chat;

import java.util.ArrayList;
import java.util.List;

import com.chat.Member.Permission;
import com.controller.Connection;
import com.data.MSG;

public class ChatBuilder {

    /*CHAT_PREFIX + creator ID +
     * numberOfChatsOfCreator */
    private static String generateChatCode(String creatorId, String numChatsOfCreator) {
        return Chat.CHAT_PREFIX + creatorId + numChatsOfCreator;
    }

    /*
     * Creates chat From chatRef
     */
    public static Chat initChat(String chatRef) {
        String[] data = chatRef.split("_");

        String chatId = data[0];
        String chatTitle = data[1];
        String chatDesc = data[2];

        String membersRaw = data[3];
        System.out.println("DATA[3]:" + data[3]);

        String[] memberRefList = membersRaw.split("-");

        List<Member> membersList = new ArrayList<>();

        for (int i = 0; i < memberRefList.length; i++) {
            System.out.println(memberRefList[i]);
            Member fromRef = initMember(memberRefList[i]);
            membersList.add(fromRef);
        }
        return new Chat(chatId, chatTitle, chatDesc, membersList);
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
        String creatorId = chatInfo.getEmisor();
        String creatorNIck = chatInfo.getBody();
        String chatTitle = chatInfo.getParameter(0);
        String chatDesc = chatInfo.getParameter(1);
        String numChats = chatInfo.getReceptor();

        String chatId = generateChatCode(creatorId, numChats);

        Member creator = initCreator(creatorId, creatorNIck);

        return new Chat(creator, chatId, chatTitle, chatDesc);
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

        String chatId = chatInfo.getEmisor();
        String chatTitle = chatInfo.getReceptor();
        String chatDesc = chatInfo.getBody();
        String[] membersRaw = chatInfo.getParameters();
        List<Member> members = new ArrayList<Member>();

        for (int i = 0; i < membersRaw.length; i++) {
            members.add(initMember(membersRaw[i]));
        }

        return new Chat(chatId, chatTitle, chatDesc, members);
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
