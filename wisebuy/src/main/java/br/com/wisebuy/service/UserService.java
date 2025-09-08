package br.com.wisebuy.service;

import java.util.Collections;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.wisebuy.dto.SignInDTO;
import br.com.wisebuy.dto.SignUpDTO;
import br.com.wisebuy.entity.Role;
import br.com.wisebuy.entity.User;
import br.com.wisebuy.repository.RoleRepository;
import br.com.wisebuy.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private static final String DEFAULT_ROLE = "USER";

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public ResponseEntity<String> register(SignUpDTO dto) {

        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail já cadastrado.");
        }

        User user = new User();
        user.setName(dto.getName());
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        Role role = roleRepository.findByRole(DEFAULT_ROLE)
                .orElseThrow(() -> new IllegalStateException("Função '" + DEFAULT_ROLE + "' não encontrada."));
        user.setRoles(Collections.singletonList(role));

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED).body("Usuário registrado com sucesso.");
    }

    public ResponseEntity<String> login(SignInDTO dto) {

        Optional<User> optionalUser = userRepository.findByEmail(dto.getEmail());

        if (!optionalUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("E-mail não cadastrado.");
        }

        try {

            User user = optionalUser.get();

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

            if (authentication.isAuthenticated()) {
                String token = jwtService.generateToken(user.getId(), user.getEmail());
                return ResponseEntity.status(HttpStatus.OK).body(token);
            }

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos.");

        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("E-mail ou senha inválidos.");
        }
    }

}
