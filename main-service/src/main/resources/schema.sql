CREATE TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50)                             NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    title  VARCHAR(50)                             NOT NULL,
    pinned BOOLEAN                                 NOT NULL,
    CONSTRAINT pk_compilations PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS compilations_events
(
    compilation_id BIGINT NOT NULL,
    events_id      BIGINT NOT NULL
);

CREATE TABLE IF NOT EXISTS event
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation         VARCHAR(2000)                           NOT NULL,
    category_id        BIGINT,
    created_on         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    description        VARCHAR(7000)                           NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    user_id            BIGINT,
    location_id        BIGINT,
    paid               BOOLEAN                                 NOT NULL,
    participant_limit  INTEGER                                 NOT NULL,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN                                 NOT NULL,
    state              INTEGER                                 NOT NULL,
    title              VARCHAR(255)                            NOT NULL,
    CONSTRAINT pk_event PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS location
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    lat FLOAT                                   NOT NULL,
    lon FLOAT                                   NOT NULL,
    CONSTRAINT pk_location PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS requests
(
    id       BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    user_id  BIGINT                                  NOT NULL,
    event_id BIGINT                                  NOT NULL,
    created  TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    status   INTEGER                                 NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email VARCHAR(254)                            NOT NULL,
    name  VARCHAR(250)                            NOT NULL,
    banned BOOLEAN,
    CONSTRAINT pk_users PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS comments
(
    id         BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    text       VARCHAR(1024)                           NOT NULL,
    author_id  BIGINT                                  NOT NULL,
    event_id   BIGINT                                  NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    edited_on  TIMESTAMP WITHOUT TIME ZONE,
    deleted_on TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_comments PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS claim
(
    id                BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    author_id         BIGINT                                  NOT NULL,
    comment_id        BIGINT                                  NOT NULL,
    created_on        TIMESTAMP WITHOUT TIME ZONE             NOT NULL,
    state             INTEGER                                 NOT NULL,
    allowed_on        TIMESTAMP WITHOUT TIME ZONE,
    CONSTRAINT pk_claim PRIMARY KEY (id)
);

ALTER TABLE IF EXISTS categories
    ADD CONSTRAINT uc_categories_name UNIQUE (name);

ALTER TABLE IF EXISTS compilations
    ADD CONSTRAINT uc_compilations_title UNIQUE (title);

ALTER TABLE IF EXISTS users
    ADD CONSTRAINT uc_users_email UNIQUE (email);

ALTER TABLE IF EXISTS users
    ADD CONSTRAINT uc_users_name UNIQUE (name);

ALTER TABLE IF EXISTS event
    ADD CONSTRAINT FK_EVENT_ON_CATEGORY FOREIGN KEY (category_id) REFERENCES categories (id);

ALTER TABLE IF EXISTS event
    ADD CONSTRAINT FK_EVENT_ON_LOCATION FOREIGN KEY (location_id) REFERENCES location (id);

ALTER TABLE IF EXISTS event
    ADD CONSTRAINT FK_EVENT_ON_USER FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS requests
    ADD CONSTRAINT FK_REQUESTS_ON_EVENT FOREIGN KEY (event_id) REFERENCES event (id);

ALTER TABLE IF EXISTS requests
    ADD CONSTRAINT FK_REQUESTS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);

ALTER TABLE IF EXISTS compilations_events
    ADD CONSTRAINT fk_comeve_on_compilation FOREIGN KEY (compilation_id) REFERENCES compilations (id) ON DELETE CASCADE;

ALTER TABLE IF EXISTS compilations_events
    ADD CONSTRAINT fk_comeve_on_event FOREIGN KEY (events_id) REFERENCES event (id);

ALTER TABLE IF EXISTS comments
    ADD CONSTRAINT FK_COMMENTS_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES users (id);

ALTER TABLE IF EXISTS comments
    ADD CONSTRAINT FK_COMMENTS_ON_EVENT FOREIGN KEY (event_id) REFERENCES event (id);

ALTER TABLE IF EXISTS claim
    ADD CONSTRAINT FK_CLAIM_ON_AUTHOR FOREIGN KEY (author_id) REFERENCES users (id);

ALTER TABLE IF EXISTS claim
    ADD CONSTRAINT FK_CLAIM_ON_COMMENT FOREIGN KEY (comment_id) REFERENCES comments (id);
