package com.comunication;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Pckg implements Serializable {

    private static final long serialVersionUID = 123456789L;

    public static final int AS_ARRAY = 0;
    public static final int AS_LIST = 1;

    /* TYPES */
    public enum PckgType {
        MIXED,
        COLLECTION;
    }

    /* PROPERTIES */
    public final PckgType PACKAGE_TYPE;
    public String pckgName = "PKG_NAME";
    private List<Msg> pMsgs = new ArrayList<>();

    /* GETTERS */
    public String getPckgName() {
        return pckgName;
    }

    public Msg getMsgAt(int position) {
        return pMsgs.get(position);
    }

    public List<Msg> getMessagesList() {
        return pMsgs;
    }

    /* SETTERS */
    public void setMessages(List<Msg> messages) {
        pMsgs = messages;
    }

    public void setMsgAt(int position, Msg msg) {
        pMsgs.add(position, msg);
    }

    public void addMsg(Msg msg) {
        pMsgs.add(msg);
    }

    public void setName(String name) {
        pckgName = name;
    }

    /* CONSTRUCTORS */
    public Pckg(final PckgType type) {
        PACKAGE_TYPE = type;
    }

    /* PARSERS */

    private String messagesToString() {
        String toret = "";
        
        for (Msg msg : pMsgs) {
            toret += msg.toString();
        }

        return "\n" + toret;
    }

    @Override
    public String toString() {
        return "[" + PACKAGE_TYPE + "]" + pckgName + messagesToString();
    }

}
