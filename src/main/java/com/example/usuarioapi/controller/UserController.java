package com.example.usuarioapi.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.PageRequest;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.usuarioapi.service.UserService;
import com.example.usuarioapi.dto.UserRequest;
import com.example.usuarioapi.dto.UserResponse;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.Objects;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
        private final UserService userService;

        @PostMapping
        public ResponseEntity<UserResponse> create(@Valid @RequestBody UserRequest req) {
                Objects.requireNonNull(req, "request body must not be null");
                UserResponse created = userService.create(req);
                URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                                .path("/{id}")
                                .buildAndExpand(created.getId())
                                .toUri();
                return ResponseEntity.created(uri).body(created);
        }

        @GetMapping("/{id}")
        public ResponseEntity<UserResponse> getById(@PathVariable Long id) {
                Objects.requireNonNull(id, "id must not be null");
                UserResponse user = userService.getById(id);
                return ResponseEntity.ok(user);
        }

        @GetMapping
        public ResponseEntity<Page<UserResponse>> findAll(
                        @RequestParam(required = false) String q,
                        @org.springframework.data.web.PageableDefault(size = 20) Pageable pageable) {
                Objects.requireNonNull(pageable, "pageable must not be null");

                // Sanitizar las órdenes de ordenación recibidas. Algunos frontends pueden
                // enviar valores no válidos (p. ej. "string"). Evitamos pasarlos a JPA
                // y en su lugar aplicamos una whitelist de propiedades permitidas.
                Set<String> allowed = Set.of("id", "username", "email", "createdAt", "active");
                List<Sort.Order> validOrders = new ArrayList<>();
                for (Sort.Order o : pageable.getSort()) {
                        String prop = o.getProperty();
                        if (prop != null && allowed.contains(prop)) {
                                // conservar dirección
                                validOrders.add(o);
                        }
                }

                Pageable effective;
                if (!validOrders.isEmpty()) {
                        effective = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                                        Sort.by(validOrders));
                } else {
                        // si no hay órdenes válidas, aplicar orden por defecto del servidor
                        effective = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(),
                                        Sort.by(Sort.Order.desc("createdAt")));
                }

                Page<UserResponse> page = userService.findAll(q, effective);
                return ResponseEntity.ok(page);
        }

        @DeleteMapping("/{id}")
        public ResponseEntity<Void> delete(@PathVariable Long id) {
                Objects.requireNonNull(id, "id must not be null");
                userService.delete(id);
                return ResponseEntity.noContent().build();
        }

        @PutMapping("/{id}")
        public ResponseEntity<UserResponse> update(@PathVariable Long id, @Valid @RequestBody UserRequest req) {
                Objects.requireNonNull(id, "id must not be null");
                Objects.requireNonNull(req, "req must not be null");
                UserResponse updated = userService.update(id, req);
                return ResponseEntity.ok(updated);
        }
}
