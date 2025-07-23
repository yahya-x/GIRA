package com.GIRA.Backend.mapper;

import com.GIRA.Backend.Entities.Role;
import com.GIRA.Backend.DTO.response.RoleResponse;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RoleMapper.
 */
class RoleMapperTest {
    @Test
    void testValidPermissionsJson() {
        Role role = new Role();
        role.setPermissions("[\"CREATE_USER\", \"DELETE_USER\"]");
        RoleResponse dto = RoleMapper.toResponse(role);
        List<String> expected = Arrays.asList("CREATE_USER", "DELETE_USER");
        assertEquals(expected, dto.getPermissions());
    }

    @Test
    void testEmptyPermissionsJson() {
        Role role = new Role();
        role.setPermissions("");
        RoleResponse dto = RoleMapper.toResponse(role);
        assertEquals(Collections.emptyList(), dto.getPermissions());
    }

    @Test
    void testNullPermissionsJson() {
        Role role = new Role();
        role.setPermissions(null);
        RoleResponse dto = RoleMapper.toResponse(role);
        assertEquals(Collections.emptyList(), dto.getPermissions());
    }

    @Test
    void testMalformedPermissionsJson() {
        Role role = new Role();
        role.setPermissions("not a json");
        RoleResponse dto = RoleMapper.toResponse(role);
        assertEquals(Collections.emptyList(), dto.getPermissions());
    }
} 