package com.jokesapi.dto;

public class JokeResponseDTO {
    private int id;
    private String setup;
    private String punchline;

    public JokeResponseDTO(int id, String setup, String punchline) {
        this.id = id;
        this.setup = setup;
        this.punchline = punchline;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getSetup() {
        return setup;
    }

    public String getPunchline() {
        return punchline;
    }
    
    public void setId(int id) {
		this.id = id;
	}

	public void setSetup(String setup) {
		this.setup = setup;
	}

	public void setPunchline(String punchline) {
		this.punchline = punchline;
	}

	public JokeResponseDTO() {}
}

