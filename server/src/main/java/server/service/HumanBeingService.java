package server.service;

import common.model.HumanBeing;
import common.model.User;
import org.slf4j.Logger;
import server.App;
import server.exceptions.UserNotOwnerOfObject;
import server.repo.HumanBeingJDBCBasedRepository;
import server.repo.HumanBeingNativeBasedRepository;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class HumanBeingService {
    private final Connection connection;
    private final HumanBeingJDBCBasedRepository humanBeingJdbcBasedRepository;
    private final Logger logger = App.logger;
    private final HumanBeingNativeBasedRepository humanBeingNativeBasedRepository;
    private final ReentrantLock lock = new ReentrantLock();

    public HumanBeingService(Connection connection) {
        this.connection = connection;
        humanBeingJdbcBasedRepository = new HumanBeingJDBCBasedRepository(connection);
        try {
            Set<HumanBeing> set = new HashSet<>(humanBeingJdbcBasedRepository.readCollection());
            Queue<HumanBeing> queue = new PriorityQueue<>(set);
            humanBeingNativeBasedRepository = new HumanBeingNativeBasedRepository(queue);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Queue<HumanBeing> get() {
        return humanBeingNativeBasedRepository.get();
    }

    public int add(User user, HumanBeing element) {
        int id = -1;
        try {
            id = humanBeingJdbcBasedRepository.add(user, element);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return id;
        }
        element.setId(id);
        element.setOwnerId(user.getId());
        lock.lock();
        humanBeingNativeBasedRepository.add(element);
        lock.unlock();

        return id;
    }

    public LocalDateTime getLastInitTime() {
        return humanBeingNativeBasedRepository.getLastInitTime();
    }

    public LocalDateTime getLastSaveTime() {
        return humanBeingNativeBasedRepository.getLastSaveTime();
    }

    public String getTypeOfCollection() {
        return humanBeingNativeBasedRepository.type();
    }

    public List<HumanBeing> getSortedHumanBeings() {
        return humanBeingNativeBasedRepository.sorted();
    }

    public boolean isExist(int id) {
        return humanBeingNativeBasedRepository.checkExist(id);
    }

    public void updateHumanBeing(User user, HumanBeing humanBeing) throws SQLException {
        var h = humanBeingNativeBasedRepository.getById(humanBeing.getId());
        if (h == null) {
            add(user, humanBeing);
            return;
        }

        if (h.getOwnerId() == user.getId()) {
            humanBeingJdbcBasedRepository.updateHumanBeing(humanBeing);
            lock.lock();
            var humanInCollection = humanBeingNativeBasedRepository.getById(humanBeing.getId());
            humanInCollection.update(humanBeing);
            lock.unlock();
        } else {
            throw new UserNotOwnerOfObject("анвак не твое");
        }
    }

    public void removeById(User user, int id) throws SQLException {
        HumanBeing humanBeing = humanBeingNativeBasedRepository.getById(id);
        if (humanBeing == null) {
            logger.warn("Ничего не было удалено.");
            return;
        }

        if (humanBeing.getOwnerId() != user.getId()) {
            throw new UserNotOwnerOfObject("анвак не твое");
        }

        humanBeingJdbcBasedRepository.remove(user.getId(), id);
        lock.lock();
        humanBeingNativeBasedRepository.remove(id);
        lock.unlock();
    }

    public void clear(User user) {
        try {
            humanBeingJdbcBasedRepository.clear(user.getId());
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }

        lock.lock();
        humanBeingNativeBasedRepository.get().removeIf(it -> it.getOwnerId() == user.getId());
        lock.unlock();
    }

    public HumanBeing getFirstElement() {
        return humanBeingNativeBasedRepository.first();
    }

    public long getSumOfImpactSpeed() {
        return humanBeingNativeBasedRepository.get().stream()
                .map(HumanBeing::getImpactSpeed)
                .mapToLong(Long::longValue)
                .sum();
    }

}
