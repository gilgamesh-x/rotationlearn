package ru.gilgamesh.abon.motot.model;

import lombok.Getter;

@Getter
public enum ActivityResult {
    ROUTE_REFRESH (555)
    , ROUTE_EDIT_CLOSE (1)
    , ROUTE_DELETE (2)
    , ROUTE_REFRESH_POINT (3)
    , COMPETITION_EDIT_CLOSE(4)
    , COMPETITION_REFRESH(5)
    , REGISTRATION_SUCCESS(6)
    , REGISTRATION_CANCEL(7)
    , GROUP_REFRESH(8)
    , GROUP_DELETE(9)
    , ROUTE_DELETE_POINT(10)
    , PROFILE_EDIT(100333)
    , PROFILE_COUNT_SUBS(100334);

    private final int value;

    ActivityResult(final int val) {
        value = val;
    }
}
