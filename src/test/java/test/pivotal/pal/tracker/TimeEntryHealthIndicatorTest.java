package test.pivotal.pal.tracker;

import io.pivotal.pal.tracker.TimeEntry;
import io.pivotal.pal.tracker.TimeEntryHealthIndicator;
import io.pivotal.pal.tracker.TimeEntryRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.health.Health;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TimeEntryHealthIndicatorTest {
  @Mock
  private TimeEntryRepository repository;

  @Test
  public void indicatorShouldReturnUpWhenLessThen5TimeEntries() {
    when(repository.list()).thenReturn(Arrays.asList(new TimeEntry()));
    TimeEntryHealthIndicator indicator = new TimeEntryHealthIndicator(repository);
    Health health = indicator.health();
    assertThat(health).isNotNull();
    assertThat(health).isEqualTo(Health.up().build());
    verify(repository).list();
  }

  @Test
  public void indicatorShouldReturnDownWhenNumberOfTimeEntriesIs5() {
    when(repository.list()).thenReturn(Arrays.asList(new TimeEntry(), new TimeEntry(), new TimeEntry(), new TimeEntry(), new TimeEntry()));
    TimeEntryHealthIndicator indicator = new TimeEntryHealthIndicator(repository);
    Health health = indicator.health();
    assertThat(health).isNotNull();
    assertThat(health).isEqualTo(Health.down().build());
    verify(repository).list();
  }
}
