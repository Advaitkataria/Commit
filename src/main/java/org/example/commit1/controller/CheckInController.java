package org.example.commit1.controller;

import org.example.commit1.model.CheckIn;
import org.example.commit1.service.CheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/checkins")
public class CheckInController {

    private final CheckInService checkInService;

    @Autowired
    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @PostMapping
    public ResponseEntity<CheckIn> checkIn(@RequestBody Map<String, Object> body) {
        int matchId = (int) body.get("matchId");
        String note = (String) body.getOrDefault("note", null);
        return new ResponseEntity<>(checkInService.checkIn(matchId, note), HttpStatus.CREATED);
    }

    @GetMapping("/today")
    public ResponseEntity<Map<String, Boolean>> checkedInToday() {
        return new ResponseEntity<>(checkInService.checkedInToday(), HttpStatus.OK);
    }

    @GetMapping("/match/{matchId}")
    public ResponseEntity<List<CheckIn>> getByMatch(@PathVariable int matchId) {
        return new ResponseEntity<>(checkInService.getByMatch(matchId), HttpStatus.OK);
    }
}