package bip.common.batch.writer;

import bip.common.batch.listener.ReturnKeyItemWriterListener;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.jdbc.core.namedparam.*;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;

/**
 * Created by ramezani on 9/30/2017.
 */
public class ReturnKeyItemWriter<T> implements ItemWriter<T>,InitializingBean {
    private static Logger logger = LoggerFactory.getLogger(ReturnKeyItemWriter.class);

    DataSource dataSource;
    String sql;
    String idColumn;
    BatchErrorHandler<T> batchErrorHandler;

    protected NamedParameterJdbcOperations namedParameterJdbcOperations;

    ReturnKeyItemWriterListener<T> listener;

    public void write(List<? extends T> list) throws Exception {
        for (T entity:list){
            Object pkValue =writeAndReturnKey(entity,sql);
            if(pkValue!=null) {
                if(entity instanceof Map){
                    ((Map) entity).put(idColumn,pkValue);
                }else {
                    entity.getClass().getMethod("set" + WordUtils.capitalize(idColumn), int.class).invoke(entity, pkValue);
                }
            }
        }
    }

    protected Object writeAndReturnKey(T entity,String sql) {
        logger.debug("writeAndReturnKey:"+sql+"["+entity.toString()+"]");
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            if(listener!=null)listener.beforeWrite(entity);

            if(entity instanceof Map){
                namedParameterJdbcOperations.update(sql, new MapSqlParameterSource((Map<String, ?>) entity), keyHolder,new String[]{idColumn});
            }else{
                namedParameterJdbcOperations.update(sql, new BeanPropertySqlParameterSource(entity), keyHolder,new String[]{idColumn});
            }

            if(listener!=null)listener.afterWrite(entity);
        }catch(Throwable t){
            logger.error("error in writeAndReturnKey of :"+entity+" , sql=["+sql+"]");
            if(listener!=null)listener.onWriteError(t,entity);
            if(batchErrorHandler !=null){
                batchErrorHandler.handleError(entity,t);
            }else{
                throw new RuntimeException(t);
            }
            return 0;
        }
        //return keyHolder.getKey().intValue();
        if(keyHolder.getKeys()!=null && keyHolder.getKeys().size()>0){
            return keyHolder.getKeys().values().iterator().next();
        }else if(keyHolder.getKey()!=null){
            return keyHolder.getKey();
        }else{
            return null;
        }
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.dataSource, "A dataSource is required.");
        Assert.notNull(this.sql, "An SQL statement is required.");
        Assert.notNull(this.idColumn, "A dataSource is required.");

        namedParameterJdbcOperations = new NamedParameterJdbcTemplate(dataSource);
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        namedParameterJdbcOperations = new NamedParameterJdbcTemplate(dataSource);
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public void setIdColumn(String idColumn) {
        this.idColumn = idColumn;
    }


    public void setBatchErrorHandler(BatchErrorHandler<T> batchErrorHandler) {
        this.batchErrorHandler = batchErrorHandler;
    }

    public void setListener(ReturnKeyItemWriterListener<T> listener) {
        this.listener = listener;
    }
}
