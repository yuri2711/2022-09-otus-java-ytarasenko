package ru.otus.servlet;

import com.google.gson.Gson;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.otus.core.repository.ClientDao;
import ru.otus.crm.model.Client;

import java.io.IOException;
import java.util.stream.Collectors;


public class CreateClientsApiServlet extends HttpServlet {

    private final ClientDao clientDao;
    private final Gson gson;

    public CreateClientsApiServlet(ClientDao clientDao, Gson gson) {
        this.clientDao = clientDao;
        this.gson = gson;
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        ServletOutputStream out = response.getOutputStream();
        var client = clientDao.save(extractClientFromRequest(request));
    }

    private Client extractClientFromRequest(HttpServletRequest request) throws IOException {
        String collect = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        return gson.fromJson(collect, Client.class);
    }
}
