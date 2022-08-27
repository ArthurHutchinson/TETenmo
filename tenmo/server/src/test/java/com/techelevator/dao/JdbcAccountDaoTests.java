package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import javax.sql.rowset.JdbcRowSet;
import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests{

    // TODO: Write DaoTesting for Account, mainly getBalance, refer to test-data.sql and make private data to compare.

    // TODO: Need to figure out how to test automatic creation of account when user is createrd.

    private static final Account ACCOUNT_1 = new Account(2001,1001, new BigDecimal("1000.00"));
    private static final Account ACCOUNT_2 = new Account(2002,1002, new BigDecimal("2000.00"));

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

}
