package server.commands;

import common.build.request.AuthReq;
import common.build.request.RegisterReq;
import common.build.request.Request;
import common.build.response.AuthRes;
import common.build.response.RegisterRes;
import common.build.response.Response;
import common.util.Commands;
import org.postgresql.util.PSQLException;
import server.managers.AuthManager;
import server.repo.auth.exceptions.UserNotExistException;

public class Auth extends Command {
    private final AuthManager authManager;

    public Auth(AuthManager authManager) {
        super(Commands.AUTH, "Войти в системку под юзерка, ведь мы любим создавать убийц ура =)");
        this.authManager = authManager;
    }


    @Override
    public Response apply(Request request) {
        var req = (AuthReq) request;
        var user = req.getUser();

        try {
            var newUserId = authManager.authenticateUser(user.getName(), user.getPassword());
            return new AuthRes(user.copy(newUserId), null);
        } catch (UserNotExistException e) {
            var message = "Юзерка не сущ";
            return new AuthRes(user, message);

        } catch (Exception e) {
            return new AuthRes(user, e.getMessage());
        }
    }
}
