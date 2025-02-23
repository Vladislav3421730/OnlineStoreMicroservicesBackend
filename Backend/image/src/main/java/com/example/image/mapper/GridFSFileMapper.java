package com.example.image.mapper;

import com.example.image.dto.MetaDataDto;
import com.mongodb.client.gridfs.model.GridFSFile;

public class GridFSFileMapper {

    private static final String KEY_CONTENT_TYPE = "_contentType";

    public static MetaDataDto map(GridFSFile gridFSFile) {
        return MetaDataDto.builder()
                .id(gridFSFile.getId().asObjectId().getValue().toHexString())
                .uploadAt(gridFSFile.getUploadDate())
                .size(gridFSFile.getChunkSize())
                .length(gridFSFile.getLength())
                .filename(gridFSFile.getFilename())
                .type(gridFSFile.getMetadata().getString(KEY_CONTENT_TYPE))
                .build();
    }
}
