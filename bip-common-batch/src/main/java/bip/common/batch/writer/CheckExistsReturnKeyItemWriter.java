package bip.common.batch.writer;

import bip.common.batch.exceptions.CouldNotCheckExistsException;
import bip.common.batch.listener.CheckExistsReturnKeyItemWriterListener;
import bip.common.util.NVL;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.BeanUtils;
import org.springframework.util.Assert;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ramezani on 10/5/2017.
 */
public class CheckExistsReturnKeyItemWriter<T extends CheckExists> extends ReturnKeyItemWriter<T> {
    private static Logger logger = LoggerFactory.getLogger(CheckExistsReturnKeyItemWriter.class);


    String sqlCheckExists;
    String sqlCheckExistsINparameterName;
    String searchColumn;
    /*boolean removeExists=false;*/
    String sqlUpdate;

    ItemWriter<T> updateItemWriter;

    CheckExistsReturnKeyItemWriterListener listener;

    static int ORA_01795_maximum_number_of_expressions_is_1000=999;

    @Override
    public void write(List<? extends T> list) throws Exception {
        fillExistsEntitiesId(list);

        List<T> existsEntities = new ArrayList<T>();
        List<T> notExistsEntities = new ArrayList<T>();
        splitExistsAndNotExistsEntities(list,existsEntities,notExistsEntities);

        if(listener!=null){
            listener.afterSplit(list,existsEntities,notExistsEntities);
        }

        super.write(notExistsEntities);

        if(updateItemWriter !=null && existsEntities.size()>0){
            updateItemWriter.write(existsEntities);
            if(listener!=null)listener.afterOverwrite(existsEntities);
        }

    }

    //private void splitExistsAndNotExistsEntities(List<? extends T> srcList,List<T> existsList,List<T> notExistsList) throws CouldNotCheckExistsException {
    private void splitExistsAndNotExistsEntities(List<? extends T> srcList,List existsList,List notExistsList) throws CouldNotCheckExistsException {

        try {
            for (int i = srcList.size()-1; i >=0 ; i--) {
                //T entity = srcList.get(i);
                Object entity = srcList.get(i);
                if(entity!=null) {
                    Object id = getEntityIdValue(entity);
                    boolean exists = getEntityExists(entity);
                    if (id == null || !exists){// || NVL.getInt(id) == 0) {
                        notExistsList.add(entity);
                    }else{
                        existsList.add(entity);
                    /*}else if(removeExists){
                        list.remove(i);*/
                    }
                }
            }
        } catch (NoSuchMethodException e) {
            throw new CouldNotCheckExistsException("error in getting entities idColumn value !",e);
        } catch (IllegalAccessException e) {
            throw new CouldNotCheckExistsException("error in getting entities idColumn value !",e);
        } catch (InvocationTargetException e) {
            throw new CouldNotCheckExistsException("error in getting entities idColumn value !",e);
        }
    }

    private void fillExistsEntitiesId(List<? extends T> entities) throws CouldNotCheckExistsException {
        try {
            List<String> searchColumnValues = new ArrayList<String>();
            //Map<String,T> mapEntities=new HashMap<String, T>();
            Map<String,Object> mapEntities=new HashMap<String, Object>();
            //for (T entity:entities){
            for(int i=0;i<entities.size();i++){
                //T entity=entities.get(i);
                Object entity=entities.get(i);
                if(entity!=null) {
                    String key = NVL.getString(getEntitySearchColumnValue(entity));
                    if(!mapEntities.containsKey(key)) {
                        mapEntities.put(key, entity);
                        searchColumnValues.add("'"+key.replaceAll("'","''")+"'");
                    }
                }
            }

            if(searchColumnValues.size()>0){
                Connection cn=null;
                Statement stmt=null;
                ResultSet rs=null;
                try {
                    String[] arSearchColumnValues = searchColumnValues.toArray(new String[0]);
                    cn = dataSource.getConnection();
                    stmt = cn.createStatement();

                    for (int i = 0; i < searchColumnValues.size(); i+=ORA_01795_maximum_number_of_expressions_is_1000) {
                        int startIndex=i;
                        int endIndex = Math.min(searchColumnValues.size(),i+ORA_01795_maximum_number_of_expressions_is_1000);
                        String sqlWithParameters =
                                sqlCheckExists.replace(":" + sqlCheckExistsINparameterName
                                        , StringUtils.join(arSearchColumnValues, ",",startIndex,endIndex));
                        //String sqlWithParameters = AIPUtil.replaceAllString(sqlCheckExists,":"+sqlCheckExistsINparameterName,StringUtils.join(searchColumnValues,",").replace("\\","\\\\")  );
                        logger.debug("sqlWithParameters-"+i+"=" + sqlWithParameters);

                        rs = stmt.executeQuery(sqlWithParameters);
                        while (rs.next()) {
                            String searchColumnValue = rs.getString(searchColumn);
                            //T entity = mapEntities.get(searchColumnValue);
                            Object entity = mapEntities.get(searchColumnValue);

                            //entity.getClass().getMethod("set" + WordUtils.capitalize(idColumn),Object.class).invoke(entity, idValue);

                            if(entity instanceof Map){
                                ((Map)entity).put(idColumn,rs.getObject(idColumn));
                                ((Map)entity).put("exists",true);
                            }else{
                                Object idValueOld = PropertyUtils.getProperty(entity,idColumn);
                                if(idValueOld instanceof String){
                                    PropertyUtils.setProperty(entity,idColumn,rs.getString(idColumn));
                                }else if(idValueOld instanceof Integer) {
                                    PropertyUtils.setProperty(entity, idColumn, rs.getInt(idColumn));
                                }else if(idValueOld instanceof Boolean) {
                                    PropertyUtils.setProperty(entity, idColumn, rs.getBoolean(idColumn));
                                }else if(idValueOld instanceof Double) {
                                    PropertyUtils.setProperty(entity, idColumn, rs.getDouble(idColumn));
                                }else if(idValueOld instanceof Long) {
                                    PropertyUtils.setProperty(entity, idColumn, rs.getLong(idColumn));
                                }else {
                                    PropertyUtils.setProperty(entity, idColumn, rs.getObject(idColumn));
                                }
                                ((CheckExists)entity).setExists(true);
                            }


                        }
                        rs.close();
                    }
                }catch(Exception ex){
                    throw new CouldNotCheckExistsException("error in fetching exists entities",ex);
                }finally{
                    if(rs!=null)try{rs.close();}catch(SQLException e){}
                    if(stmt!=null)try{stmt.close();}catch(SQLException e){}
                    if(cn!=null)try{cn.close();}catch(SQLException e){}
                }
            }

        } catch (NoSuchMethodException e) {
            throw new CouldNotCheckExistsException("error in getting entities searchColumn value or setting entity id value! ",e);
        } catch (InvocationTargetException e) {
            throw new CouldNotCheckExistsException("error in getting entities searchColumn value or setting entity id value!",e);
        } catch (IllegalAccessException e) {
            throw new CouldNotCheckExistsException("error in getting entities searchColumn value or setting entity id value!",e);
        }

    }

    private Object getEntitiesSearchColumnValues(List<? extends T> list) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<Object> values=new ArrayList<Object>();
        for (T entity:list) {
            if(entity!=null){
                values.add( getEntitySearchColumnValue(entity) );
            }
        }
        return StringUtils.join(values,",");
    }
    private Object getEntitySearchColumnValue(Object entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(entity instanceof Map){
            return ((Map)entity).get(searchColumn) ;
        }else{
            return entity.getClass().getMethod("get"+ WordUtils.capitalize(searchColumn)).invoke(entity) ;
        }
    }
/*
    private Object getEntitySearchColumnValue(T entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(entity instanceof Map){
            return ((Map)entity).get(searchColumn) ;
        }else{
            return entity.getClass().getMethod("get"+ WordUtils.capitalize(searchColumn)).invoke(entity) ;
        }
    }
*/
    private Object getEntityIdValue(Object entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(entity instanceof Map){
            return ((Map)entity).get(idColumn) ;
        }else {
            return entity.getClass().getMethod("get" + WordUtils.capitalize(idColumn)).invoke(entity);
        }
    }
    private boolean getEntityExists(Object entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(entity instanceof Map){
            return NVL.getBool( ((Map)entity).get("exists")) ;
        }else {
            return NVL.getBool( entity.getClass().getMethod("get" + WordUtils.capitalize("exists")).invoke(entity));
        }
    }
/*
    private Object getEntityIdValue(T entity) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(entity instanceof Map){
            return ((Map)entity).get(idColumn) ;
        }else {
            return entity.getClass().getMethod("get" + WordUtils.capitalize(idColumn)).invoke(entity);
        }
    }
*/

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(this.sqlCheckExists, "An sqlCheckExists statement is required.");
        Assert.notNull(this.searchColumn, "A searchColumn is required.");
        Assert.notNull(this.sqlCheckExistsINparameterName, "A sqlCheckExistsINparameterName is required.");
        if(sqlUpdate!=null && updateItemWriter ==null){
            initUpdateJdbcBatchItemWriter();
        }
        super.afterPropertiesSet();
    }

    public void setSqlCheckExists(String sqlCheckExists) {
        this.sqlCheckExists = sqlCheckExists;
    }

    public void setSearchColumn(String searchColumn) {
        this.searchColumn = searchColumn;
    }

    public void setSqlCheckExistsINparameterName(String sqlCheckExistsINparameterName) {
        this.sqlCheckExistsINparameterName = sqlCheckExistsINparameterName;
    }

    /*public void setRemoveExists(boolean removeExists) {
        this.removeExists = removeExists;
    }*/

    public void setSqlUpdate(String sqlUpdate) {
        this.sqlUpdate = sqlUpdate;
    }

    public void setUpdateItemWriter(ItemWriter<T> updateItemWriter) {
        this.updateItemWriter = updateItemWriter;
    }

    private void  initUpdateJdbcBatchItemWriter(){
        updateItemWriter =new JdbcBatchItemWriter<T>();
        ((JdbcBatchItemWriter)updateItemWriter).setDataSource(this.dataSource);
        ((JdbcBatchItemWriter)updateItemWriter).setSql(sqlUpdate);
        ((JdbcBatchItemWriter)updateItemWriter).setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<T>());
        ((JdbcBatchItemWriter)updateItemWriter).afterPropertiesSet();
    }

    public void setListener(CheckExistsReturnKeyItemWriterListener listener) {
        this.listener = listener;
        super.setListener(listener);
    }
}
