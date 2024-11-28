package pbl.GNUB.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import pbl.GNUB.dto.DataDto;
import pbl.GNUB.service.DataService;

@RestController
@RequestMapping("/api/data")
public class DataController {

    private final DataService dataService;

    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    // DataDto를 받아서 데이터 저장
    @PostMapping
    public ResponseEntity<String> saveUserInput(@RequestBody DataDto dataDto) {
        System.out.println("데이터: " + dataDto);  // 확인용

        // saveData 메서드를 호출해 데이터 저장 처리
        dataService.saveUserInput(dataDto); 

        return ResponseEntity.ok("데이터 저장 완료");
    }
}
