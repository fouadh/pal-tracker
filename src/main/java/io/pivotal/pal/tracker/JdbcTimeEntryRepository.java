package io.pivotal.pal.tracker;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.sql.Statement.RETURN_GENERATED_KEYS;

public class JdbcTimeEntryRepository implements TimeEntryRepository {
  private JdbcTemplate jdbcTemplate;

  public JdbcTimeEntryRepository(DataSource dataSource) {
    this.jdbcTemplate = new JdbcTemplate(dataSource);
  }

  @Override
  public TimeEntry create(final TimeEntry entry) {
    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();
    jdbcTemplate.update(con -> {
      PreparedStatement ps = con.prepareStatement("insert into time_entries(project_id, user_id, date, hours) values (?, ?, ?, ?) ", RETURN_GENERATED_KEYS);
      ps.setLong(1, entry.getProjectId());
      ps.setLong(2, entry.getUserId());
      ps.setDate(3, Date.valueOf(entry.getDate()));
      ps.setInt(4, entry.getHours());
      return ps;
    }, keyHolder);

    return find(keyHolder.getKey().longValue());
  }

  @Override
  public TimeEntry find(long id) {
    List<TimeEntry> entries = jdbcTemplate.query("select id, project_id, user_id, date, hours from time_entries where id=?",
        new Object[]{id},
        (rs, rowNum) -> mapRowToTimeEntry(rs));
    if (entries.isEmpty())
      return null;
    return entries.get(0);
  }

  @Override
  public List<TimeEntry> list() {
    return jdbcTemplate.query("select id, project_id, user_id, date, hours from time_entries", (rs, rowNum) -> mapRowToTimeEntry(rs));
  }

  private TimeEntry mapRowToTimeEntry(ResultSet rs) throws SQLException {
    return new TimeEntry(rs.getLong(1), rs.getLong(2), rs.getLong(3), rs.getDate(4).toLocalDate(), rs.getInt(5));
  }

  @Override
  public TimeEntry update(long id, TimeEntry timeEntry) {
    jdbcTemplate.update("update time_entries set project_id=?, user_id=?, date=?, hours=? where id=?", timeEntry.getProjectId(), timeEntry.getUserId(), Date.valueOf(timeEntry.getDate()), timeEntry.getHours(), id);
    return find(id);
  }

  @Override
  public void delete(long id) {
    jdbcTemplate.update("delete from time_entries where id=?", id);
  }
}
