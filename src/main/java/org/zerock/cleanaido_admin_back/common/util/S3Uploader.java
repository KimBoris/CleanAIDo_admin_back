package org.zerock.cleanaido_admin_back.common.util;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Component
@RequiredArgsConstructor
@Log4j2
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    public String bucket;  // S3 버킷 이름

    // S3로 파일 업로드하기
    public String upload(String filePath)throws RuntimeException {

        File targetFile = new File(filePath);

        String uploadImageUrl = putS3(targetFile, targetFile.getName()); // s3로 업로드

        return uploadImageUrl;
    }


    // S3로 업로드
    private String putS3(File uploadFile, String fileName)throws RuntimeException
    {

        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName,
                uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    //S3 업로드 후 원본 파일 삭제
    private void removeOriginalFile(File targetFile) {
        if (targetFile.exists() && targetFile.delete()) {
            log.info("File delete success");
            return;
        }
        log.info("fail to remove");
    }

    public void removeS3File(String fileName){
        final DeleteObjectRequest deleteObjectRequest = new
                DeleteObjectRequest(bucket, fileName);
        amazonS3Client.deleteObject(deleteObjectRequest);
    }

    // S3 업로드 메서드
    public String uploadToS3(MultipartFile file, String fileName) throws IOException {
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(file.getSize());
        metadata.setContentType(file.getContentType());

        amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

    // S3로 업로드
    public String uploadThumbNail(File uploadFile, String fileName)throws RuntimeException
    {

        amazonS3Client.putObject(new PutObjectRequest(bucket, fileName,
                uploadFile)
                .withCannedAcl(CannedAccessControlList.PublicRead));
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }

}
