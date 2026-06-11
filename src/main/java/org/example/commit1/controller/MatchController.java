package org.example.commit1.controller;

import org.example.commit1.dto.MatchResponseDTO;
import org.example.commit1.model.Match;
import org.example.commit1.service.MatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/match")
public class MatchController {

    private final MatchService matchService;

    @Autowired
    public MatchController(MatchService matchService) {
        this.matchService = matchService;
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinPool(@RequestBody Map<String, Integer> body) {
        int commitId = body.get("commitId");
        Match match = matchService.joinPool(commitId);
        if (match == null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        }
        return new ResponseEntity<>(match, HttpStatus.CREATED);
    }

    @GetMapping("/current")
    public ResponseEntity<MatchResponseDTO> getCurrentMatch() {
        MatchResponseDTO response = matchService.getCurrentMatch();
        if (response == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}