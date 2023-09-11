package com.chat;

import com.controller.Connection;

/**
 * Description:
 * Program Name: LocalChatApi
 * 
 * @author Carlos Rolán Díaz
 * @version beta
 */
public class Member {

    protected static enum Permission {
        ADMIN("AD"),
        REGULAR("REG"),
        UNDEFINED("undefined");

        private String permCode;

        Permission(String permision) {
            permCode = permision;
        }

        public static Permission assing(String code) {
            switch (code) {
                case "AD":
                    return ADMIN;
                case "REG":
                    return REGULAR;
                default:
                    return UNDEFINED;
            }
        }

        @Override
        public String toString() {
            return permCode;
        }
    }

    /* STATIC */
    /**
     * 
     * @param memberRef comes with the follow strucute
     *                  ClientID_ClientNick_ClientPermission
     */
    public static Member makeFromRef(String memberRef) {
        String[] data = memberRef.split("-");
        return new Member(data[0], data[1], Permission.assing(data[2]));
    }

    public static Member initCreator(String conId, String name) {
        return new Member(conId, name, Permission.ADMIN);
    }

    public static Member regular(String conId, String name) {
        return new Member(conId, name, Permission.REGULAR);
    }

    public static Member regular(Connection con) {
        return new Member(con.getConId(), con.getNick(), Permission.REGULAR);
    }

    public static Member admin(Connection con) {
        return new Member(con.getConId(), con.getNick(), Permission.ADMIN);
    }

    public static Member newMember(Connection con, String permissions) {
        return new Member(con.getConId(), con.getNick(), Permission.assing(permissions));
    }

    /* PROPs */
    private final int connectionRef;
    private final Permission mRights;
    private String mName;

    /* GETTERS */
    public boolean isAdmin() {
        if (mRights == Permission.ADMIN) {
            return true;
        } else {
            return false;
        }
    }

    public String getConnectionId() {
        return String.valueOf(connectionRef);
    }

    public String getName() {
        return mName;
    }

    /* CONSTRUCTORs */
    Member(String conId, String name, final Permission permission) {
        connectionRef = Integer.parseInt(conId);
        mRights = permission;
        mName = name;
    }

    @Override
    public String toString() {
        return connectionRef + "-" + mName + "-" + mRights;
    }

    public String toXML() {
        return "<member connectionRef=" + connectionRef + " name='" + mName + "'' rights='" + mRights.name() + "'/>";
    }

}
