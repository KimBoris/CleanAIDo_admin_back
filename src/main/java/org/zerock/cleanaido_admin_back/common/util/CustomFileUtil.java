package org.zerock.cleanaido_admin_back.common.util;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

  private final S3Uploader s3Uploader;
  private final FileUploadUtil fileUploadUtil;

  @Value("${org.zerock.upload.path}")
  private String uploadPath;

  @PostConstruct
  public void init() {
    File tempFolder = new File(uploadPath);

    if(tempFolder.exists() == false) {
      tempFolder.mkdir();
    }

    uploadPath = tempFolder.getAbsolutePath();

    log.info("-------------------------------------");
    log.info(uploadPath);
  }

  public List<String> saveFiles(List<MultipartFile> files)throws RuntimeException{

    if(files == null || files.size() == 0){
      return null;
    }

    List<String> uploadNames = new ArrayList<>();
    List<String> uploadThumbNailNames = new ArrayList<>();

    for (MultipartFile multipartFile : files) {

      String savedName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();

      Path savePath = Paths.get(uploadPath, savedName);

      try {
        //임시 upload파일에 저장
        Files.copy(multipartFile.getInputStream(), savePath);
        //s3에 업로드
        String uploadedUrl = s3Uploader.upload(uploadPath+"/"+savedName);
        String contentType = multipartFile.getContentType();

        if(contentType != null && contentType.startsWith("image")){ //이미지여부 확인
          Path thumbnailPath = Paths.get(uploadPath, "s_"+savedName);
          //썸네일 생성
          Thumbnails.of(savePath.toFile())
                  .size(400,400)
                  .toFile(thumbnailPath.toFile());
        }
        String uploadThumbnailUrl = s3Uploader.upload(uploadPath+"/s_"+savedName);
        uploadNames.add(savedName);
        uploadThumbNailNames.add("s_"+savedName);
      } catch (IOException e) {
        throw new RuntimeException(e.getMessage());
      }finally {
        //업로드 완료시 임시파일 제거
        deleteFiles(uploadNames);
        deleteFiles(uploadThumbNailNames);
      }
    }//end for
    return uploadNames;
  }
  //썸네일 생성을 제외한 사용처 이미지 업로드
  public List<String> saveUsageFiles(List<MultipartFile> files)throws RuntimeException{

    if(files == null || files.size() == 0){
      return null;
    }

    List<String> uploadNames = new ArrayList<>();

    for (MultipartFile multipartFile : files) {

      String savedName = UUID.randomUUID().toString() + "_" + multipartFile.getOriginalFilename();

      Path savePath = Paths.get(uploadPath, savedName);

      try {
        Files.copy(multipartFile.getInputStream(), savePath);
        String savedLoacation = uploadPath+"/"+savedName;
        String uploadedUrl = s3Uploader.upload(savedLoacation);
        //파이선 서버에도 같은 사진을 전송
        String uploadToApi = fileUploadUtil.uploadImages(savedLoacation);
        uploadNames.add(savedName);
      } catch (IOException e) {
        throw new RuntimeException(e.getMessage());
      }finally {
        //업로드 완료시 임시파일 제거
        deleteFiles(uploadNames);
      }
    }//end for
    return uploadNames;
  }

  public ResponseEntity<Resource> getFile(String fileName) {

    Resource resource = new FileSystemResource(uploadPath+ File.separator + fileName);

    if(!resource.exists()) {

      resource = new FileSystemResource(uploadPath+ File.separator + "default.jpeg");

    }

    HttpHeaders headers = new HttpHeaders();

    try{
      headers.add("Content-Type", Files.probeContentType( resource.getFile().toPath() ));
    } catch(Exception e){
      return ResponseEntity.internalServerError().build();
    }
    return ResponseEntity.ok().headers(headers).body(resource);
  }

  public String saveFile(MultipartFile file) throws RuntimeException {

    if (file == null || file.isEmpty()) {
      return null;
    }

    // 고유한 파일 이름 생성
    String savedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

    // 저장 경로 설정
    Path savePath = Paths.get(uploadPath, savedName);

    try {
      // 파일을 저장 경로로 복사
      Files.copy(file.getInputStream(), savePath);
    } catch (IOException e) {
      throw new RuntimeException(e.getMessage());
    }

    return savedName;
  }

  public void deleteFiles(List<String> fileNames) {

    if(fileNames == null || fileNames.size() == 0){
      return;
    }

    fileNames.forEach(fileName -> {

      //썸네일이 있는지 확인하고 삭제
      String thumbnailFileName = "s_" + fileName;
      Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
      Path filePath = Paths.get(uploadPath, fileName);

      try {
        Files.deleteIfExists(filePath);
        Files.deleteIfExists(thumbnailPath);
      } catch (IOException e) {
        throw new RuntimeException(e.getMessage());
      }
    });
  }



}