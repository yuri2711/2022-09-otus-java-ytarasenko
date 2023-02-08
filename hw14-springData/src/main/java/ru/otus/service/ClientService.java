package ru.otus.service;

import ru.otus.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientService {

    Client saveClient(Client client);

    Client getClient(String id);

    Optional<Client> findByName(String name);

    List<Client> findAll();
}
