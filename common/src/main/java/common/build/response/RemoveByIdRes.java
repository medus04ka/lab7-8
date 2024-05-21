package common.build.response;

import common.util.Commands;

public class RemoveByIdRes extends Response {
    public RemoveByIdRes(String error) {
        super(Commands.REMOVE_BY_ID, error);
    }
}