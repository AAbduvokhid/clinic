package uz.snow.clinic.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.snow.clinic.common.exception.NotFoundException;
import uz.snow.clinic.user.model.entity.Role;
import uz.snow.clinic.user.model.enums.RoleName;
import uz.snow.clinic.user.repository.RoleRepository;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role findByName(RoleName name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("Role not found with name:  " + name));
    }
}
