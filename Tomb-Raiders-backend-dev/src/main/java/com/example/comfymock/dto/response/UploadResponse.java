package com.example.comfymock.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UploadResponse {
	@JsonProperty("name")
	private String name;

	@JsonProperty("subfolder")
	private String subfolder;

	@JsonProperty("type")
	private String type;
}
