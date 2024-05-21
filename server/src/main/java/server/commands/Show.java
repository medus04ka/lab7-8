package server.commands;

import common.build.request.Request;
import common.build.response.*;
import server.repo.HumanBeingNativeBasedRepository;
import server.service.HumanBeingService;

/**
 * Команда 'show'. Выводит все элементы коллекции.
 */
public class Show extends Command {
    private final HumanBeingService service;

    public Show(HumanBeingService service) {
        super("show", "вывести все элементы коллекции");
        this.service = service;
    }

    /**
     * Выполняет команду
     *
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        try {
            return new ShowRes(service.getSortedHumanBeings(), null);
        } catch (Exception e) {
            return new ShowRes(null, e.toString());
        }
    }
}