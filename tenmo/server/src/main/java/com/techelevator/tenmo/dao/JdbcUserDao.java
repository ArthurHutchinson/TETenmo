package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.User;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcUserDao implements UserDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcUserDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int findIdByUsername(String username) {
        String sql = "SELECT user_id FROM tenmo_user WHERE username ILIKE ?;";
        try {
            Integer id = jdbcTemplate.queryForObject(sql, Integer.class, username);
            if (id != null) {
                return id;
            } else {
                return -1;
            }
        } catch (EmptyResultDataAccessException e) {
            return -1;
        }
    }

    @Override
    public String findUsernameById(int userId) {
        String sql = "SELECT username FROM tenmo_user WHERE user_id = ?;";
        String usernameReturn = jdbcTemplate.queryForObject(sql, String.class, userId);
        return usernameReturn;
    }




    @Override
    public List<String> findAllUsernames() {
        List<String> usernames = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            User user = mapRowToUser(results);
            usernames.add(user.getUsername());
        }
        return usernames;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql);
        while(results.next()) {
            User user = mapRowToUser(results);
            users.add(user);
        }
        return users;
    }

    @Override
    public User findByUsername(String username) throws UsernameNotFoundException {
        String sql = "SELECT user_id, username, password_hash FROM tenmo_user WHERE username ILIKE ?;";
        SqlRowSet rowSet = jdbcTemplate.queryForRowSet(sql, username);
        if (rowSet.next()){
            return mapRowToUser(rowSet);
        }
        throw new UsernameNotFoundException("User " + username + " was not found.");
    }

    @Override
    public boolean create(String username, String password) {

        // create user
        String sql = "INSERT INTO tenmo_user (username, password_hash) VALUES (?, ?) RETURNING user_id";
        String sql2 = "INSERT INTO account(user_id, balance)\n" +
                "VALUES(?, ?) RETURNING account_id;";
        String password_hash = new BCryptPasswordEncoder().encode(password);
        Integer newUserId;
        Account newAccount = new Account();
        try {
            newUserId = jdbcTemplate.queryForObject(sql, Integer.class, username, password_hash);
            Integer newAccountId = jdbcTemplate.queryForObject(sql2, Integer.class, newUserId, 1000.00);
            newAccount.setAccountId(newAccountId);
        } catch (DataAccessException e) {
            return false;
        }


        return true;
    }

    private User mapRowToUser(SqlRowSet rs) {
        User user = new User();
        user.setId(rs.getInt("user_id"));
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password_hash"));
        user.setActivated(true);
        user.setAuthorities("USER");
        return user;
    }
}
