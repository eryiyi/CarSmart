package com.xiaolong.CarSmart.dao;

import android.database.sqlite.SQLiteDatabase;
import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import java.util.Map;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig shoppingCartDaoConfig;

    private final ShoppingCartDao shoppingCartDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        shoppingCartDaoConfig = daoConfigMap.get(ShoppingCartDao.class).clone();
        shoppingCartDaoConfig.initIdentityScope(type);

        shoppingCartDao = new ShoppingCartDao(shoppingCartDaoConfig, this);

        registerDao(ShoppingCart.class, shoppingCartDao);
    }
    
    public void clear() {
        shoppingCartDaoConfig.getIdentityScope().clear();
    }

    public ShoppingCartDao getShoppingCartDao() {
        return shoppingCartDao;
    }

}