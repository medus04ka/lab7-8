package server.repo;

import com.google.common.collect.Iterables;
import common.model.HumanBeing;
import common.util.Comparators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.App;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Оперирует коллекцией.
 */
public class HumanBeingNativeBasedRepository {
    private final Queue<HumanBeing> collection;
    private LocalDateTime lastInitTime;
    private LocalDateTime lastSaveTime;
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    public HumanBeingNativeBasedRepository(Collection<HumanBeing> collection) {
        this.lastInitTime = null;
        this.lastSaveTime = null;
        this.collection = (PriorityQueue<HumanBeing>) collection;
    }

    public boolean validateAll() {
        for (var person : new ArrayList<>(get())) {
            if (!person.validate()) {
                logger.warn("Объект с id=" + person.getId() + " имеет невалидные поля.");
                return false;
            }
        }
        logger.info("Все загруженные объекты валидны.");
        return true;
    }

    public Queue<HumanBeing> get() {
        return collection;
    }

    public LocalDateTime getLastInitTime() {
        return lastInitTime;
    }

    public LocalDateTime getLastSaveTime() {
        return lastSaveTime;
    }

    public String type() {
        return collection.getClass().getName();
    }

    public int size() {
        return collection.size();
    }

    public HumanBeing first() {
        if (collection.isEmpty()) return null;
        return sorted().get(0);
    }

    public HumanBeing last() {
        if (collection.isEmpty()) return null;
        return Iterables.getLast(sorted());
    }

    public List<HumanBeing> sorted() {
        return new ArrayList<>(collection).stream().sorted(new Comparators()).collect(Collectors.toList());
    }

    public HumanBeing getById(int id) {
        for (HumanBeing element : collection) {
            if (element.getId() == id) return element;
        }
        return null;
    }

    public boolean checkExist(int id) {
        return getById(id) != null;
    }

    public HumanBeing getByValue(HumanBeing elementToFind) {
        for (HumanBeing element : collection) {
            if (element.equals(elementToFind)) return element;
        }
        return null;
    }

    public int add(HumanBeing element) {
        collection.add(element);
        return element.getId();
    }

    public void remove(int id) {
        collection.removeIf(person -> person.getId() == id);
    }

    public void clear() {
        collection.clear();
    }

//    public void save() {
//        manager.writeCollection(collection);
//        lastSaveTime = LocalDateTime.now();
//        logger.info("Коллекция успешно сохранена.");
//    }
//
//    private void load() {
//        collection = (PriorityQueue<HumanBeing>) manager.readCollection();
//        lastInitTime = LocalDateTime.now();
//        logger.info("Коллекция успешно загружена.");
//    }

    @Override
    public String toString() {
        if (collection.isEmpty()) return "Коллекция пуста!";
        var info = new StringBuilder();
        for (HumanBeing person : collection) {
            info.append(person);
            info.append("\n\n");
        }
        return info.substring(0, info.length() - 2);
    }
}
