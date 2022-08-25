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

        String sql = "INSERT INTO transfer (from_user_id, from_account_id, to_user_id, to_account_id, transfer_amount)\n" +
                "VALUES ((SELECT user_id FROM tenmo_user WHERE user_id = ?),?,?,?,?) RETURNING transfer_id;";
        String sql2 = "UPDATE account SET "
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                newTransfer.getFromUserId() , newTransfer.getFromAccountId(),
                newTransfer.getToUserId(), newTransfer.getToAccountId(),
                newTransfer.getTransferAmount());
        newTransfer.setTransferId(newId);
        return newTransfer;
    }


}
