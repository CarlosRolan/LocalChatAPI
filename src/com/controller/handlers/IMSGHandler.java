package com.controller.handlers;

import com.data.MSG;

public interface IMSGHandler {

    void handleRequest(MSG msgRequest);

    void handleMessage(MSG msgMessage);

    void handleError(MSG msgError);

    void unHandledMSG(MSG unHandled);

}
