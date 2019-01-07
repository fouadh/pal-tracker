package io.pivotal.pal.tracker;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {
  private final TimeEntryRepository timeEntryRepository;
  private final Counter actionCounter;
  private final DistributionSummary distributionSummary;

  public TimeEntryController(final TimeEntryRepository timeEntryRepository, final MeterRegistry meterRegistry) {
    this.timeEntryRepository = timeEntryRepository;
    this.actionCounter = meterRegistry.counter("timeEntry.actionCounter");
    this.distributionSummary = meterRegistry.summary("timeEntry.summary");
  }

  @PostMapping
  public ResponseEntity<TimeEntry> create(@RequestBody TimeEntry entry) {
    TimeEntry saved = timeEntryRepository.create(entry);
    actionCounter.increment();
    distributionSummary.record(timeEntryRepository.list().size());
    return new ResponseEntity<>(saved, HttpStatus.CREATED);
  }

  @GetMapping("/{id}")
  public ResponseEntity<TimeEntry> read(@PathVariable long id) {
    TimeEntry timeEntry = timeEntryRepository.find(id);
    if (timeEntry != null) {
      actionCounter.increment();
      return new ResponseEntity<>(timeEntry, HttpStatus.OK);
    }
    else
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @GetMapping
  public ResponseEntity<List<TimeEntry>> list() {
    List<TimeEntry> list = timeEntryRepository.list();
    actionCounter.increment();
    return new ResponseEntity<>(list, HttpStatus.OK);
  }

  @PutMapping("/{id}")
  public ResponseEntity<TimeEntry> update(@PathVariable long id, @RequestBody TimeEntry entry) {
    TimeEntry saved = timeEntryRepository.update(id, entry);
    if (saved != null) {
      actionCounter.increment();
      return new ResponseEntity<>(saved, HttpStatus.OK);
    }
    else
      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity delete(@PathVariable long id) {
    timeEntryRepository.delete(id);
    actionCounter.increment();
    distributionSummary.record(timeEntryRepository.list().size());
    return new ResponseEntity(HttpStatus.NO_CONTENT);
  }
}
