package com.chat;

import java.util.ArrayList;
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
    private final String pId;
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

        String[] toret;

        try {
            toret = new String[pMembers.size()];
            for (int i = 0; i < toret.length; i++) {
                System.out.println("IN_CHAT_CLASS:" + pMembers.get(i).getReference());
                toret[i] = pMembers.get(i).getReference();
            }
        } catch (NullPointerException e) {
            return null;
        }
        System.out.println("TORET:" + toret);

        return toret;
    }

    public String getMemberRefAsString() {
          String toret = "";

        try {
            for (int i = 0; i < pMembers.size(); i++) {
                System.out.println("IN_CHAT_CLASS:" + pMembers.get(i).getReference());
                toret += pMembers.get(i).getReference() + Member.SEPARATOR;
            }
        } catch (NullPointerException e) {
            return null;
        }
        System.out.println("TORET:" + toret);

        return toret;
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

    Chat(Member creator, String chatId, String chatTitle, String chatDesc) {
        pMembers = new ArrayList<>();
        pMembers.add(creator);
        pId = chatId;
        pTitle = chatTitle;
        pDesc = chatDesc;
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
            reference = getChatId() + REF_SEPARATOR + getTitle() + REF_SEPARATOR + getDescription() + REF_SEPARATOR
                    + getMemberRefAsString();
        } catch (NullPointerException e) {
            System.err.println("Cannot get reference from a null chat");
        }

        return reference;
    }
}