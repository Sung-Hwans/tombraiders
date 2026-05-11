package com.example.comfymock.dto.response;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PromptResponse {
	@JsonProperty("prompt_id")
	private String promptId;

	@JsonProperty("number")
	private Integer number;

	@JsonProperty("node_errors")
	private Map<String, Object> nodeErrors;
}
