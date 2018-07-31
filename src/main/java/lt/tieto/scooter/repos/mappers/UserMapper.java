package lt.tieto.scooter.repos.mappers;

import lt.tieto.scooter.dtos.UserDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserMapper implements RowMapper<UserDto> {

    @Override
    public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDto user = new UserDto();
        user.phoneNumber = rs.getString("phone_number");
        user.token = rs.getString("token");

        return user;
    }
}
