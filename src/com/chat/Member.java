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

    /**
     * 
     * @param memberRefcomes with the follow strucute
     *                       ClientID_ClientNick_PERMISSION
     * @return
     */
    public static Member init(String memberRef) {
        final String memberData[] = memberRef.split(Member.SEPARATOR);

        final String memberId = memberData[0];
        final String memberNick = memberData[1];
        final Permission rights = Permission.assing(memberData[2]);

        return new Member(memberId, memberNick, rights);
    }

    /**
     * 
     * @param memberRef
     * @param isAdmin
     * @return
     */
    public static Member init(String memberRef, boolean isAdmin) {
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
        final String memberData[] = memberRef.split(Member.SEPARATOR);

        final String memberId = memberData[0];
        final String memberNick = memberData[1];

        return new Member(memberId, memberNick, Permission.REGULAR);
    }

    /**
     * 
     * @param memberRef comes with the follow strucute
     *                  ClientID_ClientNick
     */
    private static Member initAdmin(String memberRef) {
        final String memberData[] = memberRef.split(Member.SEPARATOR);

        final String memberId = memberData[0];
        final String memberNick = memberData[1];

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
        final Member.Permission permissions = Permission.assing(rights);
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
    private Permission mRights;
    private String mNick;

    /* GETTERS */
    /**
     * 
     * @return
     */
    public boolean isAdmin() {
        if (mRights == Permission.ADMIN) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 
     * @return
     */
    public String getConnectionId() {
        return String.valueOf(mConnectionRef);
    }

    /**
     * 
     * @return
     */
    public String getNick() {
        return mNick;
    }

    /**
     * 
     * @return
     */
    public String getReference() {
        return mConnectionRef + SEPARATOR + mNick + SEPARATOR + mRights;
    }

    /* SETTERs */
    public void makeAdmin() {
        mRights = Permission.ADMIN;
    }

    public void makeRegular() {
        mRights = Permission.REGULAR;
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
