package bip.common.batch.writer;

import org.springframework.util.ErrorHandler;

import javax.sql.DataSource;

/**
 * Created by ramezani on 2/1/2018.
 */
public interface BatchErrorHandler<T> extends ErrorHandler {
    void handleError(T entity, Throwable throwable);

    void setDataSource(DataSource dataSource);

    //void setInjectionHolder(InjectionHolder injectionHolder);

    public void setRetryErrorSql(String retryErrorSql) ;

    public void setExistsInjectionErrorSql(String existsInjectionErrorSql) ;

}