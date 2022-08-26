package com.techelevator.dao;


import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import javax.sql.DataSource;
import java.util.List;

public class JdbcUserDaoTests extends BaseDaoTests{

    private static final User USER_1 = new User(1001, "TestUser1", "$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2", "USER");
    private static final User USER_2 = new User(1002, "TestUser2", "$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy", "USER");

    private JdbcUserDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    public void findIdByUsername_returns_correct_id() {
       int id = sut.findIdByUsername(USER_1.getUsername());
       Assert.assertEquals(USER_1.getId(), id);
    }

    @Test
    public void findIdByUsername_returns_zero_when_id_not_found() {
        int id = sut.findIdByUsername("IDoNotExist");
        Assert.assertEquals(-1,id);
    }

    @Test
    public void findAll_returns_list_of_all_users() {
        List<User> users = sut.findAll();
        Assert.assertEquals(2,users.size());
    }

    @Test
    public void findByUsername_returns_correct_user() {
        USER_1.setAuthorities("USER");
        User user = sut.findByUsername(USER_1.getUsername());
        Assert.assertNotNull(user);
        assertUserMatch(USER_1, user);
//        Assert.assertEquals(USER_1,user);
    }

    @Test (expected = UsernameNotFoundException.class)
    public void findByUsername_returns_null_when_username_not_found() {
        User user = sut.findByUsername("IDoNotExist");
    }

    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("TEST_USER");
        Assert.assertEquals("TEST_USER", user.getUsername());
    }

    private void assertUserMatch(User expected, User actual) {
        Assert.assertEquals(expected.getId(),actual.getId());
        Assert.assertEquals(expected.getUsername(), actual.getUsername());
        String expectedRole = expected.getAuthorities().toString();
        String actualRole = actual.getAuthorities().toString();
        Assert.assertEquals(expectedRole,actualRole);
        Assert.assertEquals(expected.getPassword(),actual.getPassword());
    }

}
