package life.agaia.files.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class DoService {

    @Value("${do.space.bucket}")
    private String doSpaceBucket;

    private final AmazonS3 s3Client;

    public boolean isFileExist(String fileName) {
        try {
            return s3Client.doesObjectExist(doSpaceBucket, fileName);
        } catch (Exception exception) {
            log.error("Failed to check if file exist for {} fallback to false", fileName, exception);
            return false;
        }
    }

    public S3ObjectInputStream getFileContent(String fileName) {
        try {
            return s3Client.getObject(doSpaceBucket, fileName).getObjectContent();
        } catch (Exception exception) {
            log.error("Failed to fetch file for {} fallback to false", fileName, exception);
            throw exception;
        }
    }

    public byte[] getFileContentByte(String fileName) throws IOException {
        try {
            S3ObjectInputStream object = s3Client.getObject(doSpaceBucket, fileName).getObjectContent();
            byte[] bytes = object.readAllBytes();

            object.close();

            return bytes;
        } catch (Exception exception) {
            log.error("Failed to fetch file for {} fallback to false", fileName, exception);
            throw exception;
        }
    }

    public void writeFileContent(String fileName, String localPath) {
        try {
            File localFile = new File(localPath);

            s3Client.putObject(
                doSpaceBucket,
                fileName,
                localFile
            );

            localFile.delete();
        } catch (Exception exception) {
            log.error("Failed to write file for {}", fileName, exception);
        }
    }

}
