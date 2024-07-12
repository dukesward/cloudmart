package com.web.cloudtube.core.apps.layout.repository;

import com.web.cloudtube.core.apps.layout.entity.PageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PageMetadataRepository extends JpaRepository<PageMetadata, PageMetadata.PageId> {
    List<PageMetadata> findByIdPageIdAndIdAppId(String appId, String pageId);
}
