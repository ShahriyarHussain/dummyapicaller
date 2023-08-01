package com.testmaster.dummyapicaller.Service;

import com.testmaster.dummyapicaller.Dao.ApiResponseDao;
import com.testmaster.dummyapicaller.Enum.RequestTypes;
import com.testmaster.dummyapicaller.Exception.BadRequestException;
import com.testmaster.dummyapicaller.Exception.UnhandledErrorException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ApiResponseService {

    private final Logger LOGGER = LoggerFactory.getLogger(ApiResponseService.class);
    @Value("${response.folder.name}")
    private String responseSourceDirectory;
    @Value("${base.url}")
    private String baseUrl;
    @Value("${server.port}")
    private String serverPort;


    private final String SLASH = "/";
    private final String ERROR_MESSAGE_STRING = "[!!ERROR!!] ";

    public Map<String, Object> getApiResponseByName(String name, RequestTypes type) {
        if (!fileNameAlreadyExists(name, type)) {
            throw new BadRequestException("No such data with provided name exists");
        }
        try {
            Path path = getFilePath(name, type);
            String jsonContent = new String(Files.readAllBytes(path));
            JSONObject object = new JSONObject(jsonContent);
            return object.toMap();
        } catch (Exception e) {
            LOGGER.error(ERROR_MESSAGE_STRING + e.getMessage());
            throw new UnhandledErrorException("Error: " + e.getClass().getSimpleName() + " ---> " + e.getMessage());
        }
    }

    public void saveJsonResponse(String name, JSONObject jsonObject, RequestTypes type) {
        if (fileNameAlreadyExists(name, type)) {
            throw new BadRequestException("File With Similar Name Already Exists");
        }
        try {
            Path path = getFilePath(name, type);
            createFileByPath(path);
            writeBytesToFile(path, jsonObject.toString(2).getBytes());
        } catch (Exception e) {
            LOGGER.error(ERROR_MESSAGE_STRING + e.getMessage());
            throw new UnhandledErrorException("Error: " + e.getClass().getSimpleName() + " ---> " + e.getMessage());
        }
    }

    public void overwriteFileContentByName(String name, JSONObject jsonObject, RequestTypes type) {
        try {
            Path path = getFilePath(name, type);
            writeBytesToFile(path, jsonObject.toString(2).getBytes());
        } catch (Exception e) {
            LOGGER.error(ERROR_MESSAGE_STRING + e.getMessage());
            throw new UnhandledErrorException("Error: " + e.getClass().getSimpleName() + " ---> " + e.getMessage());
        }
    }

    public List<ApiResponseDao> getAllSavedResponsesAsDaoList() {
        String SLASH_API_SLASH = "/api/", COLON = ":";
        List<ApiResponseDao> daoList = new LinkedList<>();
        for (RequestTypes type : RequestTypes.values()) {
            for (String apiName: getSavedResponsesByRequestType(type)) {
                String url = baseUrl + COLON + serverPort + SLASH_API_SLASH + apiName;
                daoList.add(new ApiResponseDao(apiName, url, type));
            }
        }
        return daoList;
    }

    public List<String> getSavedResponsesByRequestType(RequestTypes type) {
        Path path = Paths.get(responseSourceDirectory + SLASH + type.toString() + SLASH);
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            return getFileNamesFromDirectoryStream(stream);
        } catch (IOException e) {
            LOGGER.error(ERROR_MESSAGE_STRING + e.getMessage());
            throw new UnhandledErrorException("Error: " + e.getClass().getSimpleName() + " ---> " + e.getMessage());
        }
    }
    public void deleteAllResponses() {
        for (RequestTypes type : RequestTypes.values()) {
            deleteAllByType(type);
        }
    }

    public void deleteFileByNameAndRequestType(String name, RequestTypes type) {
        try {
            deleteFile(name, type);
        } catch (Exception e) {
            LOGGER.error(ERROR_MESSAGE_STRING + e.getMessage());
            throw new UnhandledErrorException("Error: " + e.getClass().getSimpleName() + " ---> " + e.getMessage());
        }
    }

    private void deleteAllByType(RequestTypes type) {
        try {
            for (String fileName : getSavedResponsesByRequestType(type)) {
                deleteFile(fileName, type);
            }
        } catch (Exception e) {
            LOGGER.error(ERROR_MESSAGE_STRING + e.getMessage());
            throw new UnhandledErrorException("Error: " + e.getClass().getSimpleName() + " ---> " + e.getMessage());
        }
    }

    private void deleteFile(String name, RequestTypes type) throws IOException {
        Path path = getFilePath(name, type);
        Files.delete(path);
    }

    private boolean fileNameAlreadyExists(String name, RequestTypes type) {
        return Files.exists(getFilePath(name, type));
    }

    private Path getFilePath(String name, RequestTypes type) {
        String JSON_FILE_EXTENSION = ".json";
        return Paths.get(responseSourceDirectory + SLASH + type.toString() + SLASH + name + JSON_FILE_EXTENSION);
    }

    private List<String> getFileNamesFromDirectoryStream(DirectoryStream<Path> stream) {
        final int LENGTH_OF_JSON_EXTENSION = 5;
        List<String> fileNamesList = new LinkedList<>();
        for (Path file : stream) {
            if (Files.isRegularFile(file)) {
                String fileName = file.getFileName().toString();
                fileNamesList.add(fileName.substring(0, fileName.length() - LENGTH_OF_JSON_EXTENSION));
            }
        }
        return fileNamesList;
    }

    private void createFileByPath(Path path) throws IOException {
        Files.createFile(path);
    }

    private void writeBytesToFile(Path path, byte[] bytes) throws IOException {
        Files.write(path, bytes);
    }


}
