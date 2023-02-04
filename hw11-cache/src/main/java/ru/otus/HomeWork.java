package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.*;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {

        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<Client>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient); //реализация DataTemplate, универсальная
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);

        List<Long> ids = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            ids.add(dbServiceClient
                    .saveClient(new Client("client_" + i))
                    .getId());

            /*if (i % 100 == 0) {
                System.out.println("Cache size: " + dbServiceClient.cacheSize());
            }*/
        }
        /**
         * Во время наполнения кэша наблюдал интересную вещь в выводе println()
         * В первый раз размер кэша сбросился примерно на 2000 элементов,
         * затем как будто "растянулся" и второй сброс кэша произошел около 23300 элементов
         */

        long start = System.currentTimeMillis();

        for (long i : ids) {
            dbServiceClient.getClient(i);

            /*if (i % 100 == 0) {
                System.out.println("Cache size: " + dbServiceClient.cacheSize());
            }*/
        }
        log.info("duration WITH cache. Millis:{}", System.currentTimeMillis() - start);


        /**
         * При работе с кэшом выигрыш во времени получал всегда. Но результат зависит от количества элементов.
         * На моем железе 16ГБ ОЗУ результаты следующие:
         *
         *  - при количестве элементов 100
         *  {
         *      12:31:04.787 [main] INFO ru.otus.HomeWork - duration WITHOUT cache. Millis:29
         *      12:31:04.787 [main] INFO ru.otus.HomeWork - duration WITH cache. Millis:0
         *  }
         *
         *  - при количестве элементов 2000
         *  {
         *      12:29:26.001 [main] INFO ru.otus.HomeWork - duration WITHOUT cache. Millis:389
         *      12:29:26.005 [main] INFO ru.otus.HomeWork - duration WITH cache. Millis:4
         *  }
         *
         *  - при количестве элементов 10000
         *  {
         *      12:27:41.776 [main] INFO ru.otus.HomeWork - duration WITHOUT cache. Millis:1335
         *      12:27:41.782 [main] INFO ru.otus.HomeWork - duration WITH cache. Millis:6
         *  }
         *
         *  - при количестве элементов 20000
         *  {
         *      12:32:04.785 [main] INFO ru.otus.HomeWork - duration WITHOUT cache. Millis:2184
         *      12:32:05.311 [main] INFO ru.otus.HomeWork - duration WITH cache. Millis:526
         *  }
         *
         *  - при количестве элементов 40000
         *  {
         *      12:47:37.135 [main] INFO ru.otus.HomeWork - duration WITHOUT cache. Millis:4470
         *      12:47:37.148 [main] INFO ru.otus.HomeWork - duration WITH cache. Millis:13
         *  }
         */
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
