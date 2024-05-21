package server.network;

import common.build.request.Request;
import common.build.response.NoSuchCommandRes;
import common.build.response.Response;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import server.App;
import server.handler.CommandHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.atomic.AtomicReference;

/**
 * UDP обработчик запросов
 */
abstract class UDPServer {
    private final InetSocketAddress addr;
    private final CommandHandler commandHandler;
    private Runnable afterHook;
    private final ForkJoinPool service;
    private final ForkJoinPool responsePool;
    private final Logger logger = App.logger;

    private boolean running = true;

    public UDPServer(InetSocketAddress addr, CommandHandler commandHandler) {
        this.addr = addr;
        this.commandHandler = commandHandler;
        responsePool = new ForkJoinPool();
        service = ForkJoinPool.commonPool();
//        new ForkJoinPool()
    }

    public InetSocketAddress getAddr() {
        return addr;
    }

    /**
     * Получает данные с клиента.
     * Возвращает пару из данных и адреса клиента
     */
    public abstract Pair<Byte[], SocketAddress> receiveData() throws IOException;

    /**
     * Отправляет данные клиенту
     */
    public abstract void sendData(byte[] data, SocketAddress addr) throws IOException;

    public abstract void connectToClient(SocketAddress addr) throws SocketException;

    public abstract void disconnectFromClient();

    public abstract void close();

    public void run() {
        logger.info("Сервер запущен по адресу " + addr);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.getFactory().newThread(forkJoinPool);

        while (running) {
            Pair<Byte[], SocketAddress> dataPair = null;
            try {
                dataPair = receiveData();
            } catch (Exception e) {
                logger.error("Ошибка получения данных : " + e.getMessage(), e);
                disconnectFromClient();
            }
            RecursiveAction action = null;
            if (dataPair != null) {
                action = serverReceive(dataPair);
                action.fork();
                action.join();
            }

            if (action != null) {
                forkJoinPool.execute(action);
            }
//            System.out.println(forkJoinPool.getActiveThreadCount());
        }

        close();
    }

    private RecursiveAction serverReceive(Pair<Byte[], SocketAddress> dataPair) {

        return new RecursiveAction() {
            @Override
            protected void compute() {


                var dataFromClient = dataPair.getKey();
                var clientAddr = dataPair.getValue();

                try {
                    connectToClient(clientAddr);
                    logger.info("Соединено с " + clientAddr);
                } catch (Exception e) {
                    logger.error("Ошибка соединения с клиентом : " + e.getMessage(), e);
                }

                Request request = null;
                try {
                    request = SerializationUtils.deserialize(ArrayUtils.toPrimitive(dataFromClient));
                    logger.info("Обработка " + request + " из " + clientAddr);
                } catch (SerializationException e) {
                    logger.error("Невозможно десериализовать объект запроса." + e.getMessage(), e);
                    disconnectFromClient();
                }

                AtomicReference<Response> response = new AtomicReference<>(null);
                Request finalRequest = request;
                Thread thread = new Thread(() -> {
                    try {
                        assert finalRequest != null;
                        response.set(commandHandler.handle(finalRequest));
                        if (afterHook != null) afterHook.run();
                    } catch (Exception e) {
                        logger.error("Ошибка выполнения команды : " + e.getMessage(), e);
                    }
                    if (response.get() == null) response.set(new NoSuchCommandRes(finalRequest.getName()));
                });

                thread.start();

                try {
                    thread.join();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                var data = SerializationUtils.serialize(response.get());
                logger.info("Ответ: " + response);

                try {
                    sendData(data, clientAddr);
                    logger.info("Отправлен ответ клиенту " + clientAddr);
                } catch (Exception e) {
                    logger.error("Ошибка ввода-вывода : " + e.getMessage(), e);
                }

                disconnectFromClient();
                logger.info("Отключение от клиента " + clientAddr);
            }
        };
    }


    /**
     * Вызывает хук после каждого запроса.
     *
     * @param afterHook хук, вызываемый после каждого запроса
     */
    public void setAfterHook(Runnable afterHook) {
        this.afterHook = afterHook;
    }

    public void stop() {
        running = false;
    }
}