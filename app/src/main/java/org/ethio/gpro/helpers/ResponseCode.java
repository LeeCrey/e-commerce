package org.ethio.gpro.helpers;

public class ResponseCode {
    public static boolean isNotModified(int code) {
        return 304 == code;
    }

    public static boolean isOkay(int code) {
        return 200 == code;
    }

    public static boolean isUnAuthorized(int code) {
        return 401 == code;
    }

    public static boolean isNotFound(int code) {
        return 404 == code;
    }

    public static boolean unProcessableEntity(int code) {
        return 422 == code;
    }

    public static boolean created(int code) {
        return 201 == code;
    }
}
