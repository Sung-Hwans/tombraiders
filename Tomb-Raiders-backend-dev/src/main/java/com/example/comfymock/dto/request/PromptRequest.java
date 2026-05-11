package com.example.comfymock.dto.request;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@NotNull
public class PromptRequest {
	@JsonProperty("client_id")
	private String clientId;

	@JsonProperty("prompt")
	private Map<String, Object> prompt;
}
