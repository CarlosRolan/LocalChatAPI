package com.comunication;

import com.chat.Chat;
import com.chat.Member;

public class Reference {

    private static String SEPARATOR = "_";

    public static String toReference(Chat chat) {
        return chat.toString();
    }

    public static String toReference(Member member) {
        return member.toString();
    }

    public static String toReference(Connection con) {
        return con.toString();
    }

    public static String[] getRef(String reference) {
        return reference.split(SEPARATOR);
    }
}
