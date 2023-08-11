package com.comunication.handlers;

import com.comunication.Pckg;

public interface IPckgHandler {

    void handleMixed(Pckg pckgMixed);

    void handleCollection(Pckg pckgCollection);

    default void handlePckg(Pckg pckg) {
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
