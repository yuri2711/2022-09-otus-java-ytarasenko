package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> messageStore = new HashMap<>();


    @Override
    public void onUpdated(Message msg) {
        final var field13 = new ObjectForMessage();
        final var data = new ArrayList<String>();
        data.addAll(msg.getField13().getData());
        field13.setData(data);
        var message = new Message.Builder(msg.getId())
                .field1(msg.getField1())
                .field2(msg.getField2())
                .field3(msg.getField3())
                .field4(msg.getField4())
                .field5(msg.getField5())
                .field6(msg.getField6())
                .field7(msg.getField7())
                .field8(msg.getField8())
                .field9(msg.getField9())
                .field10(msg.getField10())
                .field11(msg.getField11())
                .field12(msg.getField12())
                .field13(field13)
                .build();

        messageStore.put(msg.getId(), message);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.ofNullable(messageStore.get(id));
    }
}
