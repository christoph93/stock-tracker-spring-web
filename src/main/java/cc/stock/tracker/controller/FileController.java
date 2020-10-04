package cc.stock.tracker.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import cc.stock.tracker.service.FileServiceImpl;

@CrossOrigin
@Controller
public class FileController {

	static Logger log = Logger.getLogger(FileController.class.getName());
	@Autowired
	FileServiceImpl fileService;

	@PostMapping("/uploadTransactions")
	public ResponseEntity<String> uploadTransactionsFile(@RequestParam("file") MultipartFile file,
			@RequestParam("userSub") String userSub, RedirectAttributes redirectAttributes) {

		fileService.uploadTransactionsFile(file, userSub);

		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return ResponseEntity.status(HttpStatus.OK).body("Test");
	}

	@PostMapping("/uploadDividends")
	public ResponseEntity<String> uploadDividendsFile(@RequestParam("file") MultipartFile file,
			@RequestParam("userSub") String userSub, RedirectAttributes redirectAttributes) {

		fileService.uploadDividendsFile(file, userSub);

		redirectAttributes.addFlashAttribute("message",
				"You successfully uploaded " + file.getOriginalFilename() + "!");

		return ResponseEntity.status(HttpStatus.OK).body("Test");
	}
}
