package bip.common.util;

import java.util.Properties;

/**
 * Created by ramezani on 1/3/2019.
 */
public class DataSourceProperties extends Properties {
    Properties connectionProperties;
    private final DataSourceProperties _this = this;
    Properties defaults=new Properties(){
        @Override
        public synchronized Object put(Object key, Object value) {
            super.put(key, value);
            return _this.put(key, value);
        }
    };

    public DataSourceProperties(){
        setConnectionProperties(new Properties());
    }

    public Properties getDefaults() {
        return defaults;
    }

    public void setDefaults(Properties defaults) {
        //this.defaults = defaults;
        for (Object key:defaults.keySet()) {
            this.defaults.put(key,defaults.get(key));
        }
    }

    public Properties getConnectionProperties() {
        return connectionProperties;
    }

    public void setConnectionProperties(Properties connectionProperties) {
        this.connectionProperties = connectionProperties;
        put("connectionProperties",connectionProperties);
    }


}
