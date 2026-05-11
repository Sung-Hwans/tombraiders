package com.example.comfymock.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.comfymock.dto.request.PromptRequest;
import com.example.comfymock.dto.request.UploadRequest;
import com.example.comfymock.dto.request.ViewRequest;
import com.example.comfymock.dto.response.HistoryResponse;
import com.example.comfymock.dto.response.PromptResponse;
import com.example.comfymock.dto.response.UploadResponse;
import com.example.comfymock.dto.response.ViewResponse;
import com.example.comfymock.service.ComfyMockService;
import com.example.comfymock.utils.ComfyMockUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ComfyMockController {

	private final ComfyMockService mockComfyService;
	private final ComfyMockUtils comfyMockUtils;

	// 이미지 업로드
	@PostMapping(value = "/upload/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public UploadResponse uploadImage(@ModelAttribute UploadRequest request) throws IOException {
		String filename = mockComfyService.saveImageBytes(request);

		return UploadResponse.builder()
			.name(filename)
			.subfolder("")
			.type("input")
			.build();
	}

	// 이미지 생성 요청
	@PostMapping("/prompt")
	public PromptResponse queuePrompt(@RequestBody PromptRequest request) {
		// 랜덤 promptId 생성
		String promptId = UUID.randomUUID().toString();

		// 작업 시작
		mockComfyService.simulateProcessing(promptId, request.getPrompt());

		return PromptResponse.builder()
			.promptId(promptId)
			.number(0)
			.nodeErrors(Collections.emptyMap())
			.build();
	}

	// 히스토리 조회
	@GetMapping("/history/{promptId}")
	public Map<String, HistoryResponse.PromptHistory> getHistory(@PathVariable("promptId") String promptId) {
		log.info("[History] 요청 ID: {}", promptId);
		return mockComfyService.getHistory(promptId);
	}

	// 이미지 다운로드
	@GetMapping("/view")
	public ResponseEntity<byte[]> viewImage(@ModelAttribute ViewRequest request) {
		log.info("[View] 요청: {}", request.getFilename());

		// Service에서 데이터와 타입을 받아옴
		ViewResponse response = mockComfyService.downloadImage(request.getFilename());

		return ResponseEntity.ok()
			.contentType(response.getMediaType())
			.body(response.getImageBytes());
	}
}
