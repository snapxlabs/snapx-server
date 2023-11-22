package com.digcoin.snapx.core.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.core.handlers.StrictFill;
import com.digcoin.snapx.core.web.CurrentUser;
import com.digcoin.snapx.core.web.CurrentUserProvider;
import org.apache.ibatis.reflection.MetaObject;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class SaveOrUpdateMetaObjectHandler implements MetaObjectHandler {

    private final CurrentUserProvider currentUserProvider;

    public SaveOrUpdateMetaObjectHandler(CurrentUserProvider currentUserProvider) {
        this.currentUserProvider = currentUserProvider;
    }

    @Override
    public void insertFill(MetaObject metaObject) {
        LocalDateTime now = LocalDateTime.now();
        long currentUserId = getCurrentUserId();
        List<StrictFill<?, ?>> strictFillList = Arrays.asList(
                StrictFill.of("createTime", LocalDateTime.class, now),
                StrictFill.of("updateTime", LocalDateTime.class, now),
                StrictFill.of("createBy", Long.class, currentUserId),
                StrictFill.of("updateBy", Long.class, currentUserId)
        );
        this.strictInsertFill(findTableInfo(metaObject), metaObject, strictFillList);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        long currentUserId = getCurrentUserId();
        this.strictUpdateFill(findTableInfo(metaObject), metaObject, Arrays.asList(
                StrictFill.of("updateTime", LocalDateTime.class, LocalDateTime.now()),
                StrictFill.of("updateBy", Long.class, currentUserId)
        ));
    }

    private long getCurrentUserId() {
        if (this.currentUserProvider != null) {
            return this.currentUserProvider.getCurrentUser().map(CurrentUser::getId).orElse(0L);
        } else {
            return 0L;
        }
    }
}
