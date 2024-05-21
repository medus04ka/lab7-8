package server.commands;

import common.build.request.Request;
import common.build.response.*;
import server.repo.HumanBeingNativeBasedRepository;
import server.service.HumanBeingService;

/**
 * Команда 'head'. Выводит последний элемент коллекции.
 */
public class Head extends Command {
    private final HumanBeingService service;

    public Head(HumanBeingService service) {
        super("head", "вывести последний элемент коллекции");
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
            return new HeadRes(service.getFirstElement(), null);
        } catch (Exception e) {
            return new HeadRes(null, e.toString());
        }
    }
}