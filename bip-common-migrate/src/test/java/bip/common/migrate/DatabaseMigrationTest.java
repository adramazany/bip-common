package bip.common.migrate;

import org.apache.commons.dbcp.BasicDataSourceFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

class DatabaseMigrationTest {

    DatabaseMigration dm =  new DatabaseMigration();

    /**
     create user test identified by test;
     grant connect, resource, create view, create procedure to test;
     */
    @org.junit.jupiter.api.Test
    void migrateOracle() throws Exception {
        System.out.println("Test migrate oracle ...");
        Properties cfg=new Properties();
        cfg.setProperty("driverClassName", "org.postgresql.Driver");
        cfg.setProperty("url", "jdbc:oracle:thin:@87.248.145.7:1521:XE");
        cfg.setProperty("username", "test");
        cfg.setProperty("password", "test");
        cfg.setProperty("maxActive", "20");
        DataSource ds = BasicDataSourceFactory.createDataSource(cfg);
//        new DatabaseMigration("oracle-databasemigration-master.xml",ds);
        dm.migrate_unsafe("oracle-databasemigration-master.xml",ds.getConnection());
    }

    /**
     drop schema test cascade;
     drop user test;
     create schema test;
     create user test with password 'test';
     GRANT ALL ON schema test to test;

     password_encryption to md5 in postgresql.conf
     scram-sha-256 to md5 in pg_hba.conf
     */
    @org.junit.jupiter.api.Test
    void migratePostgres() throws ClassNotFoundException, SQLException {
        System.out.println("Test migrate postgres ...");
        Class.forName("org.postgresql.Driver");
        Connection cn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","test","test");
        dm.migrate("postgres-databasemigration-master.xml",cn);
        System.out.println("Test migrate postgres succeed.");
    }
}