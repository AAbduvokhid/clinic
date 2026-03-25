package uz.snow.clinic.referrer.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.snow.clinic.common.exception.AlreadyExistsException;
import uz.snow.clinic.common.exception.NotFoundException;
import uz.snow.clinic.referrer.mapper.ReferrerMapper;
import uz.snow.clinic.referrer.model.dto.ReferrerResponse;
import uz.snow.clinic.referrer.model.dto.RegisterReferrerRequest;
import uz.snow.clinic.referrer.model.dto.UpdateReferrerRequest;
import uz.snow.clinic.referrer.model.entity.Referrer;
import uz.snow.clinic.referrer.repository.ReferrerRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReferrerService {
    private final ReferrerRepository referrerRepository;

    @Transactional(readOnly = true)
    public List<ReferrerResponse> findAll() {
        return ReferrerMapper.toResponseList(referrerRepository.findAll());

    }

    @Transactional(readOnly = true)
    public ReferrerResponse findById(Long id) {
        Referrer referrer = referrerRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of("Referrer", id));
        return ReferrerMapper.toResponse(referrer);
    }

    @Transactional(readOnly = true)
    public ReferrerResponse findByUniqueCode(String uniqueCode) {
        Referrer referrer = referrerRepository.findByUniqueCode(uniqueCode)
                .orElseThrow(() -> new NotFoundException
                        ("Referrer not found with unique code:" + uniqueCode));
        return ReferrerMapper.toResponse(referrer);
    }

    @Transactional
    public ReferrerResponse register(RegisterReferrerRequest request) {

        // DB level duplicate check — efficient, no full entity load
        if (referrerRepository.existsByUniqueCode(request.getUniqueCode())) {
            throw new AlreadyExistsException(
                    "Referrer with unique code:" + request.getUniqueCode() + " already exists"
            );
        }

        // Check duplicate phone too
        if (referrerRepository.existsByPhone(request.getPhone())) {
            throw new AlreadyExistsException(
                    "Referrer with phone :" + request.getPhone() + " already exists"
            );
        }
        Referrer referrer = Referrer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .uniqueCode(request.getUniqueCode())
                .phone(request.getPhone())
                .percentage(request.getPersentage())
                .build();

        Referrer savedReferrer = referrerRepository.save(referrer);
        log.info("Referrer '{}' registered successfully with code: {}",
                savedReferrer.getFirstName(), savedReferrer.getUniqueCode());

        return ReferrerMapper.toResponse(savedReferrer);
    }

    @Transactional
    public ReferrerResponse update(UpdateReferrerRequest request) {

        // Find existing or throw
        Referrer existing = referrerRepository.findById(request.getId())
                .orElseThrow(() -> NotFoundException.of("Referrer", request.getId()));

        // Check duplicate unique code — only if it is actually changing
        if (!existing.getUniqueCode().equals(request.getUniqueCode())
                && referrerRepository.existsByUniqueCode(request.getUniqueCode())) {
            throw new AlreadyExistsException(
                    "Referrer with unique code '" + request.getUniqueCode() + "' already exists");
        }

        // Check duplicate phone — only if it is actually changing
        if (!existing.getPhone().equals(request.getPhone())
                && referrerRepository.existsByPhone(request.getPhone())) {
            throw new AlreadyExistsException(
                    "Rederrer with phone '" + request.getPhone() + "' already exists");
        }
        // Update only allowed fields — set directly on existing entity
        existing.setFirstName(request.getFirstName());
        existing.setLastName(request.getLastName());
        existing.setUniqueCode(request.getUniqueCode());
        existing.setPhone(request.getPhone());
        existing.setPercentage(request.getPercentage());

        Referrer savedReferrer = referrerRepository.save(existing);
        log.info("Referrer '{}' updated successfully", savedReferrer.getFirstName());

        return ReferrerMapper.toResponse(savedReferrer);
    }

    // Delete with existence check
    @Transactional
    public void delete(Long id) {
        if (referrerRepository.existsById(id)) {
            throw NotFoundException.of("Referrer", id);
        }
        referrerRepository.deleteById(id);
        log.info("Referrer with  id '{}' deleted successfully ", id);
    }

}
