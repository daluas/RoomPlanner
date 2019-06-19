CREATE TABLE oauth_access_token (
    token_id character varying(255),
    token bytea,
    authentication_id character varying(255),
    user_name character varying(255),
    client_id character varying(255),
    authentication bytea,
    refresh_token character varying(255)
);

CREATE TABLE oauth_client_details (
    client_id character varying(256) NOT NULL,
    resource_ids character varying(256),
    client_secret character varying(256),
    scope character varying(256),
    authorized_grant_types character varying(256),
    web_server_redirect_uri character varying(256),
    authorities character varying(256),
    access_token_validity integer,
    refresh_token_validity integer,
    additional_information character varying(4096),
    autoapprove character varying(256)
);


CREATE TABLE oauth_client_token (
    token_id character varying(255),
    token bytea,
    authentication_id character varying(255),
    user_name character varying(255),
    client_id character varying(255)
);


CREATE TABLE oauth_code (
    code character varying(255),
    authentication bytea
);

CREATE TABLE oauth_refresh_token (
    token_id character varying(255),
    token bytea,
    authentication bytea
);

ALTER TABLE ONLY public.oauth_client_details
    ADD CONSTRAINT oauth_client_details_pkey PRIMARY KEY (client_id);


-- Needed because I encode the password using bcrypt
ALTER TABLE users ALTER COLUMN password TYPE VARCHAR(100);