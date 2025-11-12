package com.example.usuarioapi.service;

import com.example.usuarioapi.dto.UserRequest;
import com.example.usuarioapi.dto.UserResponse;
import com.example.usuarioapi.exception.DuplicateResourceException;
import com.example.usuarioapi.model.User;
import com.example.usuarioapi.repository.UserRepository;
import com.example.usuarioapi.service.impl.UserServiceImpl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplUnitTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    void create_shouldSaveAndReturnResponse() {
        UserRequest req = new UserRequest();
        req.setUsername("u1");
        req.setEmail("u1@example.com");

        User saved = User.builder()
                .id(10L)
                .username(req.getUsername())
                .email(req.getEmail())
                .active(true)
                .createdAt(OffsetDateTime.now())
                .build();

        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.empty());
        doReturn(saved).when(userRepository).save(any(User.class));

        UserResponse resp = userService.create(req);

        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(10L);
        assertThat(resp.getUsername()).isEqualTo("u1");
        assertThat(resp.getEmail()).isEqualTo("u1@example.com");
    }

    @Test
    void create_shouldThrowDuplicate_whenEmailExists() {
        UserRequest req = new UserRequest();
        req.setUsername("u1");
        req.setEmail("u1@example.com");

        User existing = User.builder().id(2L).username("other").email(req.getEmail()).build();
        when(userRepository.findByEmail(req.getEmail())).thenReturn(Optional.of(existing));

        assertThrows(DuplicateResourceException.class, () -> userService.create(req));
    }

    @Test
    void findAll_withQuery_shouldReturnMappedPage() {
        User u = User.builder().id(1L).username("alice").email("a@e.com").active(true).build();
        Page<User> page = new PageImpl<>(Objects.requireNonNull(List.of(u)));
        when(userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(anyString(), anyString(),
                any(org.springframework.data.domain.Pageable.class)))
                .thenReturn(page);

        var resp = userService.findAll("ali", PageRequest.of(0, 10));
        assertThat(resp.getTotalElements()).isEqualTo(1);
        assertThat(resp.getContent().get(0).getUsername()).isEqualTo("alice");
    }
}
