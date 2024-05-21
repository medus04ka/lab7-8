package common.build.response;

import common.model.HumanBeing;
import common.util.Commands;

import java.util.List;

public class ShowRes extends Response {
    public final List<HumanBeing> person;

    public ShowRes(List<HumanBeing> person, String error) {
        super(Commands.SHOW, error);
        this.person = person;
    }
}