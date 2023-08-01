package com.chat;

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
        REGULAR("REG");

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
                    return ADMIN;
            }
        }

        @Override
        public String toString() {
            return permCode;
        }
    }

    public static Member initCreator(String conId, String name) {
        return new Member(conId, name, Permission.ADMIN);
    }

    public static Member regular(String conId, String name) {
        return new Member(conId, name, Permission.REGULAR);
    }

    private final int connectionRef;
    private final Permission mRights;
    private String mName;

    public String getConnectionId() {
        return String.valueOf(connectionRef);
    }

    public String getName() {
        return mName;
    }

    private Member(String conId, String name, final Permission permission) {
        connectionRef = Integer.parseInt(conId);
        mRights = permission;
        mName = name;
    }

    protected Member(String memberInfo) {
        String[] params = memberInfo.split("_");

        connectionRef = Integer.parseInt(params[0]);
        mRights = Permission.assing(params[1]);
        mName = params[2];
    }

    @Override
    public String toString() {
        return connectionRef + "_" + mRights + "_" + mName;
    }

}
