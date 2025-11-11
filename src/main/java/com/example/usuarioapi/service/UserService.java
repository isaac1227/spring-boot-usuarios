package com.example.usuarioapi.service;

import com.example.usuarioapi.dto.UserRequest;
import com.example.usuarioapi.dto.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.lang.NonNull;

public interface UserService {
    UserResponse create(@NonNull UserRequest req);

    UserResponse getById(@NonNull Long id);

    Page<UserResponse> findAll(String q, @NonNull Pageable pageable);

    UserResponse update(@NonNull Long id, @NonNull UserRequest req);

    void delete(@NonNull Long id);
}
