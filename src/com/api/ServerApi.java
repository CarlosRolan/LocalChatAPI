package com.api;

import com.data.MSG;

public interface ServerApi extends Codes {

    default MSG sendErrorChatNotFound() {
        MSG respond = null;
        respond = new MSG(MSG.Type.ERROR);
        respond.setAction(ERROR_CHAT_NOT_FOUND);
        return respond;
    }

    default MSG sendErrorClientNotFound() {
        MSG respond = null;
        respond = new MSG(MSG.Type.ERROR);
        respond.setAction(ERROR_CLIENT_NOT_FOUND);
        return respond;
    }

}
