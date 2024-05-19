package server.commands;

import common.model.HumanBeing;
import common.build.request.Request;
import common.build.response.*;
import server.repo.HumanBeingNativeBasedRepository;
import server.service.HumanBeingService;

/**
 * Команда 'sum_of_impactSpeed'. Сумма скорости всех продуктов.
 */
public class SumOfImpactSpeed extends Command {
    private final HumanBeingService service;

    public SumOfImpactSpeed(HumanBeingService service) {
        super("sum_of_impactSpeed", "вывести сумму значений поля impactSpeed для всех элементов коллекции");
        this.service = service;
    }

    /**
     * Выполняет команду
     * @return Успешность выполнения команды.
     */
    @Override
    public Response apply(Request request) {
        try {
            return new SumOfImpactSpeedRes(service.getSumOfImpactSpeed(), null);
        } catch (Exception e) {
            return new SumOfImpactSpeedRes(-1, e.toString());
        }
    }
}