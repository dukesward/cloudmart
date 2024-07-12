package com.web.cloudtube.core.apps.layout.repository;

import com.web.cloudtube.core.apps.layout.entity.PageLayoutWidget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

public interface PageLayoutWidgetRepository extends JpaRepository<PageLayoutWidget, String> {
    List<PageLayoutWidget> findByAppId(String appId);
    @Query(nativeQuery = true, value = "SELECT m.layout, m.theme, m.page_id, w.widget_id, w.parent, w.position, w.type, w.subtype, w.user_type FROM \n" +
            "page_metadata as m LEFT JOIN page_layout_widget as w\n" +
            "ON m.app_id = w.app_id AND (m.page_id = w.page_id OR w.page_id = :defaultVal)\n" +
            "WHERE m.app_id = :appId AND m.page_id = :pageId\n")
    List<Map<String, String>> findWidgetsByPageId(String appId, String pageId, String defaultVal);
}
