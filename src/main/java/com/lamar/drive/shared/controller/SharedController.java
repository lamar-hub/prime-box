package com.lamar.drive.shared.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.lamar.drive.shared.dto.SharedDTO;
import com.lamar.drive.shared.entity.Shared;
import com.lamar.drive.shared.service.SharedService;

@RestController
@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT,
		RequestMethod.DELETE, RequestMethod.OPTIONS })
@RequestMapping("/api/shareds")
public class SharedController {

	@Autowired
	private SharedService sharedService;
	
	@Autowired
	private ModelMapper modelMapper;
	
	@GetMapping()
	public ResponseEntity<?> getAllUserShareds(@RequestHeader(name = "Authorization") String token) {
		
		String userID = this.sharedService.getUserIDFromToken(token);

		List<Shared> shareds = sharedService.getAllUserSharedFiles(userID);
		
		List<SharedDTO> sharedsDTO = shareds.stream().map(shared -> modelMapper.map(shared, SharedDTO.class)).collect(Collectors.toList());

		return ResponseEntity.ok(sharedsDTO);
	}

	@PostMapping("")
	public ResponseEntity<?> shareFile(@RequestBody SharedDTO sharedDTO) {
		
		Shared shared = modelMapper.map(sharedDTO, Shared.class);

		sharedService.share(shared);
		
		sharedDTO = modelMapper.map(shared, SharedDTO.class);

		return ResponseEntity.ok(sharedDTO);
	}

	@DeleteMapping("/{fileID}")
	public ResponseEntity<?> unshareFile(
			@RequestHeader(name = "Authorization") String token,
			@PathVariable String fileID) throws Exception {
		
		String userID = this.sharedService.getUserIDFromToken(token);

		sharedService.unshare(userID, fileID);

		return ResponseEntity.ok().build();
	}

}
