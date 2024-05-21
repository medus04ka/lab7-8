package common.build.response;

import common.util.Commands;

public class HelpRes extends Response {
    public final String helpMessage;

    public HelpRes(String helpMessage, String error) {
        super(Commands.HELP, error);
        this.helpMessage = helpMessage;
    }
}