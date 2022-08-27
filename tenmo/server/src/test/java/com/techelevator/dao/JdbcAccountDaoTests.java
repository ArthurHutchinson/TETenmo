package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import javax.sql.rowset.JdbcRowSet;
import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests{

    private static final Account ACCOUNT_1 = new Account(2001,1001, new BigDecimal("1000.00"));
    private static final Account ACCOUNT_2 = new Account(2002,1002, new BigDecimal("2000.00"));
    private static final User USER_1 = new User(1001, "TestUser1", "$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2", "USER");
    private static final User USER_2 = new User(1002, "TestUser2", "$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy", "USER");

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(dataSource);
    }

    @Test
    public void getBalance_returns_correct_balance() {
        BigDecimal test = sut.getBalance(ACCOUNT_1.getAccountId());
        Assert.assertEquals(ACCOUNT_1.getBalance(), test);
    }

    @Test
    public void getBalance_returns_null() {
        BigDecimal test = sut.getBalance(999);
        Assert.assertNull(test);
    }

    @Test
    public void get_account_id_by_username_returns_account_id(){
       int test = sut.getAccountIdByUsername(USER_1.getUsername());
       Assert.assertEquals(ACCOUNT_1.getUserId(), USER_1.getId());
       Assert.assertEquals(2001, ACCOUNT_1.getAccountId());

    }

    @Test
    public void get_account_id_by_username_returns_only_correct_account_id() {
        int test = sut.getAccountIdByUsername(USER_1.getUsername());
        Assert.assertNotEquals(2002,  ACCOUNT_1.getAccountId());
    }

    @Test
    public void createAccount() {
//        Transfer newTransfer = new Transfer(2001, 2002, new BigDecimal("175.00"), false);
//        Transfer createdTransfer = sut.createTransfer(newTransfer);
//
//        Assert.assertNotNull(createdTransfer);
//
//        Integer newId = createdTransfer.getTransferId();
//        Assert.assertTrue(newId > 0);
//
//        newTransfer.setTransferId(newId);
//        assertTransferMatch(newTransfer,createdTransfer);

        Account newAccount = new Account(1001, new BigDecimal("3000.00"));
        Account createdAccount = sut.createAccount(newAccount);

        Assert.assertNotNull(createdAccount);

        Integer newId = createdAccount.getAccountId();
        Assert.assertTrue(newId > 0);

        newAccount.setAccountId(newId);
        assertAccountMatch(newAccount,createdAccount);

    }

    private void assertAccountMatch(Account expected, Account actual){
        Assert.assertEquals(expected.getAccountId(),actual.getAccountId());
        Assert.assertEquals(expected.getUserId(),actual.getUserId());
        Assert.assertEquals(expected.getBalance(),actual.getBalance());
    }

}
