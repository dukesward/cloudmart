/*CREATE [UNIQUE | FULLTEXT | SPATIAL] INDEX index_name
    [index_type]
    ON tbl_name (key_part,...)
    [index_option]
    [algorithm_option | lock_option] ...

key_part: {col_name [(length)] | (expr)} [ASC | DESC]

index_option: {
    KEY_BLOCK_SIZE [=] value
  | index_type
  | WITH PARSER parser_name
  | COMMENT 'string'
  | {VISIBLE | INVISIBLE}
  | ENGINE_ATTRIBUTE [=] 'string'
  | SECONDARY_ENGINE_ATTRIBUTE [=] 'string'
}

index_type:
    USING {BTREE | HASH}

algorithm_option:
    ALGORITHM [=] {DEFAULT | INPLACE | COPY}

lock_option:
    LOCK [=] {DEFAULT | NONE | SHARED | EXCLUSIVE}*/

CREATE UNIQUE INDEX uk_user_session_id ON user_session_data(session_id);
CREATE UNIQUE INDEX uk_user_auth_id ON user_session_data(auth_id);

CREATE INDEX uk_page_layout_widget_page_id ON page_layout_widget(page_id);

CREATE INDEX k_page_content_widget_id ON page_content(widget_id);
CREATE INDEX k_page_content_parent ON page_content(parent);
CREATE UNIQUE INDEX uk_page_content_properties ON page_content(content_id, type, value);