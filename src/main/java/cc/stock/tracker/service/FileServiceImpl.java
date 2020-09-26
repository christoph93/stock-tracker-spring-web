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

    @Value("${cc.stocktrackerapi.fileService.uploadPath:${user.home}}")
    public String uploadDir;
    
    @Autowired
    ExcelUtilsImpl excelUtilsImpl;

    public void uploadFile(MultipartFile file) {

        try {
        	//save file
            Path copyLocation = Paths
                .get(uploadDir + File.separator + StringUtils.cleanPath(file.getOriginalFilename()));
            Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
            
            //process file
            excelUtilsImpl.saveTransactionsToMongo("D:/StockTracker/stock-tracker-spring-web/fileUploads/" + file.getOriginalFilename());
            
            
        } catch (Exception e) {
            e.printStackTrace();            
        }
    }
}