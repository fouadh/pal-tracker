package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
  private TimeEntryRepository timeEntryRepository;

  public TimeEntryController(TimeEntryRepository timeEntryRepository) {
    this.timeEntryRepository = timeEntryRepository;
  }

  @PostMapping
  public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry entry) {
    TimeEntry saved = timeEntryRepository.create(entry);
    return new ResponseEntity<>(saved, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TimeEntry> read(@PathVariable long id) {
    TimeEntry timeEntry = timeEntryRepository.find(id);
    if (timeEntry != null)
      return new ResponseEntity<>(timeEntry, HttpStatus.OK);
    else
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping
  public ResponseEntity<List<TimeEntry>> list() {
    List<TimeEntry> list = timeEntryRepository.list();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TimeEntry> update(@PathVariable long id, @RequestBody TimeEntry entry) {
    TimeEntry saved = timeEntryRepository.update(id, entry);
    if (saved != null)
      return new ResponseEntity<>(saved, HttpStatus.OK);
    else
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity delete(@PathVariable long id) {
    timeEntryRepository.delete(id);
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }
}
