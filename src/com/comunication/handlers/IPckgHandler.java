package com.comunication.handlers;

import com.comunication.PKG;

public interface IPckgHandler {

    void handleMixed(PKG pckgMixed);

    void handleCollection(PKG pckgCollection);

    default void handlePckg(PKG pckg) {
        switch (pckg.PACKAGE_TYPE) {

            case MIXED:
                handleMixed(pckg);
                break;
            case COLLECTION:
                handleCollection(pckg);
                break;

        }
    }

}
