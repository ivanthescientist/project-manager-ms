package com.isc.project.manager.controller;

import com.isc.project.manager.api.dto.UserDTO;
import com.isc.project.manager.api.exception.EntityNotFoundException;
import com.isc.project.manager.security.AuthenticatedUser;
import com.isc.project.manager.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<UserDTO> findAll(@AuthenticationPrincipal AuthenticatedUser user) {
        if(user.isAdmin()) {
            return userService.findAll();
        } else {
            return userService.findAllByTenantCode(user.getTenantCode());
        }
    }

    @RequestMapping(value = "/current", method = RequestMethod.GET)
    public UserDTO getCurrentUser(@AuthenticationPrincipal AuthenticatedUser user) {
        return userService.findById(user.getId())
                .orElseThrow(() -> new EntityNotFoundException("User", user.getId().toString()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public UserDTO findById(@PathVariable Long id) {
        return userService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User", id.toString()));
    }

    @RequestMapping(value = "", method = RequestMethod.POST)
    public UserDTO create(@RequestBody UserDTO userDTO) {
        return userService.create(userDTO);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public UserDTO update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        userDTO.setId(id);
        return userService.update(userDTO).orElseThrow(() -> new EntityNotFoundException("User", id.toString()));
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void delete(@PathVariable Long id) {
        userService.delete(id);
    }
}
