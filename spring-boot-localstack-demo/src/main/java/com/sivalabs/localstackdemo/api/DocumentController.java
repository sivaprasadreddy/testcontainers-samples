package com.sivalabs.localstackdemo.api;

import com.sivalabs.localstackdemo.domain.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class DocumentController {

	private final DocumentService documentService;

	@PostMapping("/docs")
	public Map<String, String> uploadDocs(@RequestParam("file") MultipartFile multipartFile) throws IOException {
		var filename = multipartFile.getOriginalFilename();
		var extn = filename.substring(filename.lastIndexOf("."));
		filename = UUID.randomUUID() + extn;
		documentService.upload(filename, multipartFile.getInputStream());
		return Map.of("filename", filename);
	}

}
