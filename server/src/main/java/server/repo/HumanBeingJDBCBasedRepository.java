package server.repo;

import common.model.*;
import server.exceptions.UserNotOwnerOfObject;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static server.repo.DbUtils.*;


public class HumanBeingJDBCBasedRepository {
    public final Connection connection;
    private static final String TABLE_HUMAN_BEING = " humanbeing ";

    public HumanBeingJDBCBasedRepository(Connection connection) {
        this.connection = connection;
    }

    public int add(User user, HumanBeing humanBeing) throws SQLException {
        int id = user.getId();
        String query = INSERT + TABLE_HUMAN_BEING
                + "(name, x, y, real_hero, has_tooth_pick, impact_speed, weapon, mood, owner_id)" +
                " VALUES (?,?, ?,?,?,?,?,?,?) RETURNING id;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setString(1, humanBeing.getName());
        statement.setInt(2, humanBeing.getCoordinates().getX());
        statement.setLong(3, humanBeing.getCoordinates().getY());
        statement.setBoolean(4, humanBeing.isRealHero());
        statement.setBoolean(5, humanBeing.hasToothpick());
        statement.setLong(6, humanBeing.getImpactSpeed());
        statement.setObject(7, humanBeing.getWeaponType(), Types.OTHER);
        statement.setObject(8, humanBeing.getMood(), Types.OTHER);
        statement.setInt(9, id);

        statement.execute();
        var rs = statement.getResultSet();
        if (rs.next()) {
            return rs.getInt(1);
        }

        return -1;
    }

    public int updateHumanBeing(HumanBeing humanBeing) throws SQLException, UserNotOwnerOfObject {
        String set = """
                name = ?,
                x = ?,
                y = ?,
                real_hero = ?,
                has_tooth_pick = ?,
                impact_speed = ?,
                weapon = ?,
                mood = ?
                """;
        PreparedStatement statement = connection.prepareStatement(UPDATE + TABLE_HUMAN_BEING + " SET "
                + set + WHERE + " id = ?");
        statement.setString(1, humanBeing.getName());
        statement.setInt(2, humanBeing.getCoordinates().getX());
        statement.setLong(3, humanBeing.getCoordinates().getY());
        statement.setBoolean(4, humanBeing.isRealHero());
        statement.setBoolean(5, humanBeing.hasToothpick());
        statement.setLong(6, humanBeing.getImpactSpeed());
        statement.setObject(7, humanBeing.getWeaponType(), Types.OTHER);
        statement.setObject(8, humanBeing.getMood(), Types.OTHER);
        statement.setInt(9, humanBeing.getId());

        return statement.executeUpdate();

    }

    public HumanBeing getHumanBeing(int id) throws SQLException {
        PreparedStatement statement = connection.prepareStatement
                (SELECT + "name, x, y, real_hero, has_tooth_pick, impact_speed, weapon, mood, owner_id, creation_date"
                        + FROM + TABLE_HUMAN_BEING + " as hb " + WHERE + "? = hb.id;");
        statement.setInt(1, id);
        ResultSet resultSet = statement.executeQuery();
        HumanBeing humanBeingFromResultSet = getHumanBeingFromResultSet(resultSet, false);
        humanBeingFromResultSet.setId(id);

        return humanBeingFromResultSet;
    }

    public Collection<HumanBeing> readCollection() throws SQLException {
        String query = SELECT + "id, name, x, y, real_hero, has_tooth_pick, impact_speed, weapon, mood, owner_id, creation_date "
                + FROM + TABLE_HUMAN_BEING + ";";
        PreparedStatement statement = connection.prepareStatement(query);
        ResultSet resultSet = statement.executeQuery();
        List<HumanBeing> lst = new ArrayList<>();
        while (resultSet.next()) {
            HumanBeing hb = getHumanBeingFromResultSet(resultSet, true);
            lst.add(hb);
        }

        return lst;
    }

    public int clear(int userId) throws SQLException {
        String query = DELETE + TABLE_HUMAN_BEING + WHERE + "owner_id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);

        return statement.executeUpdate();
    }

    public int remove(int userId, int humanBeingId) throws SQLException {
        String query = DELETE + TABLE_HUMAN_BEING + WHERE + "owner_id = ? AND id = ?;";
        PreparedStatement statement = connection.prepareStatement(query);
        statement.setInt(1, userId);
        statement.setInt(2, humanBeingId);

        return statement.executeUpdate();
    }

    private HumanBeing getHumanBeingFromResultSet(ResultSet rs, boolean withId) throws SQLException {
        int i = 1;

        HumanBeing humanBeing = new HumanBeing();
        if (withId) {
            humanBeing.setId(rs.getInt(i++));
        }
        humanBeing
                .setName(rs.getString(i++))
                .setCoordinates(new Coordinates(rs.getInt(i++), rs.getLong(i++)))
                .setRealHero(rs.getBoolean(i++))
                .setHasToothpick(rs.getBoolean(i++))
                .setImpactSpeed(rs.getLong(i++))
                .setWeaponType(WeaponType.valueOf(rs.getString(i++).toString()))
                .setMood(Mood.valueOf(rs.getString(i++).toString()))
                .setOwnerId(rs.getInt(i++))
                .setCreationDate(rs.getDate(i++).toLocalDate());

        return humanBeing;
    }

    private boolean isUserCreatorOfHumanBeing(int userId, int humanBeingId) throws SQLException {
        PreparedStatement statement = connection.prepareStatement
                (SELECT + " COUNT(*) " + FROM + TABLE_HUMAN_BEING + " as hb "
                        + WHERE + "? = hb.id AND " + "? = hb.owner_id;");
        statement.setInt(1, humanBeingId);
        statement.setInt(2, userId);
        ResultSet resultSet = statement.executeQuery();
        int count = resultSet.getInt(1);

        return count == 1;
    }


}
