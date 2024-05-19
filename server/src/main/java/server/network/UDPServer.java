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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

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

    /**
     * Instantiates a new Udp server.
     *
     * @param addr           the addr
     * @param commandHandler the command handler
     */
    public UDPServer(InetSocketAddress addr, CommandHandler commandHandler) {
        this.addr = addr;
        this.commandHandler = commandHandler;
        responsePool = new ForkJoinPool();
        service = new ForkJoinPool();
//        new ForkJoinPool()
    }

    /**
     * Gets addr.
     *
     * @return the addr
     */
    public InetSocketAddress getAddr() {
        return addr;
    }

    /**
     * Получает данные с клиента.
     * Возвращает пару из данных и адреса клиента
     *
     * @return the pair
     * @throws IOException the io exception
     */
    public abstract Pair<Byte[], SocketAddress> receiveData() throws IOException;

    /**
     * Отправляет данные клиенту
     *
     * @param data the data
     * @param addr the addr
     * @throws IOException the io exception
     */
    public abstract void sendData(byte[] data, SocketAddress addr) throws IOException;

    /**
     * Connect to client.
     *
     * @param addr the addr
     * @throws SocketException the socket exception
     */
    public abstract void connectToClient(SocketAddress addr) throws SocketException;

    /**
     * Disconnect from client.
     */
    public abstract void disconnectFromClient();

    /**
     * Close.
     */
    public abstract void close();

    /**
     * Run.
     */
    public void run() {
        logger.info("Сервер запущен по адресу " + addr);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        forkJoinPool.getFactory().newThread(forkJoinPool);

        while (running) {
            Pair<Byte[], SocketAddress> dataPair;
            try {
                dataPair = receiveData();
            } catch (Exception e) {
                logger.error("Ошибка получения данных : " + e.getMessage(), e);
                disconnectFromClient();
                continue;
            }

            var dataFromClient = dataPair.getKey();
            var clientAddr = dataPair.getValue();

            try {
                connectToClient(clientAddr);
                logger.info("Соединено с " + clientAddr);
            } catch (Exception e) {
                logger.error("Ошибка соединения с клиентом : " + e.getMessage(), e);
            }

            Request request;
            try {
                request = SerializationUtils.deserialize(ArrayUtils.toPrimitive(dataFromClient));
                logger.info("Обработка " + request + " из " + clientAddr);
            } catch (SerializationException e) {
                logger.error("Невозможно десериализовать объект запроса." + e.getMessage(), e);
                disconnectFromClient();
                continue;
            }

            Response response = null;
            try {
                response = commandHandler.handle(request);
                if (afterHook != null) afterHook.run();
            } catch (Exception e) {
                logger.error("Ошибка выполнения команды : " + e.getMessage(), e);
            }
            if (response == null) response = new NoSuchCommandRes(request.getName());

            var data = SerializationUtils.serialize(response);
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
        close();

    }

    /**
     * Вызывает хук после каждого запроса.
     *
     * @param afterHook хук, вызываемый после каждого запроса
     */
    public void setAfterHook(Runnable afterHook) {
        this.afterHook = afterHook;
    }

    /**
     * Stop.
     */
    public void stop() {
        running = false;
    }
}