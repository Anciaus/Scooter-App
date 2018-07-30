package com.tieto.scooter.dataAccess;

import com.tieto.scooter.models.UserDto;
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
}
