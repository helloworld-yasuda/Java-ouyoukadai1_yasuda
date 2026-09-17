package com.example.dockerapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    
//    課題1 エンドポイントにGET通信をしたときに、レスポンスが返却されるようにする
    @GetMapping("/users")
    public Users getUsers() { 
    	Users users = new Users(1,"John Doe","john.doe@example.com");
    	return users;
    }
    
    //課題2 エンドポイントにGET通信をしたときに、レスポンスが返却されるようにする エンドポイント：http://localhost:8080/users/{user_id}
    @GetMapping("/users/{user_id}")
    public Users getUsersDetail(@PathVariable("user_id") Long id){
        String sql = "SELECT id,name,email FROM users WHERE id = ?";//DBに命令するSQL文を変数sqlに格納する
        Users users = jdbcTemplate.queryForObject(//jdbcTemplate.queryForObject()はDBから一件取得する
            sql,//命令文を渡す　BeanPropertyRowMapperはDBから返ってきたデータを列名とクラスのプロパティを結びつける
            BeanPropertyRowMapper.newInstance(Users.class),//列名とフィールド名を対応付けてjavaのデータに変換
            id//webURLからidを代入　
        );
        return users;//得られたデータリストをwebブラウザやcurlに返す
    }
    
    //課題3 要件2 エンドポイントにPOST通信をしたときに、新たにデータが追加される。作成されたデータが返却されるようにする
    @PostMapping("/users")
    public Users createUser(@RequestBody Users newUser) {
        String sql = "INSERT INTO users (name, email) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();//自動裁判されたidを格納されているもの
        jdbcTemplate.update(sql, keyHolder, newUser.name, newUser.email);
        int generatedId = keyHolder.getKey().intValue();//自動裁判されたidを取り出す
        newUser.id = generatedId;
        return newUser;
    }   
    
    //要件3 
    @PutMapping("/users/{user_id}")
    public ResponseEntity<Users> updateUser(@PathVariable("user_id") Long id, @RequestBody Users updatedUser) {
        String sql = "UPDATE users SET name = ?, email = ? WHERE id = ?";
        jdbcTemplate.update(sql, updatedUser.name, updatedUser.email, id);
        return ResponseEntity.noContent().build();
    }
    
    @DeleteMapping("/users/{user_id}")
    public ResponseEntity<Users> deleteUserEntity (@PathVariable("user_id") Long id) {
    	String sql = "DELETE FROM users WHERE id = ?";
    	int rows = jdbcTemplate.update(sql, id);
    	if(rows == 0 ) {
    		 return ResponseEntity.notFound().build();
    	}
    	return ResponseEntity.noContent().build();
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
