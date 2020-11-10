package server;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;


class SimpleDataBase {
    JsonObject jsonObj;
    ReadWriteLock lock;
    String pathToFile;

    public SimpleDataBase() {
        jsonObj = new JsonObject();
        lock = new ReentrantReadWriteLock();
        pathToFile = "src/server/data/db.json";
    }

    public synchronized void get(JsonArray keyArray, ResponseObject responseObj) {
        JsonElement tmpObj = jsonObj;
        String resStr = null;
        try {
            for (JsonElement keyEntry : keyArray) {
                String curKey = keyEntry.getAsString();
                responseObj.setResponse("the current key is" + curKey);
                if (tmpObj.isJsonObject()) {
                    if (tmpObj.getAsJsonObject().has(curKey)) {
                        tmpObj = tmpObj.getAsJsonObject().get(curKey);
                    } else {
                        responseObj.setResponse("ERROR");
                        responseObj.setReason("No such key " + curKey);
                    }
                } else {
                    responseObj.setResponse("ERROR");
                    responseObj.setReason("This should be an Json Object, not other Type!");
                    break;
                }
            }
            if (tmpObj.isJsonPrimitive()) {
                responseObj.setResponse("OK");
                responseObj.setValue(tmpObj.getAsJsonPrimitive());
            } else if (tmpObj.isJsonObject()) {
                Gson gson = new Gson();
                JsonElement newEle = tmpObj.getAsJsonObject();
                responseObj.setResponse("OK");
                responseObj.setValue(newEle);
            } else {
                responseObj.setResponse("OK");
                responseObj.setValue(null);
            }


        } catch (Exception e) {
            responseObj.setResponse("ERROR" + e.getMessage());
            responseObj.setReason("No such key");
        }

    }

    public synchronized void set(JsonArray keyArray, JsonElement value, ResponseObject responseObj) {
        try {
            JsonElement tmpObj = jsonObj;

            for (int i = 0; i < keyArray.size() - 1; i++) {
                String curKey = keyArray.get(i).getAsString();
                if (tmpObj.isJsonObject()) {
                    if (tmpObj.getAsJsonObject().has(curKey)) {
                        tmpObj = tmpObj.getAsJsonObject().get(curKey);
                    } else {
                        tmpObj.getAsJsonObject().add(curKey, new JsonObject());
                    }
                } else {
                    responseObj.setResponse("ERROR");
                    responseObj.setReason("This should be an Json Object, not other Type!");
                    break;
                }
            }
            if (tmpObj.isJsonObject()) {
                String curKey = keyArray.get(keyArray.size() - 1).getAsString();
                if (tmpObj.getAsJsonObject().has(curKey)) {
                    tmpObj.getAsJsonObject().remove(curKey);
                }
                tmpObj.getAsJsonObject().add(curKey, value);
                responseObj.setResponse("OK");
            } else {
                responseObj.setResponse("ERROR");
                responseObj.setReason("This should be an Json Object, not other Type!");
            }
            //write the database into a json file
            Lock writeLock = lock.writeLock();
            File file = new File(pathToFile);
            System.out.println(file.getAbsolutePath());
            writeLock.lock();
            try (FileWriter writer = new FileWriter(file)) {
                Gson gson = new Gson();
                String strDataBase = gson.toJson(jsonObj);
                writer.write(strDataBase);
            } catch (FileNotFoundException e) {
                System.out.println("File not found:" + pathToFile);
            }
            writeLock.unlock();

        } catch (Exception e) {
            responseObj.setResponse("ERROR");
        }
    }

    public synchronized void delete(JsonArray keyArray, ResponseObject responseObj) {
        try {
            JsonElement tmpObj = jsonObj;
            for (int i = 0; i < keyArray.size() - 1; i++) {
                String curKey = keyArray.get(i).getAsString();
                if (tmpObj.isJsonObject()) {
                    if (tmpObj.getAsJsonObject().has(curKey)) {
                        tmpObj = tmpObj.getAsJsonObject().get(curKey);
                    } else {
                        responseObj.setResponse("ERROR");
                        responseObj.setReason("No such key");
                        break;
                    }
                } else {
                    responseObj.setResponse("ERROR");
                    responseObj.setReason("This should be an Json Object, not other Type!");
                    break;
                }
            }

            if (tmpObj.isJsonObject()) {
                String curKey = keyArray.get(keyArray.size() - 1).getAsString();
                if (tmpObj.getAsJsonObject().has(curKey)) {
                    tmpObj.getAsJsonObject().remove(curKey);
                    responseObj.setResponse("OK");
                } else {
                    responseObj.setResponse("ERROR");
                    responseObj.setReason("No such key");
                }
            } else {
                responseObj.setResponse("ERROR");
                responseObj.setReason("This should be an Json Object, not other Type!");
            }
            //write the database into a json file
            Lock writeLock = lock.writeLock();
            File file = new File(pathToFile);
            System.out.println(file.getAbsolutePath());
            writeLock.lock();
            try (FileWriter writer = new FileWriter(file)) {
                Gson gson = new Gson();
                String strDataBase = gson.toJson(jsonObj);
                writer.write(strDataBase);
            } catch (FileNotFoundException e) {
                System.out.println("File not found:" + pathToFile);
            }
            writeLock.unlock();

        } catch (Exception e) {
            responseObj.setResponse("ERROR");
        }
    }
}

interface Command {
    void execute();
}

class SetCommand implements Command {
    private final SimpleDataBase database;
    private final ResponseObject responseObj;
    private final JsonArray keyArray;
    private final JsonElement value;

    public SetCommand(SimpleDataBase database, JsonArray KeyArray, JsonElement value, ResponseObject responseObj) {
        this.database = database;
        this.responseObj = responseObj;
        this.keyArray = KeyArray;
        this.value = value;
    }

    @Override
    public void execute() {
        database.set(keyArray, value, responseObj);
    }
}

class GetCommand implements Command {
    private final SimpleDataBase database;
    private final ResponseObject responseObj;
    private final JsonArray keyArray;

    public GetCommand(SimpleDataBase database, JsonArray keyArray, ResponseObject responseObj) {
        this.database = database;
        this.responseObj = responseObj;
        this.keyArray = keyArray;
    }

    @Override
    public void execute() {
        database.get(keyArray, responseObj);
    }
}

class DeleteCommand implements Command {
    private final SimpleDataBase database;
    private final ResponseObject responseObj;
    private final JsonArray keyArray;

    public DeleteCommand(SimpleDataBase database, JsonArray keyArray, ResponseObject responseObj) {
        this.database = database;
        this.responseObj = responseObj;
        this.keyArray = keyArray;
    }

    @Override
    public void execute() {
        database.delete(keyArray, responseObj);
    }
}

class ExitCommand implements Command {
    private final ResponseObject responseObj;
    public ExitCommand(ResponseObject responseObj) {
        this.responseObj = responseObj;
    }

    @Override
    public void execute() {
        responseObj.setResponse("OK");
    }
}

class Controller {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void executeCommand() {
        command.execute();
    }
}
