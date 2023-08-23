package com.comunication.handlers;

import com.comunication.MSG;

public interface IMSGHandler {

    void handleRequest(MSG msgRequest);

    void handleMessage(MSG msgMessage);

    void handleError(MSG msgError);

    default void handleMsg(MSG msg) {
        switch (msg.MSG_TYPE) {

            case REQUEST:
                handleRequest(msg);
                break;

            case MESSAGE:
                handleMessage(msg);
                break;

            case ERROR:
                handleError(msg);
                break;

            default:
                break;
        }
    }

}
