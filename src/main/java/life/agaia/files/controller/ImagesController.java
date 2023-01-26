package life.agaia.files.controller;

import life.agaia.files.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
@Slf4j
public class ImagesController {

    private final ImageService imageService;

    @GetMapping("{size}/{type}/{fileName}")
    public ResponseEntity<byte[]> getImageThumb(
        @PathVariable String size,
        @PathVariable String type,
        @PathVariable String fileName
    ) throws IOException {
        String fileNameLower = fileName.toLowerCase();

        return ResponseEntity.ok()
            .contentType(fileNameLower.contains(".png") ? MediaType.IMAGE_PNG : (fileNameLower.contains(".jpg") || fileNameLower.contains(".jpeg") ? MediaType.IMAGE_JPEG : MediaType.IMAGE_GIF))
            .body(imageService.getImageThumbBytes(size, type, fileName).getImage());
    }

}
