package com.example.comfymock.dto.response;

import org.springframework.http.MediaType;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ViewResponse {
	private byte[] imageBytes;
	private MediaType mediaType;
}
