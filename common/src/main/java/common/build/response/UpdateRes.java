package common.build.response;

import common.util.Commands;

public class UpdateRes extends Response {
    public UpdateRes(String error) {
        super(Commands.UPDATE, error);
    }
}