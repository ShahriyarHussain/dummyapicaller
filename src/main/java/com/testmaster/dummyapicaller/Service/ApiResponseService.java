package com.testmaster.dummyapicaller.Service;

import com.testmaster.dummyapicaller.Dao.ApiResponseDao;
import com.testmaster.dummyapicaller.Exception.BadRequestException;
import com.testmaster.dummyapicaller.Exception.UnhandledErrorException;
import org.json.JSONObject;
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

//    private final Logger LOGGER = LoggerFactory.getLogger(ApiResponseService.class);
    @Value("${response.folder.name}")
    private String responseSourceDirectory;
    @Value("${base.url}")
    private String baseUrl;
    @Value("${server.port}")
    private String serverPort;

    private final String COLON = ":";
    private final String SLASH_API_SLASH = "/api/";

    public List<ApiResponseDao> getSavedResponsesAsDao() {
        List<ApiResponseDao> daoList = new LinkedList<>();
        for (String apiName: getListOfSavedResponses()) {
            String url = baseUrl + COLON + serverPort + SLASH_API_SLASH + apiName;
            daoList.add(new ApiResponseDao(apiName, url, "GET"));
        }
        return daoList;
    }


    public Map<String, Object> getApiResponseByName(String name) {
        if (!fileNameAlreadyExists(name)) {
            throw new BadRequestException("No such data with provided name exists");
        }
        try {
            Path path = getFilePath(name);
            String jsonContent = new String(Files.readAllBytes(path));
            JSONObject object = new JSONObject(jsonContent);
            return object.toMap();
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnhandledErrorException("Error: " + e.getClass().getSimpleName() + " ---> " + e.getMessage());
        }
    }

    public String saveJsonResponse(String name, JSONObject jsonObject) {
        if (fileNameAlreadyExists(name)) {
            throw new BadRequestException("File With Similar Name Already Exists");
        }
        try {
            Path path = getFilePath(name);
            createFileByPath(path);
            writeBytesToFile(path, jsonObject.toString(2).getBytes());
            return "Saved!";
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnhandledErrorException("Error: " + e.getClass().getSimpleName() + " ---> " + e.getMessage());
        }
    }

    public String overwriteFileContentByName(String name, JSONObject jsonObject) {
        try {
            Path path = getFilePath(name);
            writeBytesToFile(path, jsonObject.toString(2).getBytes());
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnhandledErrorException("Error: " + e.getClass().getSimpleName() + " ---> " + e.getMessage());
        }
    }

    public List<String> getListOfSavedResponses() {
        Path path = Paths.get(responseSourceDirectory + "/");
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
            return getFileNamesFromDirectoryStream(stream);
        } catch (IOException e) {
            e.printStackTrace();
            throw new UnhandledErrorException("Error: " + e.getClass().getSimpleName() + " ---> " + e.getMessage());
        }
    }

    public String deleteFileByName(String name) {
        try {
            deleteFile(name);
            return "Deleted!";
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnhandledErrorException("Error: " + e.getClass().getSimpleName() + " ---> " + e.getMessage());
        }
    }

    public String deleteAll() {
        try {
            List<String> listOfAvailableFiles = getListOfSavedResponses();
            for (String fileName : listOfAvailableFiles) {
                deleteFile(fileName);
            }
            return "All Deleted!";
        } catch (Exception e) {
            e.printStackTrace();
            throw new UnhandledErrorException("Error: " + e.getClass().getSimpleName() + " ---> " + e.getMessage());
        }
    }

    private void deleteFile(String name) throws IOException {
        Path path = getFilePath(name);
        Files.delete(path);
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

    private Path getFilePath(String name) {
        return Paths.get(responseSourceDirectory + "/" + name + ".json");
    }

    private void createFileByPath(Path path) throws IOException {
        Files.createFile(path);
    }

    private void writeBytesToFile(Path path, byte[] bytes) throws IOException {
        Files.write(path, bytes);
    }

    private boolean fileNameAlreadyExists(String name) {
        return Files.exists(getFilePath(name));
    }
}
