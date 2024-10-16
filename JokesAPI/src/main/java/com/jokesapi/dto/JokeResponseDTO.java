package com.jokesapi.dto;

public class JokeResponseDTO {
	private String type;
    private String setup;
    private String punchline;
    private Integer id;

    public JokeResponseDTO(Integer id, String setup, String punchline) {
        this.id = id;
        this.setup = setup;
        this.punchline = punchline;
    }

    // Getters
    public  Integer getId() {
        return id;
    }

    public String getSetup() {
        return setup;
    }

    public String getPunchline() {
        return punchline;
    }
    
    public void setId(Integer id) {
		this.id = id;
	}

	public void setSetup(String setup) {
		this.setup = setup;
	}

	public void setPunchline(String punchline) {
		this.punchline = punchline;
	}
	

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public JokeResponseDTO() {}
}

