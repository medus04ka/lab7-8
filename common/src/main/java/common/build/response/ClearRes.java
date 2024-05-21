package common.build.response;

import common.util.Commands;

public class ClearRes extends Response {
    public ClearRes(String error) {
        super(Commands.CLEAR, error);
    }
}