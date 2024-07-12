INSERT IGNORE INTO page_layout_widget (widget_id, app_id, page_id, parent, position, subtype, type)
VALUES
    ('cloudtube-header-banner', 'dashboard', 'dashboard', null, 'header', 'header-banner', 'banner'),
    ('cloudtube-header-logo', 'dashboard', 'dashboard', 'cloudtube-header-banner', null, 'svg-logo', 'logo'),
    ('cloudtube-header-toolbelt', 'dashboard', 'dashboard', 'cloudtube-header-banner', null, 'banner-toolbelt', 'list'),
    ('cloudtube-header-auth-signup', 'dashboard', 'dashboard', 'cloudtube-header-auth', 'auth-box', 'auth-signup', 'button'),
    ('cloudtube-header-auth-signin', 'dashboard', 'dashboard', 'cloudtube-header-auth', 'auth-box', 'auth-signin', 'button'),
    ('cloudtube-header-authbox', 'dashboard', 'dashboard', 'cloudtube-header-banner', 'auth-box', 'auth-corner', 'auth'),

    ('cloudtube-auth-modal-signin', 'dashboard', 'dashboard', 'cloudtube-header-authbox', 'auth-modal', 'auth-modal', 'overlay'),
    ('cloudtube-auth-modal-signin-form', 'dashboard', 'dashboard', 'cloudtube-auth-modal-signin', 'auth-modal-body', 'auth-form', 'form')
;