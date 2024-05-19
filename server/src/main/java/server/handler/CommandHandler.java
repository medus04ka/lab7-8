package server.handler;

import common.build.request.Request;
import common.build.response.Response;
import common.build.response.NoSuchCommandRes;
import org.slf4j.Logger;
import server.App;
import server.managers.AuthManager;
import server.managers.CommandManager;
import server.repo.auth.exceptions.BadCredentialsException;

import java.sql.SQLException;

/**
 * The type Command handler.
 */
public class CommandHandler {
    private final CommandManager manager;
    private final AuthManager authManager;
    private final Logger logger = App.logger;
    /**
     * Instantiates a new Command handler.
     *
     * @param manager the manager
     */
    public CommandHandler(CommandManager manager, AuthManager authManager) {
        this.manager = manager;
        this.authManager = authManager;
    }

    /**
     * Handle response.
     *
     * @param request the request
     * @return the response
     */
    public Response handle(Request request) throws SQLException {
        if (!request.isAuth()) {var user = request.getUser();
            if (user == null || authManager.authenticateUser(user.getName(), user.getPassword()) <= 0) {
                throw new BadCredentialsException("Неверные учетные данные. Пожалуйста, войдите в свой аккаунт.");
            }

        }
        var command = manager.getCommands().get(request.getName());
        if (command == null) return new NoSuchCommandRes(request.getName());
        return command.apply(request);
    }
}