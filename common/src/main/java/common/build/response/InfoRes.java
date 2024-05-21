package common.build.response;

import common.util.Commands;

public class InfoRes extends Response {
    public final String infoMessage;

    public InfoRes(String infoMessage, String error) {
        super(Commands.INFO, error);
        this.infoMessage = infoMessage;
    }
}