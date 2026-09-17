package com.example.dockerapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import users.Users;

@RestController
@RequestMapping("/api")
public class HelloController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/hello")
	public String sayHello() {
		return "Hello, Docker World!";
	}

	@GetMapping("/hoge")
	public String sayHoge() {
		return "hogehogehoge";
	}

	// 課題1 エンドポイントにGET通信をしたときに、レスポンスが返却されるようにする
	@GetMapping("/users")
	public Users getUsers() {
		Users users = new Users(1, "John Doe", "john.doe@example.com");
		return users;
	}

	// 課題2 エンドポイントにGET通信をしたときに、レスポンスが返却されるようにする
	// エンドポイント：http://localhost:8080/users/{user_id}
	@GetMapping("/users/{user_id}")
	public Users getUsersDetail(@PathVariable("user_id") Long id) {
		String sql = "SELECT id,name,email FROM users WHERE id = ?";
		Users users = jdbcTemplate.queryForObject(
				sql, 
				BeanPropertyRowMapper.newInstance(Users.class), 
				id
		);
		return users;// 得られたデータリストをwebブラウザやcurlに返す
	}

	@GetMapping("/check-db")
	public String checkDbConnection() {
		try {
			jdbcTemplate.queryForObject("SELECT 1", Integer.class); // MySQLへの接続確認
			return "Database connection is successful!";
		} catch (Exception e) {
			return "Database connection failed!";
		}
	}
}
