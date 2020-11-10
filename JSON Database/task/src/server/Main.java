package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    static final String address = "127.0.0.1";
    static final int port = 23456;

    public static void main(String[] args) {
        SimpleDataBase database = new SimpleDataBase();
        int poolSize = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(poolSize);
        System.out.println("Server started!");
        while (true) {
            try (ServerSocket server = new ServerSocket(port)) {
                Session session = new Session(server.accept(), database);
                var exitFlag = executor.submit(session);
                if (exitFlag.get()) {
                    executor.shutdown();
                    break;
                }
            } catch (IOException | ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
