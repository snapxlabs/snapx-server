package com.digcoin.snapx.domain.infra.component;

import com.digcoin.snapx.domain.infra.entity.ResourceFile;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.time.format.DateTimeFormatter;

public abstract class AbstractFileStorage {

    protected String getFilePath(ResourceFile resourceFile) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        String date = resourceFile.getCreateTime().format(dateTimeFormatter);
        String fileExtension = resourceFile.getFileExtension();
        String filePath = String.join(geSeparator(), date, String.valueOf(resourceFile.getId()));
        if (StringUtils.isNotBlank(fileExtension)) {
            filePath = filePath + "." + fileExtension;
        }
        return filePath;
    }

    protected abstract String geSeparator();

}
