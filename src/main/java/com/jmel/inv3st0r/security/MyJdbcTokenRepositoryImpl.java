// The following code is modified from https://github.com/lizard-buzzard/persistent-token-rememberme-authentication/blob/master/README.md
package com.jmel.inv3st0r.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;

public class MyJdbcTokenRepositoryImpl extends JdbcTokenRepositoryImpl {
    private final Logger LOGGER = LoggerFactory.getLogger(MyJdbcTokenRepositoryImpl.class);
    public static final String CREATE_TABLE_SQL = "create table persistent_logins (username varchar(64) not null, series varchar(64) primary key, token varchar(64) not null, last_used timestamp not null)";

    public MyJdbcTokenRepositoryImpl() {
        super();
    }

    @Override
    protected void initDao() {
        try {
            assert super.getJdbcTemplate() != null;
            super.getJdbcTemplate().execute(CREATE_TABLE_SQL);
        } catch (DataAccessException e) {
            LOGGER.info("Table persistent_logins have been already created");
        }
    }
}
