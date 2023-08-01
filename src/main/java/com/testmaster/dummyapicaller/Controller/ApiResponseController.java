package com.testmaster.dummyapicaller.Controller;

import com.testmaster.dummyapicaller.Enum.RequestTypes;
import com.testmaster.dummyapicaller.Service.ApiResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ApiResponseController {

    private final ApiResponseService apiResponseService;


    //For api clients only (i.e. Postman)
    @GetMapping("/{dataName}")
    public ResponseEntity<Map<String, Object>> getApiResponseByName(@PathVariable String dataName) {
        return ResponseEntity.ok(apiResponseService.getApiResponseByName(dataName, RequestTypes.GET));
    }

    @PostMapping("/{dataName}")
    public ResponseEntity<Map<String, Object>> getApiResponseByNameAndBody(@PathVariable String dataName) {
        return ResponseEntity.ok(apiResponseService.getApiResponseByName(dataName, RequestTypes.POST));
    }

    @PutMapping("/{dataName}")
    public ResponseEntity<Map<String, Object>> getApiPutResponseByName(@PathVariable String dataName) {
        return ResponseEntity.ok(apiResponseService.getApiResponseByName(dataName, RequestTypes.PUT));
    }

    @DeleteMapping("/{dataName}")
    public ResponseEntity<Map<String, Object>> getApiDeleteResponseByName(@PathVariable String dataName) {
        return ResponseEntity.ok(apiResponseService.getApiResponseByName(dataName, RequestTypes.DELETE));
    }
}

