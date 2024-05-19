package server;

import common.util.Commands;
import io.github.cdimascio.dotenv.Dotenv;

import org.checkerframework.checker.units.qual.A;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.commands.*;
import server.handler.CommandHandler;
import server.managers.AuthManager;
import server.managers.CommandManager;
import server.network.UDPDatagramServer;
import server.service.HumanBeingService;

import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Серверная часть приложения.
 *
 */
public class App {
    /**
     * The constant PORT.
     */
    public static final int PORT = 1506;

    /**
     * The constant logger.
     */
    public static Logger logger = LoggerFactory.getLogger("ServerLogger");

    public static Dotenv dotenv = Dotenv.load();

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) throws SQLException, ClassNotFoundException {

        var url = dotenv.get("DB_URL");
        var user = dotenv.get("DB_USER");
        var password = dotenv.get("DB_PASSWORD");

        if (url == null || url.isEmpty() || user == null || user.isEmpty() || password == null || password.isEmpty()) {
            System.out.println("В .env файле не обнаружены данные для подключения к базе данных");
            System.exit(1);
        }
        Class.forName("org.postgresql.Driver");
        Connection connection = DriverManager.getConnection(url, user, password);

        HumanBeingService humanBeingService = new HumanBeingService(connection);

        AuthManager authManager = new AuthManager(connection, ""); //TODO ADD PEPPER

        var commandManager = new CommandManager() {{
            register(Commands.HELP, new Help(this));
            register(Commands.ADD, new Add(humanBeingService));
            register(Commands.INFO, new Info(humanBeingService));
            register(Commands.SHOW, new Show(humanBeingService));
            register(Commands.UPDATE, new Update(humanBeingService));
            register(Commands.REMOVE_BY_ID, new RemoveById(humanBeingService));
            register(Commands.CLEAR, new Clear(humanBeingService));
            register(Commands.HEAD, new Head(humanBeingService));
            register(Commands.SUM_OF_IMPACT_SPEED, new SumOfImpactSpeed(humanBeingService));
            register(Commands.REGISTER, new Register(authManager));
        }};

        try {
            var server = new UDPDatagramServer(InetAddress.getLocalHost(), PORT, new CommandHandler(commandManager, authManager));
            server.run();
        } catch (SocketException e) {
            logger.error("Случилась ошибка сокета", e);
        } catch (UnknownHostException e) {
            logger.error("Неизвестный хост", e);
        }

    }
}