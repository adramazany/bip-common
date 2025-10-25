package bip.common.migrate;

import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: adelramezani.jd@gmail.com
 * Date: 10/14/2025 8:34 PM
 */

public class DatabaseMigration {
    static final Logger logger = LoggerFactory.getLogger(DatabaseMigration.class);
    public DatabaseMigration(String changeLogPath, DataSource dataSource){
        logger.warn("DatabaseMigration initiating ... ");
        System.out.println("DatabaseMigration initiating ... ");
        try {
            migrate(changeLogPath, dataSource.getConnection());
        } catch (SQLException e) {
            logger.error("Migration failed! "+e.getMessage());
        }
    }
    public void migrate(String changeLogPath, Connection cn){
        UnsupportedVersionExceptionHandler handler = new UnsupportedVersionExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(handler);
        Thread t = new Thread(new ReflectionBasedMigration(cn,changeLogPath));
        t.setUncaughtExceptionHandler(handler);
        t.start();
        try {
            t.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        logger.info("Migrate succeed by :"+changeLogPath);
    }

    class ReflectionBasedMigration implements Runnable {
        Connection cn;
        String changeLogPath;
        public ReflectionBasedMigration(Connection cn, String changeLogPath) {
            this.cn = cn;
            this.changeLogPath = changeLogPath;
        }
        @Override
        public void run() {
            try {
                logger.info("ReflectionBasedMigration starting ...");
//                    liquibase.database.jvm.JdbcConnection jdbcCN = new liquibase.database.jvm.JdbcConnection(cn);
                Class<?> clazzJdbcConnection = Class.forName("liquibase.database.jvm.JdbcConnection");
                Constructor<?> ctorJdbcConnection = clazzJdbcConnection.getConstructor(Connection.class);
                liquibase.database.jvm.JdbcConnection jdbcCN = (liquibase.database.jvm.JdbcConnection) ctorJdbcConnection.newInstance(new Object[] { cn });

//                    liquibase.Liquibase liquibase = new liquibase.Liquibase(changeLogPath, new liquibase.resource.ClassLoaderResourceAccessor(), jdbcCN);
                Class<?> clazzLiquibase = Class.forName("liquibase.Liquibase");
                Constructor<?> ctorLiquibase = clazzLiquibase.getConstructor(String.class, liquibase.resource.ResourceAccessor.class, liquibase.database.DatabaseConnection.class);
                Object liquibase = ctorLiquibase.newInstance(new Object[] { changeLogPath, Class.forName("liquibase.resource.ClassLoaderResourceAccessor").newInstance(), jdbcCN});

//                liquibase.update(new liquibase.Contexts(), new liquibase.LabelExpression());
                Method update = clazzLiquibase.getMethod("update", liquibase.Contexts.class, liquibase.LabelExpression.class);
                update.invoke(liquibase, Class.forName("liquibase.Contexts").newInstance(), Class.forName("liquibase.LabelExpression").newInstance());
                logger.info("ReflectionBasedMigration on database succeed.");
            } catch (Exception e) {
                logger.error("ReflectionBasedMigration on database failed! :"+ e.getMessage(),e);
//                e.printStackTrace();
            }
        }
    }
    class UnsupportedVersionExceptionHandler implements Thread.UncaughtExceptionHandler{
        @Override
        public void uncaughtException(Thread thread, Throwable throwable) {
            if (throwable instanceof UnsupportedClassVersionError) {
                logger.error("Thread [" + thread.getName() + "] crashed due to class version mismatch! "+ throwable.getMessage());
                //System.exit(0); // ✅ graceful exit, prevents exit code 1
            } else {
                logger.error("UnsupportedVersionExceptionHandler!", throwable);
//                throwable.printStackTrace();
                System.exit(1);
            }
        }
    }
}
