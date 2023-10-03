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

    /* ENUM */
    static enum Permission {
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
    public final static String SEPARATOR = "-";

    public static Member initMember(String memberRef) {
     String memberData[] = memberRef.split(Member.SEPARATOR);

        String memberId = memberData[0];
        String memberNick = memberData[1];
        Permission rights = Permission.assing(memberData[2]);

        return new Member(memberId, memberNick, rights);
    }

    public static Member initMember(String memberRef, boolean isAdmin) {
        if (isAdmin) {
            return initAdmin(memberRef);
        } 

        return initRegular(memberRef);
    }

    /**
     * 
     * @param memberRef comes with the follow strucute
     *                  ClientID_ClientNick
     */
    private static Member initRegular(String memberRef) {
        String memberData[] = memberRef.split(Member.SEPARATOR);

        String memberId = memberData[0];
        String memberNick = memberData[1];

        return new Member(memberId, memberNick, Permission.REGULAR);
    }

        /**
     * 
     * @param memberRef comes with the follow strucute
     *                  ClientID_ClientNick
     */
    private static Member initAdmin(String memberRef) {
        String memberData[] = memberRef.split(Member.SEPARATOR);

        String memberId = memberData[0];
        String memberNick = memberData[1];

        return new Member(memberId, memberNick, Permission.ADMIN);
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

    public static Member newMember(Connection con, String rights) {
        Member.Permission permissions = Permission.assing(rights);
        return new Member(con.getConId(), con.getNick(), permissions);
    }

    /**
     * 
     * @param connection
     * @return
     */
    public static Member newRegular(Connection connection) {
        return new Member(connection.getConId(), connection.getNick(), Permission.REGULAR);
    }

    /* PROPs */
    private final int mConnectionRef;
    private final Permission mRights;
    private String mNick;

    /* GETTERS */
    public boolean isAdmin() {
        if (mRights == Permission.ADMIN) {
            return true;
        } else {
            return false;
        }
    }

    public String getConnectionId() {
        return String.valueOf(mConnectionRef);
    }

    public String getNick() {
        return mNick;
    }

    public String getReference() {
        return mConnectionRef + SEPARATOR + mNick + SEPARATOR + mRights;
    }

    /* CONSTRUCTORs */
    Member(String conId, String name, final Permission permission) {
        mConnectionRef = Integer.parseInt(conId);
        mRights = permission;
        mNick = name;
    }

    public String toXML() {
        return "<member connectionRef=" + mConnectionRef + " name='" + mNick + "'' rights='" + mRights.name() + "'/>";
    }

}
