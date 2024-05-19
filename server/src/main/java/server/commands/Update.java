package server.commands;

import common.build.request.*;
import common.build.response.*;
import server.repo.HumanBeingNativeBasedRepository;
import server.service.HumanBeingService;

/**
 * Команда 'update'. Обновляет элемент коллекции.
 *
 */
public class Update extends Command {
    private final HumanBeingService service;

    /**
     * Instantiates a new Update.
     *
     */
    public Update(HumanBeingService service) {
        super("update <ID> {element}", "обновить значение элемента коллекции по ID");
        this.service = service;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        var req = (UpdateReq) request;
        try {
            if (!service.isExist(req.id)) {
                return new UpdateRes("Продукта с таким ID в коллекции нет!");
            }
            if (!req.updatedPerson.validate()) {
                return new UpdateRes( "Поля продукта не валидны! Продукт не обновлен!");
            }
            service.updateHumanBeing(req.getUser(), req.updatedPerson);
            return new UpdateRes(null);
        } catch (Exception e) {
            return new UpdateRes(e.toString());
        }
    }
}