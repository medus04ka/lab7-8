package server.managers;

import com.google.common.hash.Hashing;
import common.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import server.App;
import server.repo.auth.AuthRepository;
import server.repo.auth.exceptions.UserNotExistException;

import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AuthManager {

    private final int SALT_LENGTH = 10;
    private final String pepper;
    private final Logger logger = App.logger;
    private final AuthRepository authRepository;

    public AuthManager(Connection connection, String pepper) {
        this.pepper = pepper;
        this.authRepository = new AuthRepository(connection);
    }

    public int registerUser(String login, String password) throws SQLException {
        logger.info("Создание нового пользователя " + login);

        var salt = generateSalt();
        var passwordHash = generatePasswordHash(password, salt);

        var user = new User();
        user.setName(login);
        user.setPassword(passwordHash);
        user.setSalt(salt);

        int id = authRepository.addNewUser(user);

        logger.info("Пользователь успешно создан, id#" + id);
        return id;
    }

    public int authenticateUser(String login, String password) throws SQLException {
        logger.info("Аутентификация пользователя " + login);
        User user = authRepository.findUserByUsername(login);

        if (user == null) {
            throw new UserNotExistException();
        }

        var id = user.getId();
        var salt = user.getSalt();
        var expectedHashedPassword = user.getPassword();
        var actualHashedPassword = generatePasswordHash(password, salt);
        if (expectedHashedPassword.equals(actualHashedPassword)) {
            logger.info("Пользователь " + login + " аутентифицирован c id#" + id);
            return id;
        } else {
        logger.warn(
                "Неправильный пароль для пользователя " + login +
                        ". Ожидалось '" + expectedHashedPassword + "', получено '" + actualHashedPassword + "'");
        }

        return 0;
    }

    private String generateSalt() {
        return RandomStringUtils.randomAlphanumeric(SALT_LENGTH);
    }

    private String generatePasswordHash(String password, String salt) {
        return Hashing.sha256()
                .hashString(pepper + password + salt, StandardCharsets.UTF_8)
                .toString();
    }
}
