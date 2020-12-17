package com.example.gridsfsvsjackrabbit.rest;

import com.example.gridsfsvsjackrabbit.model.RetrievedFile;
import com.example.gridsfsvsjackrabbit.model.SavedFile;
import com.example.gridsfsvsjackrabbit.service.JackrabbitStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.jcr.RepositoryException;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jackrabbit/files")
public class JackrabbitFileRestEndpoint {

	private final JackrabbitStorageService jackrabbitStorageService;

	@PostMapping
	public ResponseEntity<SavedFile> saveFile(@RequestParam MultipartFile file) throws IOException, RepositoryException {
		SavedFile savedFile = jackrabbitStorageService.saveFile(file.getOriginalFilename(), file.getContentType(), file.getBytes());
		return ResponseEntity.status(HttpStatus.CREATED).body(savedFile);
	}

	@GetMapping(value = "/{fileId}")
	public ResponseEntity<Resource> getFile(@PathVariable String fileId) throws IOException, RepositoryException {
		RetrievedFile file = jackrabbitStorageService.getFile(fileId);
		ByteArrayResource byteArrayResource = new ByteArrayResource(file.getFile());
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(file.getContentType()))
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileId + "\"")
				.body(byteArrayResource);
	}

}
