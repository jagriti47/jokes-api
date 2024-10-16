package com.jokesapi.dto;
public class JokeSaveDTO {
	private Integer id;
    private String setup;
    private String punchline;

    public JokeSaveDTO(Integer id,String setup, String punchline) {
    	this.id=id;
        this.setup = setup;
        this.punchline = punchline;
    }

    public String getSetup() {
        return setup;
    }

    public String getPunchline() {
        return punchline;
    }

    
public void setSetup(String setup) {
		this.setup = setup;
	}

	public void setPunchline(String punchline) {
		this.punchline = punchline;
	}
	

public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

public JokeSaveDTO() {}
}

