package DTO;

public class JokeResponseDTO {
    private Long id;
    private String setup;
    private String punchline;

    public JokeResponseDTO(Long id, String setup, String punchline) {
        this.id = id;
        this.setup = setup;
        this.punchline = punchline;
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getSetup() {
        return setup;
    }

    public String getPunchline() {
        return punchline;
    }
    
}

