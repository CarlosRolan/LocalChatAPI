package com.comunication.handlers;

import com.comunication.PKG;

public interface IPCKGHandler {

    void handleMixed(PKG pkgMixed);

    void handleCollection(PKG pkgCollection);

    default void handlePckg(PKG pkg) {
        switch (pkg.PACKAGE_TYPE) {

            case MIXED:
                handleMixed(pkg);
                break;
            case COLLECTION:
                handleCollection(pkg);
                break;

        }
    }

}
