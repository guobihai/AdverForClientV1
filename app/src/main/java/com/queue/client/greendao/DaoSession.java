package com.queue.client.greendao;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.queue.client.entry.HostNo;

import com.queue.client.greendao.HostNoDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig hostNoDaoConfig;

    private final HostNoDao hostNoDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        hostNoDaoConfig = daoConfigMap.get(HostNoDao.class).clone();
        hostNoDaoConfig.initIdentityScope(type);

        hostNoDao = new HostNoDao(hostNoDaoConfig, this);

        registerDao(HostNo.class, hostNoDao);
    }
    
    public void clear() {
        hostNoDaoConfig.clearIdentityScope();
    }

    public HostNoDao getHostNoDao() {
        return hostNoDao;
    }

}