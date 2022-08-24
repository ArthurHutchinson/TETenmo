package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.LoginDTO;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcAccountDao implements AccountDao {

    private final JdbcTemplate jdbcTemplate;
//    public static List<Account> accounts = new ArrayList<>();

    public JdbcAccountDao (DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Account createAccount(Account newAccount) {
        String sql = "INSERT INTO account(user_id, balance)\n" +
                "VALUES(?, ?) RETURNING account_id;";
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class, newAccount.getUserId(), 1000.00);
        newAccount.setAccountId(newId);
        return newAccount;
    }

    @Override
    public BigDecimal getBalance(int accountId) {
        Account account = null;
        String sql = "SELECT balance, user_id, account_id FROM account WHERE account_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, accountId);
        try{
        if (result.next()) {
            account = mapRowToAccount(result);
            return account.getBalance();
        }
        }catch (NullPointerException e) {
            return null;
        }return null;
    }

    private Account mapRowToAccount(SqlRowSet result) {
        Account account = new Account();
        account.setAccountId(result.getInt("account_id"));
        account.setUserId(result.getInt("user_id"));
        account.setBalance(result.getBigDecimal("balance"));
        return account;
    }





}
