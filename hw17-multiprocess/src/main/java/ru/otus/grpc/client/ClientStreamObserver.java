package ru.otus.grpc.client;

import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.grpc.protobuf.generated.NumberGrpcResponse;

public class ClientStreamObserver implements StreamObserver<NumberGrpcResponse> {
    private static final Logger log = LoggerFactory.getLogger(ClientStreamObserver.class);

    private long last = 0;

    @Override
    public void onNext(NumberGrpcResponse value) {
        setLast(value.getValue());
        log.info("Get next value from server = {}", value.getValue());
    }

    @Override
    public void onError(Throwable t) {
        log.error("Exception: ", t);
    }

    @Override
    public void onCompleted() {
        log.info("Completed");
    }

    private synchronized void setLast(long value){
        this.last = value;
    }

    public synchronized long getLastValueAndReset(){
        var currentLast = this.last;
        this.last = 0;
        return currentLast;
    }

}
