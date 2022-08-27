package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferDTO;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests{

    private static final Transfer TRANSFER_1 = new Transfer(3001, 2001, 2002, new BigDecimal("50.00"), true);


    private static final Account ACCOUNT_1 = new Account(2001,1001, new BigDecimal("1000.00"));
    private static final Account ACCOUNT_2 = new Account(2002,1002, new BigDecimal("2000.00"));

    private static final User USER_1 = new User(1001, "TestUser1", "$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2", "USER");
    private static final User USER_2 = new User(1002, "TestUser2", "$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy", "USER");

    private static final TransferDTO TRANSFER_DTO_1 = new TransferDTO(USER_1.getUsername(), USER_2.getUsername(), TRANSFER_1.getTransferAmount());

    private JdbcTransferDao sut;



    // TODO: Place some tests for the transfers, refer to test-data.sql and make private data to compare.

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(dataSource);
    }

    @Test
    public void getTransferByUserId_returns_transfer() {
        List<TransferDTO> transfers = sut.getTransfersByUserId(USER_1.getId(), USER_1.getUsername(), ACCOUNT_1.getAccountId());
        assertTransferDTOMatch(TRANSFER_DTO_1,transfers.get(0));
    }

    @Test
    public void getTransferByUserId_returns_empty() {
        List<TransferDTO> transfers = sut.getTransfersByUserId(0, "I don't exist", 5000);
        List<TransferDTO> empty = new ArrayList<>();
        Assert.assertEquals(empty, transfers);
    }

    @Test
    public void getTransferByTransferId_returns_transfer() {
        List<TransferDTO> transfer = sut.getTransferByTransferId(USER_1.getId(),
                USER_1.getUsername(),
                ACCOUNT_1.getAccountId(),
                TRANSFER_1.getTransferId());
        assertTransferDTOMatch(TRANSFER_DTO_1,transfer.get(0));
    }

    //

    @Test
    public void getTransferByTransferId_returns_null() {
        List<TransferDTO> transfers = sut.getTransferByTransferId(0, "I don't exist", 5000, 2);
        List<TransferDTO> empty = new ArrayList<>();
        Assert.assertEquals(empty, transfers);
    }

    @Test
    public void createNewTransfer() {
        Transfer newTransfer = new Transfer(2001, 2002, new BigDecimal("175.00"), false);
        Transfer createdTransfer = sut.createTransfer(newTransfer);

        Assert.assertNotNull(createdTransfer);

        Integer newId = createdTransfer.getTransferId();
        Assert.assertTrue(newId > 0);

        newTransfer.setTransferId(newId);
        assertTransferMatch(newTransfer,createdTransfer);
    }

    private void assertTransferMatch(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getFromAccountId(),actual.getFromAccountId());
        Assert.assertEquals(expected.getToAccountId(), actual.getToAccountId());
        Assert.assertEquals(expected.getTransferAmount(), actual.getTransferAmount());
        Assert.assertEquals(expected.getFromUsername(), actual.getFromUsername());
        Assert.assertEquals(expected.getToUsername(), actual.getToUsername());
    }

    private void assertTransferDTOMatch (TransferDTO expected, TransferDTO actual) {
        Assert.assertEquals(expected.getFromUserName(), actual.getFromUserName());
        Assert.assertEquals(expected.getToUserName(), actual.getToUserName());
        Assert.assertEquals(expected.getTransferAmount(), actual.getTransferAmount());
    }

}




















