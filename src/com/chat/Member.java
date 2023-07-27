package com.chat;

import com.comunication.Connection;

/**
 * Description: TODO"
 * Program Name: LocalChatApi
 * Date: 2020-12-16
 * @author Carlos Rolán Díaz
 * @version 1.0
 */
public class Member {

    enum Permission {
        ADMIN(),
        REGULAR();
    }

    public static Member creator(Connection c) {
        return new Member(c);
    }

    public static Member regular(String conId, String name) {
        return new Member(conId, name);
    }

    private final int connectionRef;
    private final Permission mRights;

    private String mName;

    private Member(Connection connection) {
        connectionRef = Integer.parseInt(connection.getConId());
        mRights = Permission.ADMIN;
        mName = connection.getNick();
    }

    private Member(String conId, String name) {
           connectionRef = Integer.parseInt(conId);
           mRights = Permission.REGULAR;
           mName = name;
    }

}
