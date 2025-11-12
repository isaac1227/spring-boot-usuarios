package com.example.usuarioapi.repository;

import com.example.usuarioapi.model.User;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://localhost:5432/usuario_db",
        "spring.datasource.username=usuario",
        "spring.datasource.password=password"
})
class UserRepositoryIT {

    @Autowired
    UserRepository userRepository;

    @Test
    void saveAndFind_shouldWork() {
        User u = User.builder().username("intuser").email("int@example.com").active(true).build();
        User saved = userRepository.save(Objects.requireNonNull(u));
        Optional<User> found = userRepository.findById(Objects.requireNonNull((saved).getId()));
        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("int@example.com");
    }
}
