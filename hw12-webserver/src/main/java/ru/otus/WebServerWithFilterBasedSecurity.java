package ru.otus;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
import ru.otus.core.repository.ClientDao;
import ru.otus.core.repository.ClientDaoImpl;
import ru.otus.core.repository.DataTemplateHibernate;
import ru.otus.core.repository.HibernateUtils;
import ru.otus.core.sessionmanager.TransactionManagerHibernate;
import ru.otus.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.crm.model.Address;
import ru.otus.crm.model.Client;
import ru.otus.crm.model.Phone;
import ru.otus.server.ClientWebServer;
import ru.otus.server.ClientWebServerWithFilterBasedSecurity;
import ru.otus.services.ClientAuthService;
import ru.otus.services.ClientAuthServiceImpl;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.TemplateProcessorImpl;

/**
 * Полезные ссылки:
 *     Стартовая страница - http://localhost:8080
 *     Стартовая логина   - http://localhost:8080/login
 *     Страница клиентов  - http://localhost:8080/clients
 *     REST сервис        - http://localhost:8080/api/client/3
 */
public class WebServerWithFilterBasedSecurity {

    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";

    public static void main(String[] args) throws Exception {

        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory = HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        var clientTemplate = new DataTemplateHibernate<>(Client.class);

        ClientDao clientDao = new ClientDaoImpl(transactionManager, clientTemplate);
        Gson gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
        ClientAuthService authService = new ClientAuthServiceImpl(clientDao);

        ClientWebServer clientWebServer = new ClientWebServerWithFilterBasedSecurity(WEB_SERVER_PORT,
                authService, clientDao, gson, templateProcessor);

        clientWebServer.start();
        clientWebServer.join();
    }
}
