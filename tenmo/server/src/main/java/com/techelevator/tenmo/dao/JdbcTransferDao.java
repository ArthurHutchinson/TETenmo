package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
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

    @Override
    public Transfer createTransfer(Transfer newTransfer) {

        String sql = "INSERT INTO transfer \n" +
        "(from_user_id, from_account_id, to_user_id, to_account_id, transfer_amount)\n" +
                "VALUES ((SELECT user_id FROM tenmo_user WHERE user_id = ?),?,?,?,?) RETURNING transfer_id;";
        String sqlFrom = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        String sqlTo = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";
        String sqlBalance = "SELECT balance FROM account WHERE account_id = ?;";
        Integer balance = jdbcTemplate.queryForObject(sqlBalance, Integer.class, newTransfer.getFromAccountId());
        BigDecimal bigBalance = new BigDecimal(balance);
        BigDecimal zero = new BigDecimal("0.0");
        if(newTransfer.getFromAccountId() == newTransfer.getToAccountId() || bigBalance.compareTo(newTransfer.getTransferAmount())  == -1 || (newTransfer.getFromUserId() == newTransfer.getToUserId() || newTransfer.getTransferAmount().compareTo(zero) == 0)) {
          return null  ;
        }
        // TODO: Sending transfer has an initial status of "Approved".

        // This creates a new insert into the transfer table in SQL
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                newTransfer.getFromUserId() , newTransfer.getFromAccountId(),
                newTransfer.getToUserId(), newTransfer.getToAccountId(),
                newTransfer.getTransferAmount());
        newTransfer.setTransferId(newId);

        // This updates the account balance.
        jdbcTemplate.update(sqlFrom,newTransfer.getTransferAmount(),newTransfer.getFromAccountId());
        jdbcTemplate.update(sqlTo,newTransfer.getTransferAmount(),newTransfer.getToAccountId());

        return newTransfer;
    }

    @Override
    public List<Transfer> getTransfersByUserId(int userId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, from_user_id, from_account_id, to_user_id, to_account_id, transfer_amount\n" +
                "FROM transfer\n" +
                "WHERE from_user_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId);
        while(result.next()) {
            Transfer transfer = mapRowToTransfer(result);
            transfers.add(transfer);
        }
        return transfers;
    }

    @Override
    public List<Transfer> getTransferByTransferId(int transferId) {
        List<Transfer> transfers = new ArrayList<>();
        String sql = "SELECT transfer_id, from_user_id, from_account_id, to_user_id, to_account_id, transfer_amount\n" +
                "FROM transfer\n" +
                "WHERE transfer_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId);
        while(result.next()) {
            Transfer transfer = mapRowToTransfer(result);
            transfers.add(transfer);
        }
        return transfers;
    }

    // TODO: Need a GET of a Transaction ID

    private Transfer mapRowToTransfer (SqlRowSet result) {
        Transfer transfer = new Transfer();
        transfer.setTransferId(result.getInt("transfer_id"));
        transfer.setFromUserId(result.getInt("from_user_id"));
        transfer.setFromAccountId(result.getInt("from_account_id"));
        transfer.setToUserId(result.getInt("to_user_id"));
        transfer.setToAccountId(result.getInt("to_account_id"));
        transfer.setTransferAmount(result.getBigDecimal("transfer_amount"));
        return transfer;
    }
}
