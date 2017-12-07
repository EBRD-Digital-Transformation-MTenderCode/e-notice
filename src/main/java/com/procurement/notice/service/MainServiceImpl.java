package com.procurement.notice.service;

import com.procurement.notice.model.dto.RequestDto;
import com.procurement.notice.model.dto.ResponseDto;
import com.procurement.notice.repository.PackageRepository;
import java.util.Objects;
import org.springframework.stereotype.Service;

@Service
public class MainServiceImpl implements MainService {

    private final PackageRepository packageRepository;

    public MainServiceImpl(final PackageRepository packageRepository) {
        this.packageRepository = packageRepository;
    }

    @Override
    public ResponseDto savePackage(final String cpId, final RequestDto data) {
        Objects.requireNonNull(data);
        return null;
    }
}
