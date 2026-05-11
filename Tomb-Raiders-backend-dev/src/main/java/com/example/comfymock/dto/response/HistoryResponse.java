package com.example.comfymock.dto.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;

public class HistoryResponse {

	@Getter
	@Builder
	public static class PromptHistory {
		@JsonProperty("status")
		private Status status;

		@JsonProperty("outputs")
		private Map<String, NodeOutput> outputs;
	}

	@Getter
	@Builder
	public static class Status {
		@JsonProperty("completed")
		private boolean completed;

		@JsonProperty("status_str")
		private String statusStr;

		@JsonProperty("messages")
		private List<Object> messages;
	}

	@Getter
	@Builder
	public static class NodeOutput {
		@JsonProperty("images")
		private List<ImageOutput> images;
	}

	@Getter
	@Builder
	public static class ImageOutput {
		@JsonProperty("filename")
		private String filename;

		@JsonProperty("subfolder")
		private String subfolder;

		@JsonProperty("type")
		private String type;
	}
}
