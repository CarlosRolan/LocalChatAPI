package com.controller.handlers;

import com.data.PKG;

public interface IPKGHandler {

    void handleMixed(PKG pkgMixed);

    void handleCollection(PKG pkgCollection);

    void unHandledPKG(PKG unHandled);

}
