package com.vtt.controller;


import com.vtt.commonfunc.TokenUtils;

import com.vtt.dtoforSrc.GroupedRoleDto;
import com.vtt.entities.GroupedRole;
import com.vtt.entities.SRCRole;
import com.vtt.entities.User;
import com.vtt.otherclass.MainRole;
import com.vtt.repository.GroupedRoleRepository;
import com.vtt.repository.SRCRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/grouped-roles")
@RequiredArgsConstructor
public class GroupedRoleController {

    private final GroupedRoleRepository groupedRoleRepository;
    private final SRCRoleRepository srcRoleRepository;
    private final TokenUtils tokenUtils;

    @PostMapping
    public ResponseEntity<?> addGroupedRole(@RequestHeader("Authorization") String tokenHeader,
                                            @RequestBody GroupedRoleDto dto) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
//            if (requestingUser.getMainRole() != MainRole.ADMIN) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                        .body("Only ADMIN can access this endpoint");
//            }

            Optional<SRCRole> mainRoleOpt = srcRoleRepository.findById(dto.getRoleId());
            if (mainRoleOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Main role not found");
            }

            List<SRCRole> downlinkedRoles = srcRoleRepository.findAllById(dto.getDownlinkedRoleIds());
            if (downlinkedRoles.size() != dto.getDownlinkedRoleIds().size()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Some downlinked roles not found");
            }

            GroupedRole groupedRole = new GroupedRole();
            groupedRole.setRole(mainRoleOpt.get());
            groupedRole.setDownlinkedRoles(downlinkedRoles);

            GroupedRole saved = groupedRoleRepository.save(groupedRole);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateGroupedRole(@RequestHeader("Authorization") String tokenHeader,
                                               @PathVariable String id,
                                               @RequestBody GroupedRoleDto dto) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
            }

            Optional<GroupedRole> existing = groupedRoleRepository.findById(id);
            if (existing.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("GroupedRole not found");
            }

            Optional<SRCRole> mainRoleOpt = srcRoleRepository.findById(dto.getRoleId());
            if (mainRoleOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Main role not found");
            }

            List<SRCRole> downlinkedRoles = srcRoleRepository.findAllById(dto.getDownlinkedRoleIds());
            if (downlinkedRoles.size() != dto.getDownlinkedRoleIds().size()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Some downlinked roles not found");
            }

            GroupedRole groupedRole = existing.get();
            groupedRole.setRole(mainRoleOpt.get());
            groupedRole.setDownlinkedRoles(downlinkedRoles);

            GroupedRole updated = groupedRoleRepository.save(groupedRole);
            return ResponseEntity.ok(updated);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllGroupedRoles(@RequestHeader("Authorization") String tokenHeader) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
//            if (requestingUser.getMainRole() != MainRole.ADMIN) {
//                return ResponseEntity.status(HttpStatus.FORBIDDEN)
//                        .body("Only ADMIN can access this endpoint");
//            }

            return ResponseEntity.ok(groupedRoleRepository.findAll());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroupedRole(@RequestHeader("Authorization") String tokenHeader,
                                               @PathVariable String id) {
        try {
            User requestingUser = tokenUtils.getUserFromToken(tokenHeader);
            if (requestingUser.getMainRole() != MainRole.ADMIN) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Only ADMIN can access this endpoint");
            }

            if (!groupedRoleRepository.existsById(id)) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("GroupedRole not found");
            }

            groupedRoleRepository.deleteById(id);
            return ResponseEntity.ok("GroupedRole deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: " + e.getMessage());
        }
    }
}
