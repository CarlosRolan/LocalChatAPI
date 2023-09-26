package com.chat;

import java.util.ArrayList;
import java.util.List;

import com.data.MSG;

/**
 * Description:
 * Program Name: LocalChatApi
 * 
 * @author Carlos Rolán Díaz
 * @version beta
 */
public class Chat {

    /* STATIC */

    final static String SEPARATOR = "_";

    final static String CHAT_PREFIX = "3120";

    public static List<Member> getMembersList(String[] memberRefs) {
        List<Member> toret = new ArrayList<>();

        for (int i = 0; i < memberRefs.length; i++) {
            Member member = Member.initMember(memberRefs[i]);
            toret.add(member);
        }

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
    public static Chat initChat(String chatRef) {
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
            Member fromRef = Member.initMember(memberRefList[i]);
            membersList.add(fromRef);
        }
        return new Chat(chatId, chatTitle, chatDesc, membersList);
    }

    /**
     * From CLIENT to SERVER
     * The data in the Msg is store like:
     * Emisor: Chat ID.
     * Receptor: Chat Title
     * Parameters: { members as a string }
     * Body: Chat Description
     * 
     * @param chatMsg Msg object that contain all info of a Chat
     * 
     * @return new instance of Chat previously constructed in Server and sended as a
     *         Msg to the cleint
     */
    public static Chat createChat(MSG chatMsg) {

        String chatTitle = chatMsg.getEmisor();
        String chatDesc = chatMsg.getReceptor();
        String numChats = chatMsg.getBody();

        List<Member> memberRefList = getMembersList(chatMsg.getParameters());

        Member creator = memberRefList.get(0);

        String chatId = generateChatCode(creator.getConnectionId(), numChats);

        return new Chat(chatId, chatTitle, chatDesc, memberRefList);
    }

    /**
     * From SERVER to CLIENT
     * @param chatMsg is
     * @return
     */
    public static Chat instanceChat(MSG chatMsg) {
        String chatId = chatMsg.getEmisor();
        String chatTitle = chatMsg.getReceptor();
        String chatDesc = chatMsg.getBody();

        List<Member> memberRefList = getMembersList(chatMsg.getParameters());

        return new Chat(chatId, chatTitle, chatDesc, memberRefList);
    }

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

    /* PROPs */
    private final List<Member> pMembers;
    private final String pId;
    private String pTitle;
    private String pDesc;

    /* GETTERS */
    /**
     * 
     * @param conId
     * @return
     */
    public Member getMember(String conId) {
        for (Member member : pMembers) {
            if (conId.equals(member.getConnectionId()))
                return member;
        }

        return null;
    }

    public String getChatId() {
        return pId;
    }

    public String getTitle() {
        return pTitle;
    }

    /**
     * 
     * @return
     */
    public String getDescription() {
        return pDesc;
    }

    /**
     * 
     * @return
     */
    public List<Member> getMembers() {
        return pMembers;
    }

    /**
     * 
     * @return
     */
    public String[] getMembersRef() {

        String[] toret;

        try {
            toret = new String[pMembers.size()];
            for (int i = 0; i < toret.length; i++) {
                toret[i] = pMembers.get(i).getReference();
            }
        } catch (NullPointerException e) {
            return null;
        }

        return toret;
    }

    /**
     * 
     * @return
     */
    public String getMemberRefAsString() {
        String toret = "";

        try {
            for (int i = 0; i < pMembers.size(); i++) {
                if (i == pMembers.size()) {
                    toret += pMembers.get(i).getReference();
                } else {
                    toret += pMembers.get(i).getReference() + Member.SEPARATOR;
                }

            }
        } catch (NullPointerException e) {
            return null;
        }

        return toret;
    }

    /**
     * 
     * @param check
     * @return
     */
    public boolean isMemberInChat(Member check) {
        return pMembers.contains(check);
    }

    /**
     * 
     * @param conId
     * @return
     */
    public boolean isMemberInChat(String conId) {
        for (Member member : pMembers) {
            if (member.getConnectionId().equals(conId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 
     * @return
     */
    public String getReference() {
        String reference = null;

        try {
            reference = getChatId() + SEPARATOR + getTitle() + SEPARATOR + getDescription() + SEPARATOR
                    + getMemberRefAsString();
        } catch (NullPointerException e) {
            System.err.println("Cannot get reference from a null chat");
        }

        return reference;
    }

    /* SETTERS */
    /**
     * 
     * @param newTitle
     */
    public void setTitle(String newTitle) {
        pTitle = newTitle;
    }

    /* UTILs */
    /**
     * 
     * @param newMember
     */
    public void addMember(Member newMember) {
        if (pMembers != null) {
            pMembers.add(newMember);
        }
    }

    /**
     * 
     * @param newDescription
     */
    public void setDescription(String newDescription) {
        pDesc = newDescription;
    }

    /* CONSTRUCTOR */
    /**
     * 
     * @param chatId
     * @param title
     * @param description
     * @param members
     */
    Chat(String chatId, String title, String description, List<Member> members) {
        pId = chatId;
        pTitle = title;
        pDesc = description;
        pMembers = members;
    }

    /**
     * 
     * @return
     */
    public String toXML() {
        String toret = "<chat id=" + pId + " title='" + pTitle + "'' desc='" + pDesc + "''>";

        for (Member member : pMembers) {
            toret += "\n\t" + member.toXML();
        }

        toret += "\n</chat>";

        return toret;
    }

}