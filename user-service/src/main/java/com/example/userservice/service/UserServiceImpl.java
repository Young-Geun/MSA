package com.example.userservice.service;

import com.example.userservice.client.OrderServiceClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.jpa.UserEntity;
import com.example.userservice.jpa.UserRepository;
import com.example.userservice.vo.ResponseOrder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final Environment env;
    // private final RestTemplate restTemplate;
    private final OrderServiceClient orderServiceClient;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final CircuitBreakerFactory circuitBreakerFactory;

    @Override
    public UserDto createUser(UserDto userDto) {
        userDto.setUserId(UUID.randomUUID().toString());

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserEntity userEntity = mapper.map(userDto, UserEntity.class);
        userEntity.setEncryptedPwd(passwordEncoder.encode(userDto.getPwd()));

        userRepository.save(userEntity);

        return mapper.map(userEntity, UserDto.class);
    }

    @Override
    public UserDto getUserById(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        UserDto userDto = new ModelMapper().map(userEntity, UserDto.class);

        // Using Rest Template
//        String orderUrl = String.format(env.getProperty("order-service.url"), userId);
//        ResponseEntity<List<ResponseOrder>> ordersResponse = restTemplate.exchange(
//                orderUrl,
//                HttpMethod.GET,
//                null,
//                new ParameterizedTypeReference<List<ResponseOrder>>() {
//        });
//        List<ResponseOrder> orders = ordersResponse.getBody();

        // Using FeignClient
        // List<ResponseOrder> orders = orderServiceClient.getOrders(userId);

        // Using CircuitBreaker
        log.info("Before call orders");
        CircuitBreaker circuitbreaker = circuitBreakerFactory.create("circuitbreaker");
        List<ResponseOrder> orders = circuitbreaker.run(() -> orderServiceClient.getOrders(userId),
                throwable -> new ArrayList<>());
        log.info("After call orders");
        userDto.setOrders(orders);

        return userDto;
    }

    @Override
    public Iterable<UserEntity> getUserByAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String findByEmail) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByEmail(findByEmail);
        if (userEntity == null) {
            throw new UsernameNotFoundException(findByEmail);
        }

        return new User(userEntity.getEmail(), userEntity.getEncryptedPwd(), true, true, true, true, new ArrayList<>());
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity userEntity = userRepository.findByEmail(email);
        if (userEntity == null) {
            throw new UsernameNotFoundException("User not found");
        }

        return new ModelMapper().map(userEntity, UserDto.class);
    }
}
