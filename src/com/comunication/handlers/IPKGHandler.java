package com.comunication.handlers;

import com.comunication.PKG;

public interface IPKGHandler {

    void handleMixed(PKG pkgMixed);

    void handleCollection(PKG pkgCollection);

    void unHandledPKG(PKG unHandled);

}
