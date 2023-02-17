package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorExceptionAtEvenSecond implements Processor {

    private final NowService nowService;

    public ProcessorExceptionAtEvenSecond(NowService nowService) {
        this.nowService = nowService;
    }

    @Override
    public Message process(Message message) {
        if (nowService.now().getSecond() % 2 == 0) {
            System.out.println("Second is even");
            throw new RuntimeException();
        }
        return message;
    }
}
