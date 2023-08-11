package com.comunication.handlers;

import com.comunication.Msg;

public interface IMsgHandler {

    void handleRequest(Msg msgRequest);

    void handleMessage(Msg msgMessage);

    void handleError(Msg msgError);

    default void handleMsg(Msg msg) {
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
        }
    }

}
