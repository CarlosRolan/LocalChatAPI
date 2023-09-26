package com.chat;

import java.util.ArrayList;
import java.util.List;

import com.chat.Member.Permission;
import com.controller.Connection;
import com.data.MSG;

public class ChatBuilder {

    /**
     * 
     * @param chatMSGInfo
     * @return
     */
    public static String newChatReference(MSG chatMSGInfo) {

        String chatId = generateChatCode(chatMSGInfo.getEmisor());
        String chatTitle = chatMSGInfo.getReceptor();
        String chatDesc = chatMSGInfo.getBody();
        String[] membersRefList = chatMSGInfo.getParameters();

        String membersAsString = "";

        for (int i = 0; i < membersRefList.length; i++) {
            String memberRef = membersRefList[i] + Member.SEPARATOR;
            membersAsString += memberRef;
        }

        String toret = chatId + Chat.SEPARATOR + chatTitle + Chat.SEPARATOR + chatDesc + Chat.SEPARATOR
                + membersAsString;

        return toret;
    }

    /**
     * 
     * @param creatorId         creatorID
     * @param numChatsOfCreator numberOfChatsOfCreator
     * @return CHAT_PREFIX + creatorID + numberOfChatsOfCreator
     */
    private static String generateChatCode(String creatorId, String numChatsOfCreator) {
        return Chat.CHAT_PREFIX + creatorId + numChatsOfCreator;
    }

    /**
     * 
     * @param chatId creatorID + numberOfChatsOfCreator
     * @return CHAT_PREFIX + creatorID + numberOfChatsOfCreator
     */
    private static String generateChatCode(String chatId) {
        return Chat.CHAT_PREFIX + chatId;
    }

    /**
     * 
     * @param chatRef
     * @return
     */
    public static Chat instanceChat(String chatRef) {
        String[] data = chatRef.split("_");

        String chatId = data[0];
        String chatTitle = data[1];
        String chatDesc = data[2];

        String membersRaw = data[3];
        System.out.println("DATA[3]:" + data[3]);

        String[] memberRefList = membersRaw.split(Member.SEPARATOR);

        List<Member> membersList = new ArrayList<>();

        for (int i = 0; i < memberRefList.length; i++) {
            System.out.println(memberRefList[i]);
            Member fromRef = initMemberFromRef(memberRefList[i]);
            membersList.add(fromRef);
        }
        return new Chat(chatId, chatTitle, chatDesc, membersList);
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
        String creatorId = chatInfo.getEmisor();
        String chatNum = chatInfo.getReceptor();
        String chatId = generateChatCode(creatorId,chatNum);
        String[] chatData = chatInfo.getBody().split(Chat.SEPARATOR);
        String chatTitle = chatData[0];
        String chatDesc = chatData[1];
        String creatorNick = chatData[2];

        List<Member> members = new ArrayList<>();

        for (int i = 0; i < chatInfo.getParameters().length; i++) {
            String memberRef = chatInfo.getParameter(i);
            if (memberRef == null) {
                Member creator = newCreator(creatorId, creatorNick);
                members.clear();
                members.add(0, creator);
                break;
            }
        }
        return new Chat(chatId, chatTitle, chatDesc, members);
    }

    /* MEMBER METHODS */
    /**
     * 
     * @param memberRef comes with the follow strucute
     *                  ClientID_ClientNick_ClientPermission
     */
    public static Member initMemberFromRef(String memberRef) {
        String memberData[] = memberRef.split(Member.SEPARATOR);

        String memberId = memberData[0];
        String memberNick = memberData[1];
        Member.Permission rights = Member.Permission.valueOf(memberData[2]);

        return new Member(memberId, memberNick, rights);
    }

    /**
     * 
     * @param conId
     * @param name
     * @return
     */
    public static Member newCreator(String conId, String name) {
        return new Member(conId, name, Permission.ADMIN);
    }

    /**
     * 
     * @param conId
     * @param name
     * @return
     */
    public static Member newRegular(String conId, String name) {
        return new Member(conId, name, Permission.REGULAR);
    }

    /**
     * 
     * @param con
     * @return
     */
    public static Member newRegular(Connection con) {
        return new Member(con.getConId(), con.getNick(), Permission.REGULAR);
    }

    /**
     * 
     * @param con
     * @return
     */
    public static Member newAdmin(Connection con) {
        return new Member(con.getConId(), con.getNick(), Permission.ADMIN);
    }

    /**
     * 
     * @param con
     * @return
     */
    public static Member newMember(Connection con, String rights) {
        return new Member(con.getConId(), con.getNick(), Permission.assing(rights));
    }

}
