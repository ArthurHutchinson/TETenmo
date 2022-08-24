package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.LoginDTO;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;
    User user;
    LoginDTO loginDTO;

    public JdbcAccountDao (DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Account createAccount(Account newAccount) {
        String sql = "INSERT INTO account(user_id, balance)\n" +
                "VALUES(?, ?) RETURNING account_id;";
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class, newAccount.getUserId(), 1000);
        newAccount.setAccountId(newId);
        return newAccount;
    }



}
