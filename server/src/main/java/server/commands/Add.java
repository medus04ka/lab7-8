package server.commands;

import common.build.request.*;
import common.build.response.*;
import server.service.HumanBeingService;

/**
 * Команда 'add'. Добавляет новый элемент в коллекцию.
 *
 */
public class Add extends Command {
    private final HumanBeingService humanBeingService;

    /**
     * Instantiates a new Add.
     *
     * @param humanBeingService the human being repository
     */
    public Add(HumanBeingService humanBeingService) {
        super("add {element}", "добавить новый элемент в коллекцию");
        this.humanBeingService = humanBeingService;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (AddReq) request;
        try {
            if (!req.person.validate()) {
                return new AddRes(-1, "Поля друга не валидны! Продукт не добавлен!");
            }
            var newId = humanBeingService.add(req.getUser(), req.person);
            return new AddRes(newId, null);
        } catch (Exception e) {
            return new AddRes(-1, e.toString());
        }
    }
}