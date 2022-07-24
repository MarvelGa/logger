package org.logger;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.logger.exception.FileCanNotBeParsedException;
import org.logger.service.LogService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.net.URISyntaxException;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class Application implements CommandLineRunner {
    private final LogService logService;

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(Application.class);
        application.run(args);
    }

    @Override
    public void run(String... args) {
        String fileName = "src/main/resources/logfile.txt";
        try {
            if (args.length == 0) {
                logService.processLog(fileName);
            } else {
                logService.processLog(args[0]);
            }
        } catch (URISyntaxException e) {
            log.error("Unable to read the file", e.getMessage());
        } catch (IOException e) {
            log.error("Not valid the file's path", e.getMessage());
        } catch (FileCanNotBeParsedException e) {
            log.error(e.getMessage());
        }
    }
}
