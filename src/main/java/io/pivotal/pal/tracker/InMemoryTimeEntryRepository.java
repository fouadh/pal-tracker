package io.pivotal.pal.tracker;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {
  private AtomicInteger sequence = new AtomicInteger(0);
  private Map<Long, TimeEntry> store = new HashMap<>();

  public TimeEntry create(TimeEntry timeEntry) {
    long id = sequence.incrementAndGet();
    TimeEntry newEntry = new TimeEntry(id, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
    store.put(id, newEntry);
    return newEntry;
  }

  @Override
  public TimeEntry find(long id) {
    return store.get(id);
  }

  @Override
  public List<TimeEntry> list() {
    return Collections.unmodifiableList(new ArrayList<>(store.values()));
  }

  @Override
  public TimeEntry update(long id, TimeEntry timeEntry) {
    TimeEntry updatedEntry = new TimeEntry(id, timeEntry.getProjectId(), timeEntry.getUserId(), timeEntry.getDate(), timeEntry.getHours());
    store.put(id, updatedEntry);
    return updatedEntry;
  }

  @Override
  public void delete(long id) {
    store.remove(id);
  }
}
