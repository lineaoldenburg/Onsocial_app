package se.jensen.linea.onsocial_app.DTO;

/**
 * Hjälpklass som bygger upp ett UserResponseDTO-objekt steg för steg
 * som sedan kan användas för att slippa boilerplate-kod.
 * <p>
 * Klassen används främst i tester för att enkelt skapa testdata
 * utan att behöva fylla i alla fält för UserResponseDTO varje gång.
 */
public final class UserResponceDtoBuilder {

    private Long id;
    private String alias;
    private String email;
    private String firstName;
    private String lastName;
    private String profilePicture;
    private String role;


    private UserResponceDtoBuilder() {

    }

    public static UserResponceDtoBuilder builder() {
        return new UserResponceDtoBuilder();
    }


    public UserResponceDtoBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    public UserResponceDtoBuilder withAlias(String alias) {
        this.alias = alias;
        return this;
    }

    public UserResponceDtoBuilder withEmail(String email) {
        this.email = email;
        return this;
    }

    public UserResponceDtoBuilder withFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public UserResponceDtoBuilder withLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public UserResponceDtoBuilder withProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
        return this;
    }

    public UserResponceDtoBuilder withRole(String role) {
        this.role = role;
        return this;
    }

    public UserResponseDTO build() {
        return new UserResponseDTO(
                id,
                alias,
                email,
                firstName,
                lastName,
                profilePicture,
                role
        );
    }


}
