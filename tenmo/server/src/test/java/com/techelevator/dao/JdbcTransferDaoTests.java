package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests{

    private static final Transfer TRANSFER_1 = new Transfer(3001, 2001, 2002, new BigDecimal("1000.00"), true);

    private JdbcTransferDao sut;

    // TODO: Place some tests for the transfers, refer to test-data.sql and make private data to compare.

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(dataSource);
    }

    @Test
    public void getTransferByUserId_returns_transfer() {
        // This doesn't work in the current state of code.
        // There is no getUserId
        Assert.fail();
    }

    @Test
    public void getTransferByUserId_returns_null() {
        // This doesn't work in the current state of code.
        // There is no getUserId
        Assert.fail();
    }

    @Test
    public void getTransferByTransferId_returns_transfer() {
        List<Transfer> transfer = sut.getTransferByTransferId(TRANSFER_1.getTransferId(), 1001);
        assertTransferMatch(TRANSFER_1,transfer.get(0));
        // There is no getUserId
    }

    @Test
    public void getTransferByTransferId_returns_null() {
        Assert.fail();
    }

    @Test
    public void createNewTransfer() {
        Assert.fail();
    }

    private void assertTransferMatch(Transfer expected, Transfer actual) {
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getFromAccountId(),actual.getFromAccountId());
        Assert.assertEquals(expected.getToAccountId(), actual.getToAccountId());
        Assert.assertEquals(expected.getTransferAmount(), actual.getTransferAmount());
        Assert.assertEquals(expected.getFromUsername(), actual.getFromUsername());
        Assert.assertEquals(expected.getToUsername(), actual.getToUsername());
    }

}
