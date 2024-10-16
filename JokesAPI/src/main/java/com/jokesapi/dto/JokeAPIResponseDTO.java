package com.jokesapi.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class JokeAPIResponseDTO {
	 @Schema(description = "Unique identifier of the joke", example = "1")
	    private Integer id;

	    @Schema(description = "The joke question", example = "What did the ocean say to the shore?")
	    private String question;

	    @Schema(description = "The joke answer", example = "Nothing, it just waved.")
	    private String answer;
    
    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    

    public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public JokeAPIResponseDTO(Integer id,String question, String answer) {
		this.id=id;
        this.question = question;
        this.answer = answer;
    }

    public JokeAPIResponseDTO() {}
}
