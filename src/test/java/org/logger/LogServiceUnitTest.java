package org.logger;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.logger.entity.Log;
import org.logger.entity.LogDetails;
import org.logger.exception.FileCanNotBeParsedException;
import org.logger.repository.LogDetailsRepository;
import org.logger.service.LogService;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
class LogServiceUnitTest {
    @MockBean
    private LogDetailsRepository repository;
    private LogService service;
    private Log logStart1;
    private Log logFinish1;

    private Log logStart2;
    private Log logFinish2;
    private LogDetails logDetails1;
    private LogDetails logDetails2;
    private String fileName = "forTest/fortest.txt";
    private String fileNameWithSyntaxProblem = "forTest/fileWithSyntaxProblems.txt";

    @BeforeEach
    void setUp() {
        service = new LogService(repository);

        logStart1 = new Log();
        logStart1.setId("aa");
        logStart1.setState(Log.StateType.STARTED);
        logStart1.setHost("111");
        logStart1.setType("APPLICATION_LOG");
        logStart1.setTimestamp(1491377495210L);

        logFinish1 = new Log();
        logFinish1.setId("aa");
        logFinish1.setState(Log.StateType.FINISHED);
        logFinish1.setHost("111");
        logFinish1.setType("APPLICATION_LOG");
        logFinish1.setTimestamp(1491377495213L);

        logStart2 = new Log();
        logStart2.setId("bb");
        logStart2.setState(Log.StateType.STARTED);
        logStart2.setTimestamp(1491377495211L);

        logFinish2 = new Log();
        logFinish2.setId("bb");
        logFinish2.setState(Log.StateType.FINISHED);
        logFinish2.setTimestamp(1491377495219L);

        logDetails1 = LogDetails.builder()
                .id("aa")
                .duration(3L)
                .host("111")
                .type("APPLICATION_LOG")
                .alert(false)
                .build();

        logDetails2 = LogDetails.builder()
                .id("aa")
                .duration(8L)
                .alert(true)
                .build();

    }

    @Test
    void shouldParsedLogs() throws IOException, URISyntaxException {
        List<Log> expected = Arrays.asList(logStart1, logStart2, logFinish2, logFinish1);

        List<Log> actual = service.parseLogs(fileName);

        assertEquals(expected, actual);
    }

    @Test
    void shouldThrowFileCanNotBeParsedException() {
        Throwable exception = Assertions.assertThrows(
                FileCanNotBeParsedException.class, () -> service.parseLogs(fileNameWithSyntaxProblem));

        assertEquals("There is problem with parsing the file. Check the text in the file!", exception.getMessage());

        assertEquals(FileCanNotBeParsedException.class, exception.getClass());
    }

    @Test
    void shouldConvertLogToLogDetails() {
        LogDetails expected = LogDetails.builder()
                .id("aa")
                .type("APPLICATION_LOG")
                .duration(1491377495210L)
                .host("111")
                .alert(false)
                .build();
        LogDetails actual = service.convertLogToLogDetails(logStart1);

        assertEquals(expected, actual);
    }

    @Test
    void shouldNotPassValidationAndThrowExceptionBecauseOfTheWrongFileName() {
        String notExistFileName = "notExist.txt";

        Throwable exception = Assertions.assertThrows(
                FileNotFoundException.class, () -> service.validatePath(notExistFileName));

        assertEquals(String.format("class path resource [%s] cannot be resolved to URL because it does not exist", notExistFileName), exception.getMessage());
        assertEquals(FileNotFoundException.class, exception.getClass());
    }

    @Test
    void shouldPassValidationIfFileNameIsCorrect() throws IOException {
        assertTrue(service.validatePath(fileName));
    }
}
