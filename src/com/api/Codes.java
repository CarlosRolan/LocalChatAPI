package com.api;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.chat.Chat;
import com.controller.Connection;
import com.data.MSG;

/**
 * Description:
 * Program Name: LocalChatApi
 * 
 * @author Carlos Rolán Díaz
 * @version beta
 */
public interface Codes {

    /* INFO-TYPE CODES */
    final String INFO_PRESENTATION_START = "PRESNTATION_START";
    final String INFO_NO_SERVER_RESPONSE = "NO_SERVER_RESPONSE";
    final String INFO_WAITING_RESPONSE = "WAITING_RESPONSE";
    final String INFO_CONECXION_REJECTED = "CONECXION_REJECTED";
    final String INFO_COMFIRMATION_SUCCESS = "OK";
    final String INFO_CONECXION_ACCEPTED = "CONECXION_ACCEPTED";
    final String INFO_CONNECTION_ESTABLISH = "CONNECTION_ESTABLISH";
    final String INFO_CONNECTION_CLOSED = "CONNECTION CLOSED";
    final String INFO_PRESENTATION_SUCCES = "PRESENTATION_SUCCES";

    final String COLLECTION_UPDATE = "UPDATE";

    /* REQ-TYPE CODES */
    final String REQ_PRESENT = "PRESENT";

    final String REQ_CON_INFO = "CON_INFO";

    final String REQ_SINGLE = "SINGLE_CHAT_REQUESTED";
    final String REQ_CHAT = "GET_CHAT";
    final String REQ_EXIT_CHAT = "EXIT_CHAT";
    final String REQ_DEL_CHAT = "DEL_CHAT";

    final String REQ_SHOW_ALL_CON = "SHOW_ALL_CON";
    final String REQ_SHOW_ALL_CHAT = "SHOW_ALL_CHAT";
    final String REQ_SHOW_ALL_MEMBERS_OF_CHAT = "SHOW_ALL_MEMBERS_OF_CHAT";

    final String REQ_ASKED_FOR_PERMISSION = "ASKED_FOR_PERMISSION";
    final String REQ_WAITING_FOR_PERMISSION = "WAITING_FOR_PERMISSION";

    final String REQ_ALLOW = "ALLOW";
    final String REQ_DENY = "DENY";

    final String REQ_START_SINGLE = "START_SINGLE";
    final String REQ_EXIT_SINGLE = "EXIT_SINGLE";

    final String REQ_CREATE_CHAT = "CREATE_CHAT";
    final String REQ_ADDED_TO_CHAT = "ADDED_TO_CHAT";
    final String REQ_INIT_CHAT = "INIT_CHAT";
    final String REQ_INIT_CON = "INIT_CON";
    final String REQ_ADD_MEMBER = "ADD_MEMBER";
    final String REQ_DELETE_MEMBER = "DELETE_MEMBER";
    final String REQ_CHANGE_MEMBER_RIGHTS = "CHANGE_MEMBER_RIGHTS";
    final String REQ_UPDATE_CHAT = "UPDATE_CHAT";

    final String REQ_SELECT_USER = "SELCT_USER";

    final String REQ_GET_CHATS_INFO = "GET_CHATS_INFO";
    final String REQ_GET_USERS_INFO = "GET_USERS_INFO";

    final String REQ_UPDATE_STATE = "UPDATE_STATE";

    /* ERROR-TYPE CODES */
    final String ERROR_PRESENTATION = "PRESENTATION";
    final String ERROR_SERVER_CONNECTION = "SERVER_CONNECTION";
    final String ERROR_CLIENT_NOT_FOUND = "CLIENT_NOT_FOUND";
    final String ERROR_SELF_REFERENCE = "SELF_REFERENCE";
    final String ERROR_CHAT_NOT_FOUND = "CHAT_NOT_FOUND";

    /* MSG-TYPE CODES */
    final String MSG_TO_SINGLE = "TO_SINGLE";
    final String MSG_TO_CHAT = "TO_CHAT";

    /* Firt check */
    final String MSG_FROM_SINGLE = "FROM_SINGLE";
    final String MSG_FROM_CHAT = "FROM_CHAT";

    /* Double check */
    final String MSG_RECIEVED_FROM_CHAT = "RECIEVED_FROM_CHAT";
    final String MSG_RECIEVED_FROM_SINGLE = "RECIEVED_FROM_SINGLE";

    /* WARNINGs */
    final String WARN_UNREGISTERED_MSG_MESSAGE_ACTION = "MSG[MESSAGE] UNHANDLED";
    final String WARN_UREGISTERED_MSG_ERROR_ACTION = "MSG[ERROR] UNHANDLED";
    final String WARN_UNREGISTERED_MSG_REQUEST_ACTION = "MSG[REQUEST] UNHANDLED";

    final String WARN_UNREGISTERED_PKG_COLLECTION_ACTION = "PKG[COLLECTION] UNHANDLED";
    final String WARN_UNREGISTERED_PKG_MIXED_ACTION = "PKG[MIXED] UNHANDLED";

    /**
     * 
     * @param con the connection to send the info
     * @return
     */
    public default MSG sendConInstance(Connection con, final String ACTION) {
        final MSG conInfo = new MSG(MSG.Type.REQUEST);
        conInfo.setAction(ACTION);
        conInfo.setEmisor(con.getConId());
        conInfo.setReceptor(con.getNick());
        conInfo.setParameter(0, con.getReference());
        // respond.setParameters(con.getChatsRef());
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        conInfo.setBody(dtf.format(now));

        return conInfo;
    }

    /**
     * 
     * @param chat
     * @param ACTION
     * @return
     */
    public default MSG sendChatInstance(Chat chat, final String ACTION) {
        final MSG chatInfo = new MSG(MSG.Type.REQUEST);
        chatInfo.setAction(ACTION);
        chatInfo.setEmisor(chat.getChatId());
        chatInfo.setReceptor(chat.getTitle());
        chatInfo.setBody(chat.getDescription());
        chatInfo.setParameters(chat.getMembersRef());

        return chatInfo;
    }

    /* COMUNICATION */
    /**
     * 
     * @return 
     */
    public default MSG recievedMsgFromChat() {
        final MSG msgSent = new MSG(MSG.Type.MESSAGE);
        msgSent.setAction(MSG_FROM_CHAT);

        return msgSent;
    }

    /**
     * 
     * @return
     */
    public default MSG recievedMsgFromSingle() {
        final MSG msgSent = new MSG(MSG.Type.MESSAGE);
        msgSent.setAction(MSG_FROM_SINGLE);

        return msgSent;
    }

    /**
     * 
     * @param chatId
     * @param emisorId
     * @param line
     * @return
     */
    public default MSG sendMsgToChat(String chatId, String emisorId, String line) {
        final MSG msgOut = new MSG(MSG.Type.MESSAGE);

        msgOut.setAction(MSG_TO_CHAT);
        msgOut.setEmisor(emisorId);
        msgOut.setReceptor(chatId);
        msgOut.setBody(line);

        return msgOut;
    }

    /**
     * 
     * @param emisorId
     * @param emisorNick
     * @param receptorId
     * @param receptorNick
     * @param msgTxt
     * @return
     */
    public default MSG sendMsgToSingle(String emisorId, String emisorNick, String receptorId, String receptorNick,
            String msgTxt) {
        final MSG msgOut = new MSG(MSG.Type.MESSAGE);

        msgOut.setAction(MSG_TO_SINGLE);
        msgOut.setEmisor(emisorId);
        msgOut.setParameter(0, emisorNick);
        msgOut.setParameter(1, receptorNick);
        msgOut.setReceptor(receptorId);
        msgOut.setBody(msgTxt);

        return msgOut;
    }

    /* ERRORs */
    public default MSG conNotFound() {
        final MSG respond = new MSG(MSG.Type.ERROR);
        respond.setAction(ERROR_CLIENT_NOT_FOUND);

        return respond;
    }

    public default MSG chatNotFound() {
        final MSG respond = new MSG(MSG.Type.ERROR);
        respond.setAction(ERROR_CHAT_NOT_FOUND);

        return respond;
    }

}
