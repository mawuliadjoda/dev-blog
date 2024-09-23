package com.esprit;

import com.esprit.ennum.StatusEnum;
import com.esprit.entity.User;
import com.esprit.repoditory.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Log4j2
public class UserLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        User user = User.builder()
                .name("test")
                .isVerified(StatusEnum.TRUE)
                .build();

        userRepository.save(user);

        List<User> all = userRepository.findAll();

        Set<StatusEnum> collect = all.stream().map(User::getIsVerified).collect(Collectors.toSet());
        log.info(collect);
    }
}
