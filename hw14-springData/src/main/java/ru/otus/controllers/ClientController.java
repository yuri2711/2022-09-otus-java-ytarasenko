package ru.otus.controllers;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.otus.model.Client;
import ru.otus.service.ClientService;

import java.util.List;

@Controller
public class ClientController {

    private final String osData;
    private final ClientService clientService;

    public ClientController(@Value("OS: #{T(System).getProperty(\"os.name\")}, " +
                                   "JDK: #{T(System).getProperty(\"java.runtime.version\")}")
                                    String osData,
                            ClientService clientService) {
        this.osData = osData;
        this.clientService = clientService;
    }

    @GetMapping("/")
    public String clientsListView(Model model) {
        List<Client> clients = clientService.findAll();
        model.addAttribute("clients", clients);
        model.addAttribute("osData", osData);
        return "index";
    }
}
