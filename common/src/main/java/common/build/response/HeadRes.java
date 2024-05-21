package common.build.response;

import common.model.HumanBeing;
import common.util.Commands;

public class HeadRes extends Response {
    public final HumanBeing person;

    public HeadRes(HumanBeing person, String error) {
        super(Commands.HEAD, error);
        this.person = person;
    }
}