package com.example.jokesapi.model;

import org.springframework.data.annotation.Id;

public class Joke {
	@Id
    private int id;          
    private String setup;    
    private String punchline;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSetup() {
		return setup;
	}
	public void setSetup(String setup) {
		this.setup = setup;
	}
	public String getPunchline() {
		return punchline;
	}
	public void setPunchline(String punchline) {
		this.punchline = punchline;
	}
    
}
