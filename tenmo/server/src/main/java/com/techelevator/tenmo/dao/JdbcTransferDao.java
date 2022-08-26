package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private final JdbcTemplate jdbcTemplate;

    public JdbcTransferDao (DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    // This is the original method
    //need transferController post method to set newTransfer to_account_id & from_account_id
//    @Override
//    public Transfer createTransfer(Transfer newTransfer) {
//
//        String sql = "INSERT INTO transfer \n" +
//        "(from_user_id, from_account_id, to_user_id, to_account_id, transfer_amount)\n" +
//                "VALUES ((SELECT user_id FROM tenmo_user WHERE user_id = ?),?,?,?,?) RETURNING transfer_id;";
//        String sqlFrom = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
//        String sqlTo = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
//        String sqlBalance = "SELECT balance FROM account WHERE account_id = ?;";
//        Integer balance = jdbcTemplate.queryForObject(sqlBalance, Integer.class, newTransfer.getFromAccountId());
//        BigDecimal bigBalance = new BigDecimal(balance);
//        BigDecimal zero = new BigDecimal("0.0");
//        if(newTransfer.getFromAccountId() == newTransfer.getToAccountId()
//                || bigBalance.compareTo(newTransfer.getTransferAmount())  == -1
//                || (newTransfer.getFromUserId() == newTransfer.getToUserId()
//                || newTransfer.getTransferAmount().compareTo(zero) == 0)) {
//          return null  ;
//        }
//        // TODO: Sending transfer has an initial status of "Approved".
//
//        // This creates a new insert into the transfer table in SQL
//        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
//                newTransfer.getFromUserId() , newTransfer.getFromAccountId(),
//                newTransfer.getToUserId(), newTransfer.getToAccountId(),
//                newTransfer.getTransferAmount());
//        newTransfer.setTransferId(newId);
//
//        // This updates the account balance.
//        jdbcTemplate.update(sqlFrom,newTransfer.getTransferAmount(),newTransfer.getFromAccountId());
//        jdbcTemplate.update(sqlTo,newTransfer.getTransferAmount(),newTransfer.getToAccountId());
//
//        return newTransfer;
//    }


    // TODO: TEST THIS
    @Override
    public Transfer createTransfer(Transfer newTransfer) {

        String sql = "INSERT INTO transfer\n" +
                "(from_account_id, to_account_id, transfer_amount)\n" +
                "VALUES (?,?,?)\n" +
                "RETURNING transfer_id;";
        String sqlFrom = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        String sqlTo = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        String sqlBalance = "SELECT balance FROM account WHERE account_id = ?;";

        // Prevent transferring from same account, transferring funds to where account is negative
        // TODO: User is able to 'steal' money from another account by adding a negative number.
        Integer balance = jdbcTemplate.queryForObject(sqlBalance, Integer.class, newTransfer.getFromAccountId());
        BigDecimal bigBalance = new BigDecimal(balance);
        BigDecimal zero = new BigDecimal("0.0");
        if(newTransfer.getFromAccountId() == newTransfer.getToAccountId()
                || bigBalance.compareTo(newTransfer.getTransferAmount()) <= 0
                || newTransfer.getTransferAmount().compareTo(zero) == 0) {
          return null;
        }

        // TODO: Sending transfer has an initial status of "Approved".

        // This creates a new insert into the transfer table in SQL
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                newTransfer.getFromAccountId(),
                newTransfer.getToAccountId(),
                newTransfer.getTransferAmount());
                newTransfer.setTransferId(newId);

        // This updates the account balance.
        jdbcTemplate.update(sqlFrom,newTransfer.getTransferAmount(),newTransfer.getFromAccountId());
        jdbcTemplate.update(sqlTo,newTransfer.getTransferAmount(),newTransfer.getToAccountId());

        return newTransfer;
    }

    @Override
    public Transfer setStatusToApproved(Transfer pendingTransfer){
        String sql = "UPDATE transfer SET is_approved = true WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, pendingTransfer.getTransferId());

        return pendingTransfer;
    }


    // TODO: TEST THIS
    @Override
    public List<Transfer> getTransfersByUserId(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, from_account_id, to_account_id, transfer_amount\n" +
                "FROM transfer AS t\n" +
                "JOIN account AS a ON a.account_id = t.from_account_id OR a.account_id = t.to_account_id\n" +
                "WHERE a.user_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        while(result.next()) {
            Transfer transfer = mapRowToTransfer(result);
            transfers.add(transfer);
        }
        return transfers;
    }


    // TODO: TEST THIS
    @Override
    public List<Transfer> getTransferByTransferId(int transferId, int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, from_account_id, to_account_id, transfer_amount\n" +
                "FROM transfer AS t\n" +
                "JOIN account AS a ON a.account_id = t.from_account_id OR a.account_id = t.to_account_id\n" +
                "WHERE transfer_id = ? AND (a.user_id = ?);";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId, userId);
        while(result.next()) {
            Transfer transfer = mapRowToTransfer(result);
            transfers.add(transfer);
        }
        return transfers;
    }

    private Transfer mapRowToTransfer (SqlRowSet result) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(result.getInt("transfer_id"));
        transfer.setFromAccountId(result.getInt("from_account_id"));
        transfer.setToAccountId(result.getInt("to_account_id"));
        transfer.setTransferAmount(result.getBigDecimal("transfer_amount"));
        return transfer;
    }
}
