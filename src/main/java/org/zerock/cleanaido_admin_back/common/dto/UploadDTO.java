package org.zerock.cleanaido_admin_back.common.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadDTO {

    private MultipartFile[] files;

    private List<String> uploadedFileNames;

}
