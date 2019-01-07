package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TimeEntryHealthIndicator implements HealthIndicator {
  private final static int MAX_ENTRIES = 5;
  private TimeEntryRepository repository;

  public TimeEntryHealthIndicator(TimeEntryRepository repository) {
    this.repository = repository;
  }

  @Override
  public Health health() {
    List<TimeEntry> list = repository.list();
    if (list.size() < MAX_ENTRIES)
      return Health.up().build();
    else
      return Health.down().build();
  }
}
