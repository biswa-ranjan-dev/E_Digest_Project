package com.journal.controller;

import com.journal.entity.JournalEntry;
import com.journal.entity.User;
import com.journal.service.JournalEntryService;
import com.journal.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @PostMapping()
    public ResponseEntity<?> saveJournalEntry(@RequestBody JournalEntry journalEntry) {
        try{
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            journalEntryService.save(journalEntry,username);
            return ResponseEntity.status(HttpStatus.CREATED).body(journalEntry);
        }
        catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }
    @GetMapping()
    public ResponseEntity<?> getAllJournalEntriesOfUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        User user = userService.findUserByUserName(username);
        List<JournalEntry> journalEntries = user.getJournalEntries();
        if (!journalEntries.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK).body(journalEntries);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findJournalEntryById(@PathVariable ObjectId id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userService.findUserByUserName(username);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).toList();

        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
            if (journalEntry.isPresent()) {
                return ResponseEntity.status(HttpStatus.OK).body(journalEntry.get());
            }
        }


        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

//    @GetMapping
//    public ResponseEntity<?> findAllJournalEntries() {
//        List<JournalEntry> journalEntries = journalEntryService.findAll();
//        if (journalEntries.isEmpty()) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//        }
//        return ResponseEntity.status(HttpStatus.OK).body(journalEntries);
//    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJournalEntryById(@PathVariable ObjectId id) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        journalEntryService.delete(id,username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }




    @PutMapping("/{id}")
    public ResponseEntity<?> updateJournalEntry(@PathVariable ObjectId id, @RequestBody JournalEntry newEntry) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        User user = userService.findUserByUserName(username);
        List<JournalEntry> collect = user.getJournalEntries().stream().filter(x -> x.getId().equals(id)).toList();

        if(!collect.isEmpty()) {
            Optional<JournalEntry> journalEntry = journalEntryService.findById(id);
            if (journalEntry.isPresent()) {
                JournalEntry oldEntry = journalEntry.get();
                oldEntry.setTitle(newEntry.getTitle() != null && !newEntry.getTitle().isEmpty() ? newEntry.getTitle() : oldEntry.getTitle());
                oldEntry.setContent(newEntry.getContent() != null && !newEntry.getContent().isEmpty() ? newEntry.getContent() : oldEntry.getContent());
                journalEntryService.save(oldEntry);
                return ResponseEntity.status(HttpStatus.OK).body(oldEntry);
            }
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }











}
