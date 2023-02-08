package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExecutorsHomeWork {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorsHomeWork.class);
    private String last = "Поток 2: ";
    private boolean inc = true;
    private int number = 1;


    private synchronized void action(String message) {
        while(!Thread.currentThread().isInterrupted()) {
            try {
                while (last.equals(message)) {
                    this.wait();
                }

                logger.info(message + number);
                if (message.equals("Поток 2: ")) {
                    if (number == 1) {
                        inc = true;
                    }
                    if (number == 10) {
                        inc = false;
                    }
                    if (inc) {
                        number++;
                    } else {
                        number--;
                    }
                }
                last = message;
                sleep();
                notifyAll();
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public static void main(String[] args) {
        ExecutorsHomeWork executorsHomeWork = new ExecutorsHomeWork();
        new Thread(() -> executorsHomeWork.action("Поток 1: ")).start();
        new Thread(() -> executorsHomeWork.action("Поток 2: ")).start();
    }

    private static void sleep() {
        try {
            Thread.sleep(1_000);
        } catch (InterruptedException e) {
            logger.error(e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}
