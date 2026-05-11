package com.example.comfymock.service;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.comfymock.dto.request.UploadRequest;
import com.example.comfymock.dto.response.HistoryResponse;
import com.example.comfymock.dto.response.ViewResponse;
import com.example.comfymock.utils.ComfyMockUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class ComfyMockService {

	private final Map<String, HistoryResponse.PromptHistory> historyStorage = new ConcurrentHashMap<>();
	private final Map<String, byte[]> imageByteStorage = new ConcurrentHashMap<>();
	private final ComfyMockUtils comfyMockUtils;

	// 이미지 저장
	public String saveImageBytes(UploadRequest request) throws IOException {
		String filename = request.getImage().getOriginalFilename();
		byte[] imageBytes = request.getImage().getBytes();

		String cleanFilename = comfyMockUtils.extractFilename(filename);

		imageByteStorage.put(cleanFilename, imageBytes);

		log.info("이미지 저장됨: {} ({} bytes)", cleanFilename, imageBytes.length);
		return cleanFilename;
	}

	// 이미지 생성
	public void simulateProcessing(String promptId, Map<String, Object> promptMap) {
		// 프롬프트 JSON에서 이미지 파일명 추출
		String targetFilename = comfyMockUtils.findInputImageName(promptMap);
		String cleanFilename = comfyMockUtils.extractFilename(targetFilename);
		validateFileExists(cleanFilename);

		HistoryResponse.PromptHistory history = createSuccessHistory(cleanFilename);

		historyStorage.put(promptId, history);
		log.info("이미지 생성 완료. Output: {}", cleanFilename);
	}

	// 히스토리 조회
	public Map<String, HistoryResponse.PromptHistory> getHistory(String promptId) {
		HistoryResponse.PromptHistory history =
			Optional.ofNullable(historyStorage.get(promptId))
				.orElseThrow(() ->
					new IllegalArgumentException("히스토리를 찾을 수 없습니다. 요청 id: " + promptId)
				);

		return Map.of(promptId, history);
	}

	// 이미지 조회
	public ViewResponse downloadImage(String filename) {
		String cleanFilename = comfyMockUtils.extractFilename(filename);
		validateFileExists(cleanFilename);

		return ViewResponse.builder()
			.imageBytes(imageByteStorage.get(cleanFilename))
			.mediaType(comfyMockUtils.determineMediaType(cleanFilename))
			.build();
	}

	// 성공 상태의 PromptHistory 객체 생성
	private HistoryResponse.PromptHistory createSuccessHistory(String filename) {
		// 결과 이미지 정보 생성
		HistoryResponse.ImageOutput imageOutput = HistoryResponse.ImageOutput.builder()
			.filename(filename)
			.subfolder("")
			.type("output")
			.build();

		// 노드 출력 데이터 생성
		HistoryResponse.NodeOutput nodeOutput = HistoryResponse.NodeOutput.builder()
			.images(List.of(imageOutput))
			.build();

		Map<String, HistoryResponse.NodeOutput> outputs = new HashMap<>();
		outputs.put("9", nodeOutput);

		// 완료 상태 정보 생성
		HistoryResponse.Status status = HistoryResponse.Status.builder()
			.completed(true)
			.statusStr("success")
			.messages(Collections.emptyList())
			.build();

		// 최종 PromptHistory 매핑 및 반환
		return HistoryResponse.PromptHistory.builder()
			.status(status)
			.outputs(outputs)
			.build();
	}

	// 이미지 존재 여부 체크
	private void validateFileExists(String filename) {
		if (!imageByteStorage.containsKey(filename)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 이미지: " + filename);
		}
	}
}
