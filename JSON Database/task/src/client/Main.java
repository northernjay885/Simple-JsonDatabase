package client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Main {

    static final String address = "127.0.0.1";
    static final int port = 23456;

    @Parameter(names = {"-t"})
    static String type;
    @Parameter(names = {"-k"})
    static String key;
    @Parameter(names = {"-v"})
    static String value;
    @Parameter(names = {"-in"})
    static String fileName;

    public static void main(String[] args) {
        Main main = new Main();

        JCommander.newBuilder()
                .addObject(main)
                .build()
                .parse(args);

        main.run();
    }

    public void run() {
        System.out.println("Client started!");

        try (Socket socket = new Socket(InetAddress.getByName(address), port);
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            Gson gson = new Gson();
            String fileContent = null;
            if (fileName == null) {
                fileContent = JsonBuilder.newBuilder()
                        .addValue("type", type)
                        .addValue("key", key)
                        .addValue("value", value).getAsString();
            } else {
                String relativePath = "src/client/data/";
                try  {
                    fileContent = new String(Files.readAllBytes(Paths.get(relativePath + fileName)));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            assert fileContent != null;
            output.writeUTF(fileContent);
            System.out.println("Sent: " + fileContent);
            String msgReceived = input.readUTF();
            msgReceived = msgReceived.replaceAll("\\\\", "");
            System.out.println("Received: " + msgReceived);

        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}

class JsonBuilder {

    private final Gson gson = new Gson();
    private final JsonObject jsonObject = new JsonObject();

    private JsonBuilder() {

    }

    public static JsonBuilder newBuilder() {
        return new JsonBuilder();
    }

    public JsonBuilder addValue(String key, String value) {
        jsonObject.addProperty(key, value);
        return this;
    }

    public JsonBuilder addValue(String key, JsonElement value) {
        jsonObject.add(key, value);
        return this;
    }

    public JsonBuilder addJsonObject(String key, JsonObject object) {
        jsonObject.add(key, object);
        return this;
    }

    public String getAsString() {
        return jsonObject.toString();
    }

    public JsonObject getAsJsonObject() {
        return jsonObject;
    }
}