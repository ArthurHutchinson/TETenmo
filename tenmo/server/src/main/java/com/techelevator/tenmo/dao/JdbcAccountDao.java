package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;

//TODO: Add Authentication and Authorization Tags

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
//    TODO build out this method.
    @Override
    public int getAccountIdByUsername(String username){

        String sql = "SELECT MIN(account_id) AS deposit_account FROM account JOIN tenmo_user as t ON t.user_id = account.user_id WHERE username = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, username);
        try {
            if (result.next()) {
                int accountId = result.getInt("deposit_account");
                return accountId;
            }
        }catch (DataAccessException e){
            System.err.println("data access exception");
        }return 0;
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
            System.err.println("null pointer exception");
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
