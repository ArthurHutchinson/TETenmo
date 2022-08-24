package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;
//    public static List<Account> accounts = new ArrayList<>();

    public JdbcAccountDao (DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);

    @Override
    public Transfer addTransfer(Account account) {
        return null;
    }
}
