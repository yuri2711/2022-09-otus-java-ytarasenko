package ru.otus.core.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.sessionmanager.TransactionManager;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;

import java.util.List;
import java.util.Optional;

public class ClientDaoImpl implements ClientDao {

    private static final Logger log = LoggerFactory.getLogger(ClientDaoImpl.class);

    private final DataTemplate<Client> clientDataTemplate;
    private final TransactionManager transactionManager;


    public ClientDaoImpl(TransactionManager transactionManager, DataTemplate<Client> clientDataTemplate) {
        this.transactionManager = transactionManager;
        this.clientDataTemplate = clientDataTemplate;


        save(new Client(null, "Крис Гир", "client1", "11111", new Address(null, "Street1"),
                List.of(new Phone(null, "number1"), new Phone(null, "number2"))));
        save(new Client(null, "Ая Кэш", "client2", "11111", new Address(null, "Street2"), List.of(new Phone(null, "9222222222"))));
        save(new Client(null, "Десмин Боргес", "client3", "11111", new Address(null, "Street3"), List.of(new Phone(null, "9333333333"))));
        save(new Client(null, "Кетер Донохью", "client4", "11111", new Address(null, "Street4"), List.of(new Phone(null, "9444444444"))));
        save(new Client(null, "Стивен Шнайдер", "client5", "11111", new Address(null, "Street5"), List.of(new Phone(null, "9555555555"))));
        save(new Client(null, "Джанет Вэрни", "client6", "11111", new Address(null, "Street6"), List.of(new Phone(null, "9666666666"))));
        save(new Client(null, "Брэндон Смит", "client7", "11111", new Address(null, "Street7"), List.of(new Phone(null, "9777777777"))));
    }


    @Override
    public Optional<Client> findById(long id) {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientOptional = clientDataTemplate.findById(session, id);
            log.info("clientById: {}", clientOptional);
            return clientOptional;
        });
    }

    @Override
    public List<Client> findByLogin(String login) {
        return transactionManager.doInReadOnlyTransaction(session ->
                clientDataTemplate.findByEntityField(session, "login", login)
        );
    }

    @Override
    public Client save(Client client) {
        return transactionManager.doInTransaction(session -> {
            var clientCloned = client.clone();

            if (client.getId() == null) {
                clientDataTemplate.insert(session, clientCloned);
                log.info("created client: {}", clientCloned);
                return clientCloned;
            }
            clientDataTemplate.update(session, clientCloned);
            log.info("updated client: {}", clientCloned);
            return clientCloned;
        });
    }

    @Override
    public List<Client> findAll() {
        return transactionManager.doInReadOnlyTransaction(session -> {
            var clientList = clientDataTemplate.findAll(session);
            log.info("clientList:{}", clientList);
            return clientList;
       });
    }
}
