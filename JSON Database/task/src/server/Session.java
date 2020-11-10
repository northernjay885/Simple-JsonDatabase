package server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.rmi.server.ExportException;
import java.util.concurrent.Callable;

class Session implements Callable<Boolean> {
    private final Socket socket;
    private final SimpleDataBase database;
    private boolean exitFlag = false;

    public Session(Socket socketForClient, SimpleDataBase database) {
        this.socket = socketForClient;
        this.database = database;
    }

    @Override
    public Boolean call() {
        try (DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())
        ) {
            String msgReceived = input.readUTF();
            Gson gson = new Gson();
            JsonObject obj = gson.fromJson(msgReceived, JsonObject.class);
            ResponseObject responseObj = new ResponseObject();
            interactWithDatabase(obj, responseObj);
            if (obj.get("type").getAsString().equals("exit")) {
                exitFlag = true;
            }
            String msgSent = gson.toJson(responseObj);
            output.writeUTF(msgSent);
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return exitFlag;
    }

    public void interactWithDatabase(JsonObject obj, ResponseObject responseObj) throws Exception {
        Controller controller = new Controller();
        JsonElement value = obj.get("value");
        JsonArray keyArray = null;
        if (obj.get("key").isJsonArray()) {
            keyArray = obj.get("key").getAsJsonArray();
        } else if (obj.get("key").isJsonPrimitive()) {
            String keyStr = obj.get("key").getAsString();
            keyArray = new JsonArray();
            keyArray.add(keyStr);
        }
        try {
            switch (obj.get("type").getAsString()) {
                case "get":
                    Command get = new GetCommand(database, keyArray, responseObj);
                    controller.setCommand(get);
                    controller.executeCommand();
                    break;

                case "set":
                    Command set = new SetCommand(database, keyArray, value, responseObj);
                    controller.setCommand(set);
                    controller.executeCommand();
                    break;

                case "delete":
                    Command delete = new DeleteCommand(database, keyArray, responseObj);
                    controller.setCommand(delete);
                    controller.executeCommand();
                    break;

                case "exit":
                    Command exit = new ExitCommand(responseObj);
                    controller.setCommand(exit);
                    controller.executeCommand();
                    break;

                default:
                    throw new Exception("Unrecognized command!");
                }
            } catch (NumberFormatException e) {
                    responseObj.setResponse("ERROR");
                    responseObj.setReason("No such key");
            } catch (ExportException e) {
                e.getMessage();
        }
    }
}

class ResponseObject {
    private String response;
    private String reason;
    private JsonElement value;

    public ResponseObject() {
        this.response = null;
        this.reason = null;
        this.value = null;
    }

    public ResponseObject(String response) {
        this.response = response;
        this.reason = null;
        this.value = null;
    }

    public ResponseObject(String response, String reason) {
        this.response = response;
        this.reason = reason;
        this.value = null;
    }

    public ResponseObject(String response, String reason, JsonElement value) {
        this.response = response;
        this.reason = reason;
        this.value = value;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public void setValue(JsonElement value) {
        this.value = value;
    }
}