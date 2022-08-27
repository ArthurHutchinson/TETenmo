package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
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

        String sql = "INSERT INTO transfer\n" +
                "(from_account_id, to_account_id, transfer_amount)\n" +
                "VALUES (?,?,?)\n" +
                "RETURNING transfer_id;";

        // This creates a new insert into the transfer table in SQL
        Integer newId = jdbcTemplate.queryForObject(sql, Integer.class,
                newTransfer.getFromAccountId(),
                newTransfer.getToAccountId(),
                newTransfer.getTransferAmount());
                newTransfer.setTransferId(newId);

                return newTransfer;
    }

    @Override
    public int verifyTransferIsLegit(Transfer newTransfer){
        String sqlBalance = "SELECT balance FROM account WHERE account_id = ?;";
        Integer balance = jdbcTemplate.queryForObject(sqlBalance, Integer.class, newTransfer.getFromAccountId());
        BigDecimal bigBalance = new BigDecimal(balance);
        BigDecimal zero = new BigDecimal("0.0");
        if(newTransfer.getFromAccountId() == newTransfer.getToAccountId()
                || newTransfer.getFromUsername() == newTransfer.getToUsername()
                || bigBalance.compareTo(newTransfer.getTransferAmount()) <= 0
                || newTransfer.getTransferAmount().compareTo(zero) <= 0) {
            return 0;
        }return 1;
    }

    @Override
    public void updateAccountsForTransfer(Transfer pendingTransfer){
        String sqlFrom = "UPDATE account SET balance = balance - ? WHERE account_id = ?;";
        String sqlTo = "UPDATE account SET balance = balance + ? WHERE account_id = ?;";

        jdbcTemplate.update(sqlFrom,pendingTransfer.getTransferAmount(),pendingTransfer.getFromAccountId());
        jdbcTemplate.update(sqlTo,pendingTransfer.getTransferAmount(),pendingTransfer.getToAccountId());

    }

    @Override
    public Transfer setStatusToApproved(Transfer pendingTransfer){
        String sql = "UPDATE transfer SET is_approved = true WHERE transfer_id = ?;";
        jdbcTemplate.update(sql, pendingTransfer.getTransferId());

        return pendingTransfer;
    }


    @Override
    public List<TransferDTO> getTransfersByUserId(int userId, String username, int accountId) {
        List<TransferDTO> transfers = new ArrayList<>();
        String sql = "SELECT u.username, transfer_amount, transfer_id, t.to_account_id\n" +
                "FROM transfer AS t\n" +
                "JOIN account AS a ON a.account_id = t.from_account_id OR a.account_id = t.to_account_id\n" +
                "JOIN tenmo_user AS u ON a.user_id = u.user_id\n" +
                "WHERE transfer_id IN (SELECT transfer_id\n" +
                "FROM transfer AS t \n" +
                "JOIN account AS a ON a.account_id = t.from_account_id OR a.account_id = t.to_account_id\n" +
                "JOIN tenmo_user AS u ON a.user_id = u.user_id\n" +
                "WHERE a.user_id = ?) AND \n" +
                "t.from_account_id = ? AND\n" +
                "u.username = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId, accountId, username);
        String toUserSql = "SELECT u.username AS to_username\n" +
                "FROM transfer AS t\n" +
                "JOIN account AS a ON a.account_id = t.from_account_id OR a.account_id = t.to_account_id\n" +
                "JOIN tenmo_user AS u ON a.user_id = u.user_id\n" +
                "WHERE transfer_id IN (SELECT transfer_id\n" +
                "FROM transfer AS t \n" +
                "JOIN account AS a ON a.account_id = t.from_account_id OR a.account_id = t.to_account_id\n" +
                "JOIN tenmo_user AS u ON a.user_id = u.user_id\n" +
                "WHERE a.user_id = ?) AND \n" +
                "t.from_account_id = ? AND\n" +
                "u.username <> ?;";
        SqlRowSet toUserResult = jdbcTemplate.queryForRowSet(toUserSql, userId, accountId, username);
        TransferDTO transfer = new TransferDTO();
        while(result.next()) {
            transfer.setFromUserName(username);
            transfer.setTransferAmount(result.getBigDecimal("transfer_amount"));
            transfers.add(transfer);
        }
        while(toUserResult.next()){
            transfer.setToUserName(toUserResult.getString("to_username"));
        }

        return transfers;
    }


    @Override
    public List<TransferDTO> getTransferByTransferId(int userId, String username, int accountId, int transfer_id) {

        List<TransferDTO> transfers = new ArrayList<>();
        String sql = "SELECT u.username, transfer_amount, transfer_id, t.to_account_id, transfer_id\n" +
                "FROM transfer AS t\n" +
                "JOIN account AS a ON a.account_id = t.from_account_id OR a.account_id = t.to_account_id\n" +
                "JOIN tenmo_user AS u ON a.user_id = u.user_id\n" +
                "WHERE transfer_id IN\n" +
                "(SELECT transfer_id FROM transfer AS t\n" +
                "JOIN account AS a ON a.account_id = t.from_account_id OR a.account_id = t.to_account_id\n" +
                "JOIN tenmo_user AS u ON a.user_id = u.user_id\n" +
                "WHERE a.user_id = ?) AND\n" +
                "t.from_account_id = ? AND\n" +
                "u.username = ? AND\n" +
                "transfer_id = ?;";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, userId, accountId, username, transfer_id);

        String toUserSql = "SELECT u.username AS to_username\n" +
                "FROM transfer AS t\n" +
                "JOIN account AS a ON a.account_id = t.from_account_id OR a.account_id = t.to_account_id\n" +
                "JOIN tenmo_user AS u ON a.user_id = u.user_id\n" +
                "WHERE transfer_id IN (SELECT transfer_id\n" +
                "FROM transfer AS t \n" +
                "JOIN account AS a ON a.account_id = t.from_account_id OR a.account_id = t.to_account_id\n" +
                "JOIN tenmo_user AS u ON a.user_id = u.user_id\n" +
                "WHERE a.user_id = ?) AND \n" +
                "t.from_account_id = ? AND\n" +
                "u.username <> ? AND\n" +
                "transfer_id = ?;";
        SqlRowSet toUserResult = jdbcTemplate.queryForRowSet(toUserSql, userId, accountId, username, transfer_id);

        TransferDTO transfer = new TransferDTO();
        while(result.next()) {
            transfer.setFromUserName(username);
            transfer.setTransferAmount(result.getBigDecimal("transfer_amount"));
            transfers.add(transfer);
        }
        while(toUserResult.next()){
            transfer.setToUserName(toUserResult.getString("to_username"));
        }

        return transfers;
        //        List<Transfer> transfers = new ArrayList<>();
//        String sql = "SELECT transfer_id, from_account_id, to_account_id, transfer_amount\n" +
//                "FROM transfer AS t\n" +
//                "JOIN account AS a ON a.account_id = t.from_account_id OR a.account_id = t.to_account_id\n" +
//                "WHERE transfer_id = ? AND (a.user_id = ?);";
//        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, transferId, userId);
//        while(result.next()) {
//            Transfer transfer = mapRowToTransfer(result);
//            transfers.add(transfer);
//        }
//        return transfers;

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
