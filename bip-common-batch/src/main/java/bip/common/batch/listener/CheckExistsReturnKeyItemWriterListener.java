package bip.common.batch.listener;

import java.util.List;

/**
 * Created by ramezani on 2/3/2018.
 */
public interface CheckExistsReturnKeyItemWriterListener<T> extends ReturnKeyItemWriterListener<T>{

     void afterSplit(List<? extends T> list, List<T> existsEntities, List<T> notExistsEntities);

     void afterOverwrite(List<T> existsEntities);
}
