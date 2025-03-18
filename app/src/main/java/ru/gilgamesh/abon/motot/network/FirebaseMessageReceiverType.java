package ru.gilgamesh.abon.motot.network;

import lombok.Getter;

@Getter
public enum FirebaseMessageReceiverType {
    MESSAGE ("Message", 1),
    NOTIFICATION ("Notification", 2),
    COMPETITION ("Competition", 3),
    GROUP_INVITE ("GROUP_INVITE", 4),
    GROUP_CHANGE_BASE ("GROUP_CHANGE_BASE", 5),
    GROUP_RIDING_MEMBER ("GROUP_RIDING_MEMBER", 6),
    COMPETITION_NEW_MEMBER ("COMPETITION_NEW_MEMBER", 7),
    COMPETITION_APPROVE_MEMBER ("COMPETITION_APPROVE_MEMBER", 8);

    private final String type;
    private final Integer code;

    FirebaseMessageReceiverType(String message, int i) {
        this.type = message;
        this.code = i;
    }

    public boolean equal (String in) {
        if (in == null || in.isEmpty()) return false;
        return in.equals(this.type);
    }
}
