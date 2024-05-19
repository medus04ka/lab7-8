package server.repo.auth;

import common.model.User;
import org.slf4j.Logger;
import server.App;
import server.repo.auth.exceptions.UserAlreadyExist;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static server.repo.DbUtils.*;

public class AuthRepository {
    private final Connection connection;
    private final String TABLE_USER = " users ";

    private final Logger logger = App.logger;

    public AuthRepository(Connection connection) {
        this.connection = connection;
    }

    public User findUserByUsername(String username) throws SQLException {
        String query = SELECT + " * " + FROM + TABLE_USER + WHERE + " name = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, username);
        ResultSet resultSet = statement.executeQuery();
        User user = getUserByResultSet(resultSet);

        return user;
    }

    public int addNewUser(User user) throws SQLException {
        String query = INSERT + TABLE_USER + "(name, passw, salt) VALUES (?,?,?) RETURNING id;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, user.getName());
        statement.setString(2, user.getPassword());
        statement.setString(3, user.getSalt());
        ResultSet result = statement.executeQuery();
        result.next();
        int id = result.getInt("id");

        if (id == -1) {
            throw new UserAlreadyExist("бро с таким ником уже экзист");
        }

        return id;
    }

    private User getUserByResultSet(ResultSet rs) throws SQLException {
        while (rs.next()) {
            return new User()
                    .setId(rs.getInt("id"))
                    .setName(rs.getString("name"))
                    .setPassword(rs.getString("passw"))
                    .setSalt(rs.getString("salt"));
        }
        return null;
    }
}
