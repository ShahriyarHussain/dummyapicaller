package com.caller.dummyapicaller.Controller;

import com.caller.dummyapicaller.Exception.BadRequestException;
import com.caller.dummyapicaller.Exception.UnhandledErrorException;
import com.caller.dummyapicaller.Service.ApiResponseService;
import lombok.RequiredArgsConstructor;
import org.atmosphere.config.service.Post;
import org.json.JSONArray;
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

    private final ApiResponseService apiResponseService;

    @PostMapping(value = "/getInstantResponse")
    public Map<String, Object> getInstantApiResponse(@RequestBody Map<String, Object> objectMap) {
        return objectMap;
    }

    @PostMapping("/save_response/{dataName}")
    public ResponseEntity<String> getAndSaveInstantApiResponse(@PathVariable String dataName,
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

    @GetMapping("/get_response_by_name/{dataName}")
    public ResponseEntity<Map<String, Object>> getApiResponseByName(@PathVariable String dataName) {
        try {
            return ResponseEntity.ok(apiResponseService.getApiResponseByName(dataName));
        } catch (BadRequestException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect Request: " + e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected Error Occurred " + e.getMessage());
        }
    }

    @GetMapping("/show_saved_api_list")
    public ResponseEntity<List<String>> showSavedApiList() {
        try {
            return ResponseEntity.ok(apiResponseService.getListOfSavedResponses());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected Error Occurred " + e.getMessage());
        }
    }

    @PostMapping("/replace_with_new_response/{dataName}")
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

    @DeleteMapping("/delete_response/{dataName}")
    public ResponseEntity<String> deleteResponseByName(@PathVariable String dataName) {
        try {
            return ResponseEntity.ok(apiResponseService.deleteFileByName(dataName));
        } catch (BadRequestException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Incorrect Request: " + e.getMessage());
        } catch (UnhandledErrorException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Incorrect Request: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete_all_responses")
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
