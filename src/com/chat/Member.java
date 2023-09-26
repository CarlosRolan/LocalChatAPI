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
    final static String SEPARATOR = "-";

    /**
     * 
     * @param memberRef comes with the follow strucute
     *                  ClientID_ClientNick_ClientPermission
     */
    public static Member initMember(String memberRef) {
        String memberData[] = memberRef.split(Member.SEPARATOR);

        String memberId = memberData[0];
        String memberNick = memberData[1];
        Member.Permission rights = Member.Permission.valueOf(memberData[2]);

        return new Member(memberId, memberNick, rights);
    }

    /**
     * 
     * @param conId
     * @param name
     * @return
     */
    public static Member newCreator(String conId, String name) {
        return new Member(conId, name, Permission.ADMIN);
    }

    /**
     * 
     * @param conId
     * @param name
     * @return
     */
    public static Member newRegular(String conId, String name) {
        return new Member(conId, name, Permission.REGULAR);
    }

    /**
     * 
     * @param connection
     * @return
     */
    public static Member newRegular(Connection connection) {
        return new Member(connection.getConId(), connection.getNick(), Permission.REGULAR);
    }

    /**
     * 
     * @param connection
     * @return
     */
    public static Member newAdmin(Connection connection) {
        return new Member(connection.getConId(), connection.getNick(), Permission.ADMIN);
    }

    /**
     * 
     * @param con
     * @param rights
     * @return
     */
    public static Member newMember(Connection con, String rights) {
        return new Member(con.getConId(), con.getNick(), Permission.assing(rights));
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

    public String getReference() {
        return connectionRef + SEPARATOR + mName + SEPARATOR + mRights;
    }

    /* CONSTRUCTORs */
    Member(String conId, String name, final Permission permission) {
        connectionRef = Integer.parseInt(conId);
        mRights = permission;
        mName = name;
    }

    public String toXML() {
        return "<member connectionRef=" + connectionRef + " name='" + mName + "'' rights='" + mRights.name() + "'/>";
    }

}
