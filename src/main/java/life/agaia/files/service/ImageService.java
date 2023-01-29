package life.agaia.files.service;

import com.amazonaws.services.s3.model.S3ObjectInputStream;
import life.agaia.files.dto.ImageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.tasks.UnsupportedFormatException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@EnableCaching
public class ImageService {

    private final DoService doService;

    @Cacheable(value = "thumb")
    public ImageDto getImageThumbBytes(String size, String type, String fileName) throws IOException {
        String thumbnailFilePath = getImageThumb(size, type, fileName);

        return ImageDto.builder()
            .image(doService.getFileContentByte(thumbnailFilePath))
            .build();
    }

    private String getImageThumb(String size, String type, String fileName) {
        String thumbnailFilePath = String.format("media/images/thumbnails/%s-%s-%s", size, type, fileName);

        if (doService.isFileExist(thumbnailFilePath)) {
            return thumbnailFilePath;
        }

        String originalFilePath = String.format("media/images/%s/%s", type, fileName);

        S3ObjectInputStream imgStream = null;

        try {
            String[] sizes = size.split("x");
            int width = Integer.parseInt(sizes[0]);
            int height = Integer.parseInt(sizes[1]);
            String newFileName = generateOutputName(originalFilePath);

            imgStream = doService.getFileContent(originalFilePath);

            Thumbnails.of(imgStream)
                .size(width, height)
                .outputQuality(0.8)
                .toFile(newFileName);

            imgStream.close();

            doService.writeFileContent(thumbnailFilePath, newFileName);

            return thumbnailFilePath;
        } catch (UnsupportedFormatException e) {
            log.error("Unsupported format for original image for {}", originalFilePath);
            return originalFilePath;
        } catch (IOException e) {
            log.error("Failed to create thumbnail for {}", originalFilePath, e);
            return originalFilePath;
        } finally {
            if (imgStream != null) {
                try {
                    imgStream.close();
                } catch (IOException e) {
                    log.error("Unable to close stream on final catch for {}", originalFilePath, e);
                }
            }
        }
    }

    private String generateOutputName(String originalFile) {
        String lowerPath = originalFile.toLowerCase();

        return String.format("%s%s", randomString(15), lowerPath.contains(".png") ? ".png" : (lowerPath.contains(".jpg") || lowerPath.contains(".jpeg") ? ".jpg" : ".gif"));
    }

    private String randomString(int length) {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        Random random = new Random();

        return random.ints(leftLimit, rightLimit + 1)
            .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
            .limit(length)
            .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
    }


}
