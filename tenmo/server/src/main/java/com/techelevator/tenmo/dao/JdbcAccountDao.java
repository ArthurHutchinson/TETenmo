package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcAccountDao (DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Account createAccount(Account newAccount) {
        String sql = "";
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class);
        newAccount.setAccountId(newId);
        return newAccount;
    }



}
