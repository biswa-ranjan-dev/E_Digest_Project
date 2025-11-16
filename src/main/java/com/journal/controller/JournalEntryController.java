package com.journal.controller;

import com.journal.entity.JournalEntry;
import com.journal.entity.User;
import com.journal.service.JournalEntryService;
import com.journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @PostMapping("{userName}")
    public ResponseEntity<?> saveJournalEntry(@RequestBody JournalEntry journalEntry, @PathVariable String userName) {
        try{

            journalEntryService.save(journalEntry,userName);
            return ResponseEntity.status(HttpStatus.CREATED).body(journalEntry);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
    @GetMapping("{userName}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String userName){
        User user = userService.findUserByUserName(userName);
        List<JournalEntry> journalEntries = user.getJournalEntries();
        if (!journalEntries.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(journalEntries);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findJournalEntryById(@PathVariable ObjectId id) {
        Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
        if (journalEntry.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(journalEntry.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping
    public ResponseEntity<?> findAllJournalEntries() {
        List<JournalEntry> journalEntries = journalEntryService.findAll();
        if (journalEntries.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(journalEntries);
    }

    @DeleteMapping("/{id}/{userName}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId id, @PathVariable String userName) {
        journalEntryService.delete(id,userName);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/{userName}")
    public ResponseEntity<?> updateJournalEntry(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry, @PathVariable String userName) {
        JournalEntry oldEntry = journalEntryService.findById(id).orElse(null);
        if (oldEntry != null) {
            oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : oldEntry.getTitle());
            oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : oldEntry.getContent());
            journalEntryService.save(oldEntry);
            return ResponseEntity.status(HttpStatus.OK).body(oldEntry);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }











}
