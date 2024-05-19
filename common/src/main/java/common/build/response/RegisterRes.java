package common.build.response;


import common.model.User;
import common.util.Commands;

public class RegisterRes extends Response {
  public final User user;
  public RegisterRes(User user, String error) {
    super(Commands.REGISTER, error);
    this.user = user;
  }
}
