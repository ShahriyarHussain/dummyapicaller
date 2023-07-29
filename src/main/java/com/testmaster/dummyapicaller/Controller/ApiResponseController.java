package com.testmaster.dummyapicaller.Controller;

import com.testmaster.dummyapicaller.Exception.BadRequestException;
import com.testmaster.dummyapicaller.Exception.UnhandledErrorException;
import com.testmaster.dummyapicaller.Service.ApiResponseService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiResponseController {

//    private final Logger LOGGER = LoggerFactory.getLogger(ApiResponseService.class);
    private final ApiResponseService apiResponseService;

    //For api clients only (i.e. Postman)
    @PostMapping(value = "/get-api-response")
    public Map<String, Object> getApiResponse(@RequestBody Map<String, Object> objectMap) {
        return objectMap;
    }

    @PostMapping("/save-api-response/{dataName}")
    public ResponseEntity<String> saveApiResponse(@PathVariable String dataName,
                                                               @RequestBody Map<String, Object> objectMap) {
        JSONObject jsonObject = new JSONObject(objectMap);
        try {
            return ResponseEntity.ok(apiResponseService.saveJsonResponse(dataName, jsonObject));
        } catch (BadRequestException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect Request: " + e.getMessage());
        } catch (UnhandledErrorException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Incorrect Request: " + e.getMessage());
        }
    }

    @GetMapping("/{dataName}")
    public ResponseEntity<Map<String, Object>> getApiResponseByName(@PathVariable String dataName) {
        try {
            return ResponseEntity.ok(apiResponseService.getApiResponseByName(dataName));
        } catch (BadRequestException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect Request: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected Error Occurred " + e.getMessage());
        }
    }

    @GetMapping("/show-saved-api-list")
    public ResponseEntity<List<String>> showSavedApiList() {
        try {
            return ResponseEntity.ok(apiResponseService.getListOfSavedResponses());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected Error Occurred " + e.getMessage());
        }
    }

    @PostMapping("/replace-saved-response/{dataName}")
    public ResponseEntity<String> overwriteResponse(@PathVariable String dataName,
                                                    @RequestBody Map<String, Object> objectMap) {
        JSONObject jsonObject = new JSONObject(objectMap);
        try {
            return ResponseEntity.ok(apiResponseService.overwriteFileContentByName(dataName, jsonObject));
        } catch (BadRequestException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect Request: " + e.getMessage());
        } catch (UnhandledErrorException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Incorrect Request: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-response/{dataName}")
    public ResponseEntity<String> deleteResponseByName(@PathVariable String dataName) {
        try {
            return ResponseEntity.ok(apiResponseService.deleteFileByName(dataName));
        } catch (BadRequestException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect Request: " + e.getMessage());
        } catch (UnhandledErrorException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Incorrect Request: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete-all-responses")
    public ResponseEntity<String> AllResponses() {
        try {
            return ResponseEntity.ok(apiResponseService.deleteAll());
        } catch (BadRequestException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect Request: " + e.getMessage());
        } catch (UnhandledErrorException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Incorrect Request: " + e.getMessage());
        }
    }
}

