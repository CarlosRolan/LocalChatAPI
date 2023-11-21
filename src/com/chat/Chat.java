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

    public final static String SEPARATOR = "_";
    public final static String CHAT_MEMBER_SEPARATOR = " ";

    final static String CHAT_PREFIX = "3120";

    public static List<Member> converToMemberList(String[] memberRefs) {
        List<Member> toret = new ArrayList<>();

        for (int i = 0; i < memberRefs.length; i++) {
            Member member = Member.init(memberRefs[i]);
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
     * @param chatRef
     * @return
     */
    public static Chat init(String chatRef) {
        String[] data = chatRef.split(Chat.SEPARATOR);

        String chatId = data[0];
        String chatTitle = data[1];
        String chatDesc = data[2];

        String membersRaw = data[3];

        String[] memberRefList = membersRaw.split(CHAT_MEMBER_SEPARATOR);

        List<Member> membersList = new ArrayList<>();

        for (int i = 0; i < memberRefList.length; i++) {
            Member fromRef = Member.init(memberRefList[i]);
            membersList.add(fromRef);
        }
        return new Chat(chatId, chatTitle, chatDesc, membersList);
    }

    /**
     * From CLIENT to SERVER
     * 
     * @param chatMsg Msg object that contain all info of a Chat
     * 
     * @return new instance of Chat previously constructed in Server and sended as a
     *         Msg to the cleint
     */
    public static Chat createChat(String chatTitle, String chatDesc, String[] membersRef, int numChats) {

        String numChatOfCreator = String.valueOf(numChats);

        List<Member> memberRefList = converToMemberList(membersRef);

        Member creator = memberRefList.get(0);

        String chatId = generateChatCode(creator.getConnectionId(), numChatOfCreator);

        return new Chat(chatId, chatTitle, chatDesc, memberRefList);
    }

    /**
     * From SERVER to CLIENT
     * 
     * @param chatMsg is
     * @return
     */
    public static Chat instanceChat(MSG chatMsg) {

        // MSG chatInfo = new MSG(MSG.Type.REQUEST);
        // chatInfo.setAction(ACTION);
        // chatInfo.setEmisor(chat.getChatId());
        // chatInfo.setReceptor(chat.getTitle());
        // chatInfo.setBody(chat.getDescription());
        // chatInfo.setParameters(chat.getMembersRef());

        String chatId = chatMsg.getEmisor();
        String chatTitle = chatMsg.getReceptor();
        String chatDesc = chatMsg.getBody();

        List<Member> memberRefList = converToMemberList(chatMsg.getParameters());

        return new Chat(chatId, chatTitle, chatDesc, memberRefList);
    }

    /* PROPs */
    private final List<Member> mMembers;
    private String mId;
    private String mTitle;
    private String mDesc;

    public boolean isCreator(Member memberToCheck) {
        String creatorId = getChatId().substring(3);
        return (creatorId.startsWith(memberToCheck.getConnectionId()));
    }

    /* GETTERS */

    public Member getMember(Member member) {
        if (mMembers.contains(member)) {
            return member;
        }
        return null;
    }

    public Member getMemberFromRef(String memberRef) {
        Member member = Member.init(memberRef);
        return getMember(member);
    }

    /**
     * 
     * @param conId
     * @return
     */
    public Member getMemberFromId(String conId) {
        for (Member member : mMembers) {
            if (conId.equals(member.getConnectionId()))
                return member;
        }

        System.err.println("There is no member with id = " + conId);

        return null;
    }

    /**
     * 
     * @param index
     * @return Member of the chat with the index given
     */
    public Member getMember(int index) {
        return mMembers.get(index);
    }

    public String getChatId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    /**
     * 
     * @return
     */
    public String getDescription() {
        return mDesc;
    }

    /**
     * 
     * @return
     */
    public List<Member> getMembers() {
        return mMembers;
    }

    /**
     * 
     * @return
     */
    public String[] getMembersRef() {

        String[] toret;

        try {
            toret = new String[mMembers.size()];
            for (int i = 0; i < toret.length; i++) {
                toret[i] = mMembers.get(i).getReference();
            }
        } catch (NullPointerException e) {
            return null;
        }

        return toret;
    }

    public List<String> getMembersRefList() {
        List<String> toret = new ArrayList<>();
        for (Member member : mMembers) {
            toret.add(member.getReference());
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
            for (int i = 0; i < mMembers.size(); i++) {
                if (i == mMembers.size() - 1) {
                    toret += mMembers.get(i).getReference();
                } else {
                    toret += mMembers.get(i).getReference() + CHAT_MEMBER_SEPARATOR;
                }

            }
        } catch (NullPointerException e) {
            return null;
        }

        return toret;
    }

    /**
     * 
     * @param memberToCheck
     * @return
     */
    public boolean isMemberInChat(Member memberToCheck) {
        return mMembers.contains(memberToCheck);
    }

    /**
     * 
     * @param memberId
     * @return
     */
    public boolean isMemberInChat(String memberId) {
        for (Member iMember : mMembers) {
            System.out.println("ID" + memberId + " =? " + "REF" + iMember.getReference());
            if (iMember.getConnectionId().equals(memberId)) {
                return true;
            }
        }

        return false;
    }

    /**
     * @param memberId
     * @return true if member is Admin
     */

    public boolean isMemberAdmin(String memberId) {

        Member memberToCheck = getMemberFromId(memberId);

        return memberToCheck.isAdmin();
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
        mTitle = newTitle;
    }

    /**
     * 
     * @param newDescription
     */
    public void setDescription(String newDescription) {
        mDesc = newDescription;
    }

    /* UTILs */
    /**
     * 
     * @param newMember
     */
    public void addMember(Member newMember) {
        if (mMembers != null) {
            mMembers.add(newMember);
        }
    }

    /**
     * 
     * @param member
     */
    public void deleteMember(Member member) {
        mMembers.remove(member);
    }

    /**
     * 
     * @param memberId
     */
    public void deleteMember(String memberId) {
        Member toDelete = getMemberFromId(memberId);
        deleteMember(toDelete);
    }

    public void updateMember(Member updated) {
        String id = updated.getConnectionId();
        Member old = getMemberFromId(id);
        int index = getMembers().indexOf(old);
        getMembers().set(index, updated);
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
        mId = chatId;
        mTitle = title;
        mDesc = description;
        mMembers = members;
    }

    /**
     * Generetas a chat with only the owner as a member
     * 
     * @param ownerId
     * @param ownerNick
     * @param chatsNum
     * @param title
     * @param description
     */
    public Chat(String ownerId, String ownerNick, String chatsNum, String title, String description) {
        mTitle = title;
        mDesc = description;
        Member owner = Member.newCreator(ownerId, ownerNick);
        mMembers = new ArrayList<>();
        mMembers.add(owner);
    }

    /**
     * 
     * @return
     */
    public String toXML() {
        String toret = "<chat id=" + mId + " title='" + mTitle + "'' desc='" + mDesc + "''>";

        for (Member member : mMembers) {
            toret += "\n\t" + member.toXML();
        }

        toret += "\n</chat>";

        return toret;
    }

}