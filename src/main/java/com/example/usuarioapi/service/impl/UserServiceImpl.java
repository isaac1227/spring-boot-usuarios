package com.example.usuarioapi.service.impl;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import java.util.Objects;
import com.example.usuarioapi.dto.UserRequest;
import com.example.usuarioapi.dto.UserResponse;
import com.example.usuarioapi.model.User;
import com.example.usuarioapi.repository.UserRepository;
import com.example.usuarioapi.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.usuarioapi.exception.DuplicateResourceException;
import com.example.usuarioapi.exception.ResourceNotFoundException;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public UserResponse create(@NonNull UserRequest req) {
        userRepository.findByEmail(req.getEmail()).ifPresent(u -> {
            throw new DuplicateResourceException("Email already in use: " + req.getEmail());
        });
        User saved = Objects.requireNonNull(userRepository.save(toEntity(req)));
        return toResponse(Objects.requireNonNull(saved));
    }

    @Override
    public UserResponse getById(@NonNull Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return toResponse(Objects.requireNonNull(user));
    }

    @Override
    public Page<UserResponse> findAll(String q, @NonNull Pageable pageable) {
        Page<User> page;
        if (q == null || q.isBlank()) {
            page = userRepository.findAll(pageable);
        } else {
            page = userRepository.findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(q, q, pageable);
        }
        return page.map(this::toResponse);
    }

    @Override
    public UserResponse update(@NonNull Long id, @NonNull UserRequest req) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        user.setUsername(req.getUsername());
        user.setEmail(req.getEmail());
        if (req.getActive() != null) {
            user.setActive(req.getActive());
        }
        User updated = Objects.requireNonNull(userRepository.save(user));
        return toResponse(Objects.requireNonNull(updated));
    }

    @Override
    public void delete(@NonNull Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        userRepository.delete(Objects.requireNonNull(user));
    }

    public User toEntity(@NonNull UserRequest req) {
        return User.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .active(req.getActive() != null ? req.getActive() : true)
                .build();
    }

    public UserResponse toResponse(@NonNull User user) {
        return UserResponse.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .active(user.getActive())
                .createdAt(user.getCreatedAt())
                .build();
    }

}
