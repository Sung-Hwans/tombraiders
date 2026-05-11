package com.example.comfymock.utils;

import java.nio.file.Paths;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Component
public class ComfyMockUtils {

	// 프롬프트 JSON에서 파일명 찾기
	public String findInputImageName(Map<String, Object> promptMap) {
		if (promptMap == null) {
			throw new IllegalArgumentException("Prompt map이 null입니다");
		}

		for (Object nodeObj : promptMap.values()) {
			if (nodeObj instanceof Map) {
				Map<?, ?> node = (Map<?, ?>) nodeObj;
				Map<?, ?> inputs = (Map<?, ?>) node.get("inputs");

				if (inputs == null) {
					continue;
				}

				// "image_path" 키 확인
				if (inputs.containsKey("image_path")) {
					Object pathObj = inputs.get("image_path");
					if (pathObj instanceof String) {
						return (String) pathObj;
					}
				}
			}
		}

		throw new NoSuchElementException("이미지 경로를 찾을 수 없습니다");
	}

	// 순수 파일 이름 반환
	public String extractFilename(String path) {
		if (path == null || path.isBlank()) {
			throw new IllegalArgumentException("파일명이 비어있습니다");
		}

		return Paths.get(path).getFileName().toString();
	}

	// 확장자에 따른 MediaType 결정
	public MediaType determineMediaType(String filename) {
		String lower = filename.toLowerCase();

		if (lower.endsWith(".jpg") || lower.endsWith(".jpeg")) {
			return MediaType.IMAGE_JPEG;
		}
		if (lower.endsWith(".gif")) {
			return MediaType.IMAGE_GIF;
		}

		return MediaType.IMAGE_PNG;
	}
}
