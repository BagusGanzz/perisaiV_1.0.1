package com.perisaimobile.iface;

public interface Communicator {
    public static final int TYPE_IGNORE_SETTING = 2;
    public static final int TYPE_REMOVE_SETTING = 4;
    public static final int TYPE_TRUST_APP = 1;
    public static final int TYPE_UNINSTALL_APP = 3;

    void respond(IProblem iProblem, int i);
}
