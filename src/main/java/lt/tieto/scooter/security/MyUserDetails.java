package lt.tieto.scooter.security;

import lt.tieto.scooter.dtos.UserDto;
import lt.tieto.scooter.repos.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetails implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public MyUserDetails(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        final UserDto user = userRepository.getUserByPhoneNumber(phoneNumber);

        if (user == null) {
            throw new UsernameNotFoundException("User with phone number '" + phoneNumber + "' not found");
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(phoneNumber)
                .password(phoneNumber)
                .authorities(Role.ROLE_CLIENT)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }

}
