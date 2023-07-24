package com.chat;

import com.Connection;

public class Member {

    enum Permission {
        ADMIN,
        REGULAR;
    }

    public static Member creator(Connection c) {
        return new Member(c);

    }

    private final int connectionRef;
    private final Permission mRights;

    private String mName;

    public Member(Connection connection) {
        connectionRef = Integer.parseInt(connection.getConId());
        mRights = Permission.ADMIN;
        mName = connection.getNick();
    }

}
