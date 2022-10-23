package life.agaia.files;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import javax.annotation.PostConstruct;
import java.util.TimeZone;

@SpringBootApplication(scanBasePackages = {
    "life.agaia.files"
})
public class FilesApplication {

    public static void main(String[] args) {
        SpringApplication.run(FilesApplication.class, args);
    }

    @PostConstruct
    public void init() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Sofia"));
    }

}
