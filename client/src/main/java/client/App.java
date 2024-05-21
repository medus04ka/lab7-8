package client;

import client.cl.Runner;
import client.netw.UDP;
import client.util.console.StandardConsole;

import java.io.IOException;
import java.net.InetAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Главный класс клиентского приложения.
 */
public class App {
    private static final int PORT = 1506;

    public static final Logger logger = LoggerFactory.getLogger("ClientLogger");

    public static void main(String[] args) {
        var console = new StandardConsole();
        try {
            var client = new UDP(InetAddress.getLocalHost(), PORT);
            var cl = new Runner(client, console);

            cl.interactiveMode();
        } catch (IOException e) {
            logger.info("Невозможно подключиться к серверу.", e);
            System.out.println("Невозможно подключиться к серверу!");
        }
    }
}
