package se.jensen.linea.onsocial_app.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.jensen.linea.onsocial_app.DTO.UserRequestDTO;
import se.jensen.linea.onsocial_app.DTO.UserResponseDTO;
import se.jensen.linea.onsocial_app.model.User;
import se.jensen.linea.onsocial_app.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Create new user
    @Transactional
    public UserResponseDTO createUser(UserRequestDTO userRequestDTO) {
        // Check if alias already exists (boolean)
        if (userRepository.findByAlias(userRequestDTO.alias()).isPresent()) {
            throw new RuntimeException("Alias already exists");
        }

        // Check if email already exists (boolean)
        if (userRepository.findByEmail(userRequestDTO.email()).isPresent()) {
            throw new RuntimeException("E-mail already exists");
        }

        User user = new User();
        user.setAlias(userRequestDTO.alias());
        user.setEmail(userRequestDTO.email());
        user.setFirstName(userRequestDTO.firstName());
        user.setLastName(userRequestDTO.lastName());
        user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
        user.setProfilePicture(userRequestDTO.profilePicture());

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getAlias(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getProfilePicture(),
                savedUser.getRole()
        );
    }

    // Get all users
    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        List<UserResponseDTO> userDTOs = new ArrayList<>();

        for (User user : users) {
            UserResponseDTO dto = new UserResponseDTO(
                    user.getId(),
                    user.getAlias(),
                    user.getEmail(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getProfilePicture(),
                    user.getRole()
            );
            userDTOs.add(dto);
        }

        return userDTOs;
    }

    // Get one user by ID
    public UserResponseDTO getUserByIdOrThrow(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return new UserResponseDTO(
                user.getId(),
                user.getAlias(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfilePicture(),
                user.getRole()
        );
    }

    // Update user
    @Transactional
    public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Check if new alias is already taken by another user (boolean)
        Optional<User> existingUserWithAlias = userRepository.findByAlias(userRequestDTO.alias());
        if (existingUserWithAlias.isPresent() && !existingUserWithAlias.get().getId().equals(id)) {
            throw new RuntimeException("Alias already exists");
        }

        // Check if new email is already taken by another user (boolean)
        Optional<User> existingUserWithEmail = userRepository.findByEmail(userRequestDTO.email());
        if (existingUserWithEmail.isPresent() && !existingUserWithEmail.get().getId().equals(id)) {
            throw new RuntimeException("E-mail already exists");
        }

        user.setAlias(userRequestDTO.alias());
        user.setEmail(userRequestDTO.email());
        user.setFirstName(userRequestDTO.firstName());
        user.setLastName(userRequestDTO.lastName());

        // Only update password if provided
        if (userRequestDTO.password() != null && !userRequestDTO.password().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userRequestDTO.password()));
        }

        user.setProfilePicture(userRequestDTO.profilePicture());

        User updatedUser = userRepository.save(user);

        return new UserResponseDTO(
                updatedUser.getId(),
                updatedUser.getAlias(),
                updatedUser.getEmail(),
                updatedUser.getFirstName(),
                updatedUser.getLastName(),
                updatedUser.getProfilePicture(),
                updatedUser.getRole()
        );
    }

    // Delete user
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}