package ru.otus.grpc.server;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc.protobuf.generated.NumberGeneratorServiceGrpc;
import ru.otus.grpc.protobuf.generated.NumberGrpcRequest;
import ru.otus.grpc.protobuf.generated.NumberGrpcResponse;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

public class NumberGeneratorServiceImpl extends NumberGeneratorServiceGrpc.NumberGeneratorServiceImplBase {
    private static final Logger log = LoggerFactory.getLogger(NumberGeneratorServiceImpl.class);

    private static final int EXECUTOR_POOL_SIZE = 1;
    private static final int EXECUTOR_DELAY = 0;
    private static final int EXECUTOR_PERIOD = 2;

    @Override
    public void getNumber(NumberGrpcRequest request, StreamObserver<NumberGrpcResponse> responseObserver) {
        log.info("Start new numbers sequence: first {}, last {}", request.getFirst(), request.getLast());

        var current = new AtomicLong(request.getFirst());
        var executor = Executors.newScheduledThreadPool(EXECUTOR_POOL_SIZE);
        Runnable task = () -> {
            var value = current.incrementAndGet();
            var response = NumberGrpcResponse.newBuilder().setValue(value).build();
            responseObserver.onNext(response);
            if(value == request.getLast()){
                executor.shutdown();
                responseObserver.onCompleted();
                log.info("Sequence is end");
            }
        };

        executor.scheduleAtFixedRate(task, EXECUTOR_DELAY, EXECUTOR_PERIOD, TimeUnit.SECONDS);
    }
}
