package com.journal.service;

import com.journal.entity.JournalEntry;
import com.journal.entity.User;
import com.journal.repo.JournalEntryRepo;
import com.journal.repo.UserRepo;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JournalEntryService {

    @Autowired
    private JournalEntryRepo journalEntryRepo;

    @Autowired
    private UserRepo userRepo;

    public JournalEntry save(JournalEntry journalEntry, String userName) {
        User user = userRepo.findByUsername(userName);
        journalEntry.setDate(LocalDateTime.now());
        JournalEntry journalEntry1 = journalEntryRepo.save(journalEntry);
        user.getJournalEntries().add(journalEntry1);
        userRepo.save(user);
        return journalEntry1;
    }
    public JournalEntry save(JournalEntry journalEntry) {

        return journalEntryRepo.save(journalEntry);

    }

    public List<JournalEntry> findAll() {
        return journalEntryRepo.findAll();
    }

    public Optional<JournalEntry> findById(ObjectId id) {
        return journalEntryRepo.findById(id);
    }

    public void delete(ObjectId id, String userName) {
        User user = userRepo.findByUsername(userName);
        user.getJournalEntries().remove(journalEntryRepo.findById(id).get());
        journalEntryRepo.deleteById(id);
    }
}
