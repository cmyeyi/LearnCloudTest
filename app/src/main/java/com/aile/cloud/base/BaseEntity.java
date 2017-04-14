package com.aile.cloud.base;


import com.aile.www.basesdk.base.CoreBaseEntity;

public class BaseEntity extends CoreBaseEntity {

    @Override
    public boolean equals(Object o) {
        if (o instanceof BaseEntity) {
            BaseEntity oEntity = (BaseEntity) o;
            return this.objectId.equals(oEntity.objectId);
        }
        return super.equals(o);
    }
}