package com.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PKG implements Serializable {

    private static final long serialVersionUID = 123456789L;

    public static final int AS_ARRAY = 0;
    public static final int AS_LIST = 1;

    /* TYPES */
    public enum Type {
        MIXED,
        COLLECTION;
    }

    /* PROPERTIES */
    public final Type PACKAGE_TYPE;
    private String pName = "PKG_NAME";
    private List<MSG> pMsgs = new ArrayList<>();

    /* GETTERS */
    public String getPKGName() {
        return pName;
    }

    public MSG getMsgAt(int position) {
        return pMsgs.get(position);
    }

    public List<MSG> getMessagesList() {
        return pMsgs;
    }

    /* SETTERS */
    public void setMessages(List<MSG> messages) {
        pMsgs = messages;
    }

    public void setMsgAt(int position, MSG msg) {
        pMsgs.add(position, msg);
    }

    public void addMsg(MSG msg) {
        pMsgs.add(msg);
    }

    public void setName(String name) {
        pName = name;
    }

    /* CONSTRUCTORS */
    public PKG(final Type type) {
        PACKAGE_TYPE = type;
    }

    /* PARSERS */
    private String messagesToString() {
        String toret = "";
        
        for (MSG msg : pMsgs) {
            toret += msg.toString();
        }

        return "\n" + toret;
    }

    @Override
    public String toString() {
        return "[" + PACKAGE_TYPE + "]" + pName + messagesToString();
    }

}
