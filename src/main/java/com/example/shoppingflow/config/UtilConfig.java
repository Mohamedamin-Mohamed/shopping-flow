package com.example.shoppingflow.config;

import com.example.shoppingflow.property.UtilProperties;

public class UtilConfig {
    private final UtilProperties utilProperties;

    public UtilConfig(UtilProperties utilProperties){
        checkForEndpointOverride();
        this.utilProperties = utilProperties;
    }

    public String endpointOverride(){
        checkForEndpointOverride();
        return utilProperties.endpointOverride;
    }

    private void checkForEndpointOverride(){
        assert utilProperties != null;
        if(utilProperties.endpointOverride.isEmpty() || utilProperties.endpointOverride.isBlank()){
            throw new IllegalArgumentException("Property endpoint override should be set");
        }
    }

    public String getTableName(){
        return utilProperties.tableName;
    }
}
