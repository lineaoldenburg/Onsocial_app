package se.jensen.linea.onsocial_app.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import se.jensen.linea.onsocial_app.DTO.UserRequestDTO;
import se.jensen.linea.onsocial_app.DTO.UserResponseDTO;
import se.jensen.linea.onsocial_app.mapper.UserMapper;
import se.jensen.linea.onsocial_app.model.User;
import se.jensen.linea.onsocial_app.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * UserService innehåller alla metoder vi behöver anropa i UserController.
 * Den innehåller alltså affärslogiken mellan UserControllern och databasen.
 * Klassen annoteras med Service, vilket gör att Spring skapar en Singleton som kan injiceras i andra klasser.
 * <p>
 * Transactional gör att metoderna körs inom en databastransaktion.
 * Antingen lyckas alla metoder med annotering eller ingen av dem.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-22
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    /**
     * Skapa en ny användare.
     *
     * @param userRequestDTO Vi hämtar data från klienten via UserRequestDTO.
     * @return En UserResponsDTO med information om användaren.
     */
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

        return userMapper.userToDTO(savedUser);
    }

    /**
     * Hämta alla användare.
     *
     * @return Lista av UserResponseDTO.
     */
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

    /**
     * Hämta användare med ett specifikt ID.
     *
     * @param id användarens unika ID.
     * @return UserResponseDTO med information om en användare.
     * @throws RuntimeException om användaren inte hittas.
     */
    public UserResponseDTO getUserByIdOrThrow(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        return userMapper.userToDTO(user);
    }

    /**
     * Uppdatera en befintlig användare.
     *
     * @param id             Användarens unika ID.
     * @param userRequestDTO Vi hämtar den nya informationen från klienten via UserRequestDTO.
     * @return En UserResponseDTO med de uppdaterade uppgifterna.
     */
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

        return userMapper.userToDTO(updatedUser);
    }

    /**
     * Radera en användare.
     *
     * @param id Användarens unika ID.
     * @return en boolean.
     */
    // Delete user
    public boolean deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}