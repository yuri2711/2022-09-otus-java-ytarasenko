package ru.otus.grpc.client;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc.protobuf.generated.NumberGeneratorServiceGrpc;
import ru.otus.grpc.protobuf.generated.NumberGrpcRequest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;

public class NumbersClient {
    private static final Logger log = LoggerFactory.getLogger(NumbersClient.class);

    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 8190;
    private static final int NUMBER_FIRST = 0;
    private static final int NUMBER_LAST = 30;
    private static final int LOOP_LIMIT = 50;
    private static final int EXECUTOR_POOL_SIZE = 1;
    private static final int EXECUTOR_DELAY = 0;
    private static final int EXECUTOR_PERIOD = 1;

    private long current = 0;

    public static void main(String... args) {
        try {
            var client = new NumbersClient();
            client.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void start() throws InterruptedException {
        log.info("Call client start");

        ManagedChannel channel = ManagedChannelBuilder.forAddress(SERVER_HOST, SERVER_PORT).usePlaintext().build();
        var asyncStub = NumberGeneratorServiceGrpc.newStub(channel);

        CountDownLatch latch = new CountDownLatch(LOOP_LIMIT);
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(EXECUTOR_POOL_SIZE);

        NumberGrpcRequest request = NumberGrpcRequest.newBuilder().setFirst(NUMBER_FIRST).setLast(NUMBER_LAST).build();
        ClientStreamObserver streamObserver = new ClientStreamObserver();
        asyncStub.getNumber(request, streamObserver);

        Runnable runnableTask = () -> {
            current = current + streamObserver.getLastValueAndReset() + 1;
            log.info("Client value = {}", current);
            latch.countDown();
        };

        executor.scheduleAtFixedRate(runnableTask, EXECUTOR_DELAY, EXECUTOR_PERIOD, SECONDS);
        latch.await();

        log.info("Client is shutting down");
        channel.shutdown();
        executor.shutdown();
    }
}
