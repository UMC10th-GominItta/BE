ALTER TABLE mind_sessions
    ADD COLUMN worry_title VARCHAR(255) NOT NULL DEFAULT '' AFTER status;
