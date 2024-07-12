INSERT IGNORE INTO page_content (widget_id, content_id, parent, type, value, update_by)
VALUES
       ('cloudtube-header-banner-title', 'header-banner-link', 'cloudtube-header-banner', 'link', 'dashboard_home', 'cms_admin'),
       ('cloudtube-header-authbox-signup', 'header-authbox-signup-label', 'cloudtube-header-authbox', 'label', 'Join now', 'cms_admin'),
       ('cloudtube-header-authbox-signup', 'header-authbox-signup-api', 'cloudtube-header-authbox', 'api', 'api_auth_signup', 'cms_admin'),
       ('cloudtube-header-authbox-signin', 'header-authbox-signin-label', 'cloudtube-header-authbox', 'label', 'Sign in', 'cms_admin'),
       ('cloudtube-header-authbox-signin', 'header-authbox-signin-api', 'cloudtube-header-authbox', 'api', 'api_auth_signin', 'cms_admin'),
       ('cloudtube-header-nav-item-home', 'header-nav-home-label', 'cloudtube-header-nav', 'label', 'Home'),

       ('cloudtube-auth-modal-signin', 'auth-modal-signin-top', 'cloudtube-header-authbox', 'label', 'Log in to see more', 'cms_mk_duke'),
       ('cloudtube-auth-modal-signin-form', 'auth-modal-signin-email', 'cloudtube-auth-modal-signin', 'label', 'Email', 'cms_mk_duke'),
       ('cloudtube-auth-modal-signin-form', 'auth-modal-signin-email-input', 'cloudtube-auth-modal-signin', 'label', 'Email', 'cms_mk_duke'),
;