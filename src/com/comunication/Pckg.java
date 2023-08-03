package com.comunication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pckg implements Serializable {

    private static final long serialVersionUID = 123456799L;

    public static final int AS_ARRAY = 0;
    public static final int AS_LIST = 1;

    /* TYPES */
    public enum PckgType {
        MIXED,
        COLLECTION;
    }

    /* PROPERTIES */
    public final PckgType PACKAGE_TYPE;
    public String pckgName;
    private Msg[] pMsgs;

    /* GETTERS */
    public Msg getMsgAt(int position) {
        return pMsgs[position];
    }

    public List<Msg> getMessagesList() {
        return msgToList(pMsgs);
    }

    public Msg[] getMessagesArray() {
        return pMsgs;
    }

    /* SETTERS */
    public void setMessages(Msg[] messages) {
        pMsgs = messages;
    }

    public void setMsgAt(int position, Msg msg) {
        pMsgs[position] = msg;
    }

    public void addMsg(Msg msg) {
        List<Msg> tempList = msgToList(pMsgs);
        tempList.add(msg);
        Msg[] tempArray = msgToArray(tempList);
        setMessages(tempArray);
    }

    /* CONSTRUCTORS */
    public Pckg(final PckgType type) {
        PACKAGE_TYPE = type;
    }

    /* PARSERS */
    private Msg[] msgToArray(List<Msg> msgList) {
        Msg[] messages = new Msg[msgList.size()];

        for (int i = 0; i < messages.length; i++) {
            messages[i] = msgList.get(i);
        }

        return messages;
    }

    private List<Msg> msgToList(Msg[] messages) {
        List<Msg> msgList = new ArrayList<Msg>();

        for (int i = 0; i < messages.length; i++) {
            msgList.add(messages[i]);
        }

        return msgList;
    }

    private String messagesToString() {
        String toret = "";

        for (int i = 0; i < pMsgs.length; i++) {
            toret = toret + pMsgs[i].toString();
        }

        return "\n" + toret;
    }

    @Override
    public String toString() {
        return "[" + PACKAGE_TYPE + "]" + pckgName + messagesToString();
    }

}
