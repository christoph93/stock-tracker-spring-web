package cc.stock.tracker.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileServiceImpl {

	// @Value("${cc.stocktrackerapi.fileService.uploadPath:${user.home}}")
	public String uploadFolder = "fileUploads";

	private String fs = System.getProperty("file.separator");

	private String uploadDir = System.getProperty("user.dir") + fs + uploadFolder;

	@Autowired
	ExcelUtilsImpl excelUtilsImpl;

	public void uploadTransactionsFile(MultipartFile file, String userSub) {

		String user = userSub.replace("|", "-");

		try {
			Files.createDirectory(Paths.get(uploadDir + File.separator + user));
			Files.createDirectory(Paths.get(uploadDir + File.separator + user + File.separator + "transactions"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// save file
			Path copyLocation = Paths.get(uploadDir + File.separator + user + File.separator + "transactions"
					+ File.separator + StringUtils.cleanPath(file.getOriginalFilename()));

			System.out.println(copyLocation.toString());

			Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

			// process file
			excelUtilsImpl.saveTransactionsToMongo(uploadDir + File.separator + user + File.separator + "transactions"
					+ File.separator + file.getOriginalFilename(), user);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void uploadDividendsFile(MultipartFile file, String userSub) {

		String user = userSub.replace("|", "-");

		try {
			Files.createDirectory(Paths.get(uploadDir + File.separator + user));
			Files.createDirectory(Paths.get(uploadDir + File.separator + user + File.separator + "dividends"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			// save file
			Path copyLocation = Paths.get(uploadDir + File.separator + user + File.separator + "dividends"
					+ File.separator + StringUtils.cleanPath(file.getOriginalFilename()));

			System.out.println(copyLocation.toString());

			Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);

			// process file
			excelUtilsImpl.saveDividendsToMongo(uploadDir + File.separator + user + File.separator + "dividends"
					+ File.separator + file.getOriginalFilename(), user);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}