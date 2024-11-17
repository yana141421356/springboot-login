package com.example.springboot_login.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.springboot_login.entity.User;
import com.example.springboot_login.repository.UserRepository;

/**
 * カスタムUserDetailsServiceの実装クラス。 ユーザー名に基づいてユーザーの詳細情報をロードします。
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * 指定されたユーザー名に基づいてユーザーの詳細情報をロードします。
     * 
     * @param username ユーザー名
     * @return UserDetailsインスタンス
     * @throws UsernameNotFoundException ユーザーが見つからない場合にスローされます
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // ユーザー名に基づいてユーザーを検索
        User user = userRepository.findByUsername(username);
        if (user == null) {
            // ユーザーが見つからない場合は例外をスロー
            throw new UsernameNotFoundException("User not found");
        }
        // ユーザーの詳細情報を返す
        return org.springframework.security.core.userdetails.User.withUsername(user.getUsername())
                .password(user.getPassword()).roles(user.getRole()).build();
    }
}
