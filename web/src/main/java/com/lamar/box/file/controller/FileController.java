package com.lamar.box.file.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lamar.box.file.dto.FileDTO;
import com.lamar.box.file.entity.File;
import com.lamar.box.file.service.FileService;

@RestController
@CrossOrigin(	origins = "*",
				methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,RequestMethod.DELETE, RequestMethod.OPTIONS })
@RequestMapping("/api/files")
public class FileController {

	@Autowired
	private FileService fileService;
	
	@Autowired
	private ModelMapper modelMapper;

	@GetMapping("")
	public ResponseEntity<?> getAllUserFiles(@RequestHeader(name = "Authorization") String token) {

		String userID = this.fileService.getUserIDFromToken(token);
		
		List<File> files = fileService.getAllUserFiles(userID);
		
		List<FileDTO> filesDTO = files.stream().map(file -> modelMapper.map(file, FileDTO.class)).collect(Collectors.toList());

		return ResponseEntity.ok(filesDTO);
	}

	@PostMapping("")
	public ResponseEntity<?> saveFile(
			@RequestHeader(name = "Authorization") String token,
			@RequestParam("file") MultipartFile multipartFile) throws Exception {
		
		String userID = this.fileService.getUserIDFromToken(token);

		File file = fileService.saveFileDatabase(multipartFile, userID);
		
		fileService.saveFileDisk(multipartFile, file);
		
		FileDTO fileDTO = modelMapper.map(file, FileDTO.class);

		return ResponseEntity.ok(fileDTO);
	}

	@PutMapping("")
	public ResponseEntity<?> updateFilename(@RequestBody FileDTO fileDTO) throws IOException {
		
		File file = modelMapper.map(fileDTO, File.class);

		fileService.updateFile(file);
		
		fileDTO = modelMapper.map(file, FileDTO.class);

		return ResponseEntity.ok(fileDTO);
	}

	@GetMapping("/{fileID}/download")
	public ResponseEntity<ByteArrayResource> getFile(@PathVariable String fileID) throws SQLException, IOException {
		
		Pair<File, ByteArrayResource> pair = fileService.getFile(fileID);

		MediaType mediaType = MediaType.parseMediaType(pair.getFirst().getType());

		return ResponseEntity
				.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=\"" + pair.getFirst().getFilename() + "\"")
				.contentLength(pair.getSecond().contentLength())
				.lastModified(pair.getFirst().getLastModified())
				.contentType(mediaType)
				.body(pair.getSecond());
	}

	@DeleteMapping("/{fileID}")
	public ResponseEntity<?> deleteFile(@PathVariable String fileID) throws Exception {
		
		File file = fileService.deleteFileDatabase(fileID);
		
		fileService.deleteFileDisk(file);

		return ResponseEntity.ok().build();
	}

}
