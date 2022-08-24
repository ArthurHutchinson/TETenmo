package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

@Component
public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao (DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

@Override
    public Transfer createTransfer(Transfer newTransfer) {
//        User user = null;
//        User user2 = null;
//        Account account = null;
//        Account account2 = null;
        String sql = "INSERT INTO transfer (from_user_id, from_account_id, to_user_id, to_account_id, transfer_amount)\n" +
                "VALUES (?,?,?,?,?) RETURNING transfer_id;";
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                newTransfer.getFromUserId(), newTransfer.getFromAccountId(),
                newTransfer.getToUserId(), newTransfer.getToAccountId(),
                newTransfer.getTransfer_amount());
        newTransfer.setTransferId(newId);
        return newTransfer;
    }


}
