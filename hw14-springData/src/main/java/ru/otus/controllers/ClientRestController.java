package ru.otus.controllers;

import org.springframework.web.bind.annotation.*;
import ru.otus.model.Client;
import ru.otus.service.ClientService;

import java.util.List;

@RestController
public class ClientRestController {

    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/api/client/{id}")
    public Client getClientById(@PathVariable(name = "id") String id) {
        return clientService.getClient(id);
    }

    @GetMapping("/api/client")
    public Client getClientByName(@RequestParam(name = "name") String name) {
        return clientService.findByName(name).orElse(null);
    }

    @GetMapping("/api/clients")
    public List<Client> getClients() {
        return clientService.findAll();
    }

    @PostMapping("/api/client")
    public Client saveClient(@RequestBody Client client) {
        return clientService.saveClient(client);
    }
}
