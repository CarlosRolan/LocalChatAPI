package com.chat;

import java.util.List;

/**
 * Description:
 * Program Name: LocalChatApi
 * 
 * @author Carlos Rolán Díaz
 * @version beta
 */
public class Chat {

    final String REF_SEPARATOR = "_";

    final static String CHAT_PREFIX = "3120";

    /* PROPs */
    private final List<Member> pMembers;
    private String pId;
    private String pTitle;
    private String pDesc;

    /* GETTERS */

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

    public String getDescription() {
        return pDesc;
    }

    public List<Member> getMembers() {
        return pMembers;
    }

    public String[] getMembersRef() {

        String[] membersRef;

        try {
            membersRef = new String[pMembers.size()];
            for (int i = 0; i < membersRef.length; i++) {
                membersRef[i] = pMembers.get(i).toString();
            }
        } catch (NullPointerException e) {
            return null;
        }

        return membersRef;
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
    Chat(String chatId, String title, String description, List<Member> members) {
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

    public String getReference() {
        String reference = null;

        try {
            reference = getChatId() + REF_SEPARATOR + getTitle() + REF_SEPARATOR + getChatId() + REF_SEPARATOR
                    + getMembersRef();
        } catch (NullPointerException e) {
            System.err.println("Cannot get reference from a null chat");
        }

        return reference;
    }
}