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

    final static String SEPARATOR = "_";

    final static String CHAT_PREFIX = "3120";

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
}