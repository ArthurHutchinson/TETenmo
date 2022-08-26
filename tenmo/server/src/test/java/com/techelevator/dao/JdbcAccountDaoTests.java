package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import org.junit.Before;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import javax.sql.rowset.JdbcRowSet;

public class JdbcAccountDaoTests extends BaseDaoTests{

    // TODO: Write DaoTesting for Account, mainly getBalance, refer to test-data.sql and make private data to compare.

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(dataSource);
    }

}
