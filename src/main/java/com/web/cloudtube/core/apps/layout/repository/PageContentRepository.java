package com.web.cloudtube.core.apps.layout.repository;

import com.web.cloudtube.core.apps.layout.entity.PageContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PageContentRepository extends JpaRepository<PageContent, PageContent.WidgetId> {
    List<PageContent> findByIdWidgetId(String widgetId);
    @Query("from PageContent c left join PageLayoutWidget w on \n" +
            " c.id.widgetId = w.widgetId or c.parent = w.widgetId \n" +
            " where w.appId = :appId and w.pageId = :pageId or w.pageId = 'any'")
    List<PageContent> findByMetadata(@Param(value = "appId") String appId, @Param(value = "pageId") String pageId);
}
