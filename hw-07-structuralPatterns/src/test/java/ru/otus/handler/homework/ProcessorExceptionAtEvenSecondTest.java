package ru.otus.handler.homework;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.otus.model.Message;
import ru.otus.processor.homework.NowService;
import ru.otus.processor.homework.ProcessorExceptionAtEvenSecond;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ProcessorExceptionAtEvenSecondTest {

    @Test
    @DisplayName("Тестируем выброс exception в четную секунду")
    void evenSecond_trowException() {
        var message = new Message.Builder(1L).field1("field1").build();

        var nowService = mock(NowService.class);
        var processor = new ProcessorExceptionAtEvenSecond(nowService);
        int evenSecond = 2;
        when(nowService.now()).thenReturn(LocalDateTime.of(2022, 1, 1, 1, 1, evenSecond));

        assertThrows(RuntimeException.class, () -> processor.process(message));
    }

    @Test
    @DisplayName("Тестируем отсутствие exception в нечетную секунду")
    void oddSecond_doNotTrowException() {
        var message = new Message.Builder(1L).field1("field1").build();

        var nowService = mock(NowService.class);
        var processor = new ProcessorExceptionAtEvenSecond(nowService);
        int oddSecond = 1;
        when(nowService.now()).thenReturn(LocalDateTime.of(2022, 1, 1, 1, 1, oddSecond));

        assertDoesNotThrow(() -> processor.process(message));
    }
}
