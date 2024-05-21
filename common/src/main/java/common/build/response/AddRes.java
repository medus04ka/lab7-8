package common.build.response;

import common.util.Commands;

public class AddRes extends Response {
    public final int newId;

    public AddRes(int newId, String error) {
        super(Commands.ADD, error);
        this.newId = newId;
    }
}