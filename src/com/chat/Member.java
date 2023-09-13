package com.chat;

/**
 * Description:
 * Program Name: LocalChatApi
 * 
 * @author Carlos Rolán Díaz
 * @version beta
 */
public class Member {
    final static String SEPARATOR = "-";

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

    public String getReference() {
        return connectionRef + SEPARATOR + mName + SEPARATOR + mRights;
    }

    public String toXML() {
        return "<member connectionRef=" + connectionRef + " name='" + mName + "'' rights='" + mRights.name() + "'/>";
    }

}
