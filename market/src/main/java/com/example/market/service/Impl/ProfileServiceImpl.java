package com.example.market.service.Impl;

import com.example.market.dto.UpdatePasswordDto;
import com.example.market.dto.UpdateUserDto;
import com.example.market.dto.UserDto;
import com.example.market.exception.PasswordsNotTheSameException;
import com.example.market.exception.RegistrationFailedException;
import com.example.market.exception.UserNotFoundException;
import com.example.market.exception.WrongPasswordException;
import com.example.market.i18n.I18nUtil;
import com.example.market.mapper.UserMapper;
import com.example.market.model.User;
import com.example.market.repository.UserRepository;
import com.example.market.service.ProfileService;
import com.example.market.service.UserService;
import com.example.market.util.Messages;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProfileServiceImpl implements ProfileService {

    UserRepository userRepository;
    UserService userService;
    UserMapper userMapper;
    I18nUtil i18nUtil;
    PasswordEncoder passwordEncoder;

    @Override
    public UserDto getUser() {
        log.info("Trying get user by authentication in security");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String email = (String) authentication.getPrincipal();
            return userService.findByEmail(email);
        }
        throw new UserNotFoundException(i18nUtil.getMessage(Messages.USER_ERROR_CONTEXT_NOT_FOUND));
    }

    @Override
    public void updatePassword(UpdatePasswordDto updatePasswordDto) {
        User user = userRepository.findById(updatePasswordDto.getId()).orElseThrow(() ->
                new UserNotFoundException(i18nUtil.getMessage(Messages.USER_ERROR_ID_NOT_FOUND, String.valueOf(updatePasswordDto.getId()))));

        if (!passwordEncoder.matches(updatePasswordDto.getOldPassword(), user.getPassword())) {
            log.info("user {} entered wrong old password", user.getEmail());
            throw new WrongPasswordException(i18nUtil.getMessage(Messages.USER_ERROR_OLD_PASSWORD));
        }

        if (!updatePasswordDto.getNewPassword().equals(updatePasswordDto.getConfirmNewPassword())) {
            log.info("user {} entered wrong confirm password ", user.getEmail());
            throw new PasswordsNotTheSameException(i18nUtil.getMessage(Messages.REGISTRATION_ERROR_PASSWORD_MISMATCH));
        }
        user.setPassword(passwordEncoder.encode(updatePasswordDto.getNewPassword()));
        userRepository.save(user);
        log.info("Password was updated successfully");
    }

    @Override
    @Transactional
    public UserDto updateUser(UpdateUserDto updateUserDto) {
        User user = userRepository.findById(updateUserDto.getId()).orElseThrow(() ->
                new UserNotFoundException(i18nUtil.getMessage(Messages.USER_ERROR_ID_NOT_FOUND, String.valueOf(updateUserDto.getId()))));

        Optional<User> userWithExistingEmail = userRepository.findByEmail(updateUserDto.getEmail());
        if (userWithExistingEmail.isPresent() && !updateUserDto.getId().equals(userWithExistingEmail.get().getId())) {
            log.error("Email {} is already in use", user.getEmail());
            throw new RegistrationFailedException(i18nUtil.getMessage(Messages.REGISTRATION_ERROR_EMAIL_EXISTS, updateUserDto.getEmail()));
        }

        Optional<User> userWithExistingPhoneNumber = userRepository.findByPhoneNumber(updateUserDto.getPhoneNumber());
        if (userWithExistingPhoneNumber.isPresent() && !updateUserDto.getId().equals(userWithExistingPhoneNumber.get().getId())) {
            log.error("Phone number {} is already in use", user.getPhoneNumber());
            throw new RegistrationFailedException(i18nUtil.getMessage(Messages.REGISTRATION_ERROR_PHONE_EXISTS, updateUserDto.getPhoneNumber()));
        }

        user.setEmail(updateUserDto.getEmail());
        user.setPhoneNumber(updateUserDto.getPhoneNumber());
        user.setUsername(updateUserDto.getUsername());
        User updatedUser = userRepository.save(user);
        log.info("Successfully updated user {}", updatedUser.getEmail());
        return userMapper.toDTO(updatedUser);
    }


}
