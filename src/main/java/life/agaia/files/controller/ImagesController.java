package life.agaia.files.controller;

import life.agaia.files.service.DoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.util.Random;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
@Slf4j
public class ImagesController {

    private final DoService doService;

    @GetMapping("{size}/{type}/{fileName}")
    public ResponseEntity<InputStreamResource> getImageThumb(
        @PathVariable String size,
        @PathVariable String type,
        @PathVariable String fileName
    ) {
        String thumbnailFilePath = String.format("media/images/thumbnails/%s-%s-%s", size, type, fileName);

        if (doService.isFileExist(thumbnailFilePath)) {
            return makeResponse(thumbnailFilePath);
        }

        String originalFilePath = String.format("media/images/%s/%s", type, fileName);

        try {
            String[] sizes = size.split("x");
            int width = Integer.parseInt(sizes[0]);
            int height = Integer.parseInt(sizes[1]);
            String newFileName = generateOutputName(originalFilePath);

            Thumbnails.of(doService.getFileContent(originalFilePath))
                .size(width, height)
                .outputQuality(0.8)
                .toFile(newFileName);

            doService.writeFileContent(thumbnailFilePath, newFileName);
        } catch (IOException e) {
            log.error("Failed to create thumbnail for {}", originalFilePath, e);
            return makeResponse(originalFilePath);
        }

        return makeResponse(thumbnailFilePath);
    }

    private ResponseEntity<InputStreamResource> makeResponse(String path) {
        String lowerPath = path.toLowerCase();

        return ResponseEntity.ok()
            .contentType(lowerPath.contains(".png") ? MediaType.IMAGE_PNG : (lowerPath.contains(".jpg") || lowerPath.contains(".jpeg") ? MediaType.IMAGE_JPEG : MediaType.IMAGE_GIF))
            .body(new InputStreamResource(doService.getFileContent(path)));
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
