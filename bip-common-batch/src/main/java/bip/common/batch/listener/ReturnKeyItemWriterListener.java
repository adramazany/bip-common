package bip.common.batch.listener;

import org.springframework.batch.core.StepListener;

/**
 * Created by ramezani on 2/3/2018.
 */
public interface ReturnKeyItemWriterListener<T> extends StepListener{
    void beforeWrite(T entity);

    void afterWrite(T entity);

    void onWriteError(Throwable t, T entity);
}
