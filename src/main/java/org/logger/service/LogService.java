package org.logger.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.logger.entity.Log;
import org.logger.entity.LogDetails;
import org.logger.exception.FileCanNotBeParsedException;
import org.logger.repository.LogDetailsRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@RequiredArgsConstructor
@Service
public class LogService {

    private final LogDetailsRepository repository;

    public boolean processLog(String fileName) throws URISyntaxException, IOException {
        log.debug("\"processLog\" method starts");
        Long durationAlert = 4L;
        Map<String, LogDetails> idToLogDetails = new HashMap<>();
        List<Log> parsedLogs = parseLogs(fileName);
        getLogDetails(durationAlert, idToLogDetails, parsedLogs);
        storeLogDetails(idToLogDetails);
        printParsedLogsAndLogDetails(parsedLogs);
        log.debug("\"processLog\" method finished");
        return true;
    }

    public boolean printParsedLogsAndLogDetails(List<Log> parsedLogs) {
        log.info("\"printLogDetails\" method starts");
        System.out.println("**************Result Of Parsing The File******************************");
        parsedLogs.stream().forEach(System.out::println);
        System.out.println("********************************************");
        List<LogDetails> result = (List<LogDetails>) repository.findAll();
        System.out.println("*****************Result About Logs Details From DataBase***************************");
        result.stream().forEach(System.out::println);
        System.out.println("********************************************");
        log.info("\"printLogDetails\" method finished");
        return true;
    }

    public Iterable<LogDetails> storeLogDetails(Map<String, LogDetails> idToLogDetails) {
        log.info("\"storeLogDetails\" method starts");
        Iterable<LogDetails> result =  repository.saveAll(idToLogDetails.values());
        log.info("\"storeLogDetails\" method finished");
        return result;
    }

    public void getLogDetails(Long durationAlert, Map<String, LogDetails> idToLogDetails, List<Log> parsedLogs) {
        log.info("\"getLogDetails\" method starts");
        parsedLogs.forEach(log -> {
            if (!idToLogDetails.containsKey(log.getId())) {
                LogDetails createdLD = convertLogToLogDetails(log);
                idToLogDetails.put(log.getId(), createdLD);
            } else {
                LogDetails logDetails = idToLogDetails.get(log.getId());
                Long durationDiff = Math.abs(log.getTimestamp() - logDetails.getDuration());
                if (durationDiff > durationAlert) {
                    logDetails.setAlert(true);
                }
                logDetails.setDuration(durationDiff);
                idToLogDetails.put(log.getId(), logDetails);
            }
        });
        log.info("\"getLogDetails\" method finished");
    }

    public LogDetails convertLogToLogDetails(Log log) {
        LogDetails createdLD = new LogDetails();
        createdLD.setId(log.getId());
        createdLD.setDuration(log.getTimestamp());
        createdLD.setHost(log.getHost());
        createdLD.setType(log.getType());
        createdLD.setAlert(false);
        return createdLD;
    }

    public List<Log> parseLogs(String fileName) throws IOException, URISyntaxException {
        log.info("\"parseLogs\" method starts");
        ObjectMapper objectMapper = new ObjectMapper();
        Stream<String> stringStream = Files.lines(Paths.get(ClassLoader.getSystemResource(fileName)
                .toURI()));

        AtomicReference<List<Log>> parsedLogs = new AtomicReference<>(stringStream
                .parallel()
                .map(el -> {
                    try {
                        return objectMapper.readValue(el, Log.class);
                    } catch (JsonProcessingException e) {
                        log.info("the JsonProcessingException were caught", e.getMessage());
                        throw new FileCanNotBeParsedException("There is problem with parsing the file. Check the text in the file!");
                    }
                })
                .collect(Collectors.toList()));
        stringStream.close();
        log.info("\"parseLogs\" method finished");
        return parsedLogs.get();
    }

    public boolean validatePath(String fileName) throws IOException {
        log.info("\"validatePath\" method starts");
        new ClassPathResource(fileName).getFile();
        log.info("\"validatePath\" method finished");
        return true;
    }
}
