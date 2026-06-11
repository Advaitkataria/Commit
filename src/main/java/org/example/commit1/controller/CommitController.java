package org.example.commit1.controller;

import jakarta.validation.Valid;
import org.example.commit1.model.Commit;
import org.example.commit1.repository.CommitRepository;
import org.example.commit1.service.CommitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/commit")
public class CommitController {

    private final CommitService commitService;

    @Autowired
    public CommitController(CommitService commitService){
        this.commitService=commitService;
    }
    @GetMapping
    public ResponseEntity<List<Commit>> getAllCommit(@RequestParam(required = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){
        return new ResponseEntity<>(commitService.getAllCommit(date), HttpStatus.OK);
    }
    @GetMapping("/history")
    public ResponseEntity<List<Commit>> getMyHistory() {
        return new ResponseEntity<>(commitService.getMyHistory(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Commit> addCommit(@Valid @RequestBody Commit commit){
        return new ResponseEntity<>(commitService.addCommit(commit),HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or @commitService.isOwner(#id)")
    public ResponseEntity<Void> deleteCommit(@PathVariable int id){
        commitService.deleteCommit(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Commit> updateCommit(@PathVariable int id, @Valid@RequestBody Commit updatedCommit){
        return new ResponseEntity<>(commitService.updateCommit(id,updatedCommit),HttpStatus.OK);
    }

}
