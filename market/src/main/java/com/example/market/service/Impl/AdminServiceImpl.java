package com.example.market.service.Impl;

import com.example.market.dto.UserDto;
import com.example.market.exception.UserUpdatingException;
import com.example.market.i18n.I18nUtil;
import com.example.market.mapper.UserMapper;
import com.example.market.model.User;
import com.example.market.model.enums.Role;
import com.example.market.repository.UserRepository;
import com.example.market.service.AdminService;
import com.example.market.service.UserService;
import com.example.market.util.Messages;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AdminServiceImpl implements AdminService {

    UserRepository userRepository;
    UserService userService;
    UserMapper userMapper;
    I18nUtil i18nUtil;

    @Override
    @Transactional
    public void bunUser(UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        if (email.equals(userDto.getEmail())) {
            log.error("User try bun himself");
            throw new UserUpdatingException(i18nUtil.getMessage(Messages.USER_UPDATING_ERROR_CANNOT_BAN_SELF));
        }
        User user = userMapper.toEntity(userDto);
        log.info("{} {}", user.isBun() ? "ban user" : "unban", user.getEmail());
        user.setBun(!userDto.isBun());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void madeManager(UserDto userDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        if (email.equals(userDto.getEmail())) {
            log.error("User try add/remove role manager to himself");
            throw new UserUpdatingException(i18nUtil.getMessage(Messages.USER_UPDATING_ERROR_CANNOT_ADD_REMOVE_ROLE_SELF));
        }
        User user = userMapper.toEntity(userDto);
        if (!user.getRoleSet().add(Role.ROLE_MANAGER)) {
            log.info("User {} already has ROLE_MANAGER. Removing the role.", user.getUsername());
            user.getRoleSet().remove(Role.ROLE_MANAGER);
        } else {
            log.info("Adding ROLE_MANAGER to user {}.", user.getUsername());
        }
        userRepository.save(user);
        log.info("User {} has been updated successfully.", user.getUsername());
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        UserDto user = userService.findById(id);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = (String) authentication.getPrincipal();
        if (email.equals(user.getEmail())) {
            log.error("User try delete himself");
            throw new UserUpdatingException(i18nUtil.getMessage(Messages.USER_DELETING_ERROR_CANNOT_DELETE_SELF));
        }
        log.info("Try delete user with id {}", id);
        userRepository.deleteById(id);
        log.info("User was deleted successfully");
    }

}
