package ru.yandex.practicum.filmorate.storage.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.storage.MPAStorage;

import java.util.List;

@Component
public class InDBMPAStorage implements MPAStorage {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public InDBMPAStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public MPA getMpa(int id) {
        String sqlQuery = "SELECT id, " +
                "                 name " +
                "            FROM Mpa " +
                "           WHERE id=?";

        List<MPA> MPAs = jdbcTemplate.query(sqlQuery, (rs, rowNum) -> new MPA(rs.getInt("id"), rs.getString("name")), id);
        if (MPAs.isEmpty()) {
            return null;
        }
        return MPAs.get(0);
    }

    @Override
    public List<MPA> getAllMpa() {
        String sqlQuery = "SELECT id, " +
                "                name " +
                "           FROM Mpa";

        return jdbcTemplate.query(sqlQuery,
                (rs, rowNum) -> new MPA(rs.getInt("id"), rs.getString("name")));
    }


}
