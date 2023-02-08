package ru.otus.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
public class ClientResultSetExtractorClass implements ResultSetExtractor<List<Client>> {

    @Override
    public List<Client> extractData(ResultSet rs) throws SQLException, DataAccessException {
        var clientList = new ArrayList<Client>();
        String prevClientId = null;
        while (rs.next()) {
            var clientId = rs.getString("client_id");
            Client client = null;
            if (prevClientId == null || !prevClientId.equals(clientId)) {
                client = new Client(
                        clientId,
                        rs.getString("client_name"),
                        new Address(clientId, rs.getString("address_street")),
                        new HashSet<>(),
                        false);
                clientList.add(client);
                prevClientId = clientId;
            } else if (clientId != null) {
                Optional<Client> first = clientList.stream()
                        .filter(c -> clientId.equals(c.getId()))
                        .findFirst();
                client = first.orElse(null);
            }
            Long phoneId = (Long) rs.getObject("phone_id");
            if (client != null && phoneId != null) {
                client.getPhones().add( new Phone(phoneId, rs.getString("phone_number"), clientId) );
            }
        }
        return clientList;
    }
}
