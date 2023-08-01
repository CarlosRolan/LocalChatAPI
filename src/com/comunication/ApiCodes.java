package com.comunication;

/**
 * Description:
 * Program Name: LocalChatApi
 * 
 * @author Carlos Rolán Díaz
 * @version beta
 */
public interface ApiCodes {

    /* INFO-TYPE CODES */
    final String INFO_PRESENTATION_START = "INFO_PRESNTATION_START";
    final String INFO_NO_SERVER_RESPONSE = "INFO_NO_SERVER_RESPONSE";
    final String INFO_WAITING_RESPONSE = "INFO_WAITING_RESPONSE";
    final String INFO_CONECXION_REJECTED = "CONECXION_REJECTED";
    final String INFO_COMFIRMATION_SUCCESS = "OK";
    final String INFO_CONECXION_ACCEPTED = "CONECXION_ACCEPTED";
    final String INFO_CONNECTION_ESTABLISH = "CONNECTION_ESTABLISH";
    final String INFO_CONNECTION_CLOSED = "CONNECTION CLOSED";
    final String INFO_PRESENTATION_SUCCES = "PRESENTATION_SUCCES";

    /* REQ-TYPE CODES */
    final String REQ_PRESENT = "PRESENT";
    final String REQ_SINGLE = "SINGLE_CHAT_REQUESTED";
    final String REQ_SHOW_ALL_CON = "SHOW_ALL_CON";
    final String REQ_SHOW_ALL_CHAT = "SHOW_ALL_CHAT";
    final String REQ_ASKED_FOR_PERMISSION = "ASKED_FOR_PERMISSION";
    final String REQ_WAITING_FOR_PERMISSION = "WAITING_FOR_PERMISSION";
    final String REQ_ALLOW = "ALLOW";
    final String REQ_DENY = "DENY";
    final String REQ_START_SINGLE = "START_SINGLE";
    final String REQ_EXIT_SINGLE = "EXIT_SINGLE";
    final String REQ_CHAT = "GET_CHAT";
    final String REQ_CREATE_CHAT = "CREATE_CHAT";
    final String REQ_INIT_CHAT = "INIT_CHAT";
    final String REQ_SELECT_USER = "SELCT_USER";
    final String REQ_ADD_MEMBER = "ADD_MEMBER";
    final String REQ_DELETE_MEMBER = "DELETE_MEMBER";
    final String REQ_CHANGE_MEMBER_RIGHTS = "CHANGE_MEMBER_RIGHTS";
    final String REQ_SHOW_ALL_MEMBERS_OF_CHAT ="SHOW_ALL_MEMBERS_OF_CHAT";

    /* ERROR-TYPE CODES */
    final String ERROR_PRESENTATION = "ERROR_PRESENTATION";
    final String ERROR_SERVER_CONNECTION = "ERROR_SERVER_CONNECTION";
    final String ERROR_CLIENT_NOT_FOUND = "CLIENT_NOT_FOUND";
    final String ERROR_SELF_REFERENCE = "SELF_REFERENCE";
    final String ERROR_CHAT_NOT_FOUND = "ERROR_CHAT_NOT_FOUND";

    /* MSG-TYPE CODES */
    final String MSG_TO_SINGLE = "MSG_TO_SINGLE";
    final String MSG_TO_CHAT = "SEND_MSG_CHAT";

}
