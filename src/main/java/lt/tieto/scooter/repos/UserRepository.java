package lt.tieto.scooter.repos;

import lt.tieto.scooter.models.UserDto;
import lt.tieto.scooter.repos.mappers.UserMapper;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void createUser(UserDto user) {
        jdbcTemplate.update("INSERT INTO USERS(PHONE_NUMBER, TOKEN) VALUES(?, ?)",
                user.phoneNumber, user.token);
    }

    public UserDto getUserByPhoneNumber(String phoneNumber) {
        return jdbcTemplate.queryForObject("SELECT * FROM USERS WHERE PHONE_NUMBER = ?",
                new UserMapper(), phoneNumber);
    }
}
