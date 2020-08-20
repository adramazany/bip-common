package bip.common.batch.handler;

import javax.sql.DataSource;

/**
 * Created by ramezani on 9/12/2019.
 */
public interface EntityErrorHandler<T> {
    void handleError(T entity, Throwable throwable);

    void setDataSource(DataSource dataSource);

//    void setEntity(T entity);

    public void setRetryErrorSql(String retryErrorSql) ;

    public void setExistsEntityErrorSql(String existsInjectionErrorSql) ;

//    T getEntity();
}
