package com.ctecx.brs.tenant.util;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RegisteredComputerService {

    @Autowired
    private RegisteredComputerRepository registeredComputerRepository;

    public RegisteredComputer registerComputer(String computerName, String ipAddress, String macAddress, String description) {
        RegisteredComputer computer = new RegisteredComputer();
        computer.setComputerName(computerName);
        computer.setIpAddress(ipAddress);
        computer.setMacAddress(macAddress);
        computer.setDescription(description);
        computer.setRegistrationDate(LocalDateTime.now());
        computer.setActive(true);
        return registeredComputerRepository.save(computer);
    }

    public List<RegisteredComputer> getAllRegisteredComputers() {
        return registeredComputerRepository.findAll();
    }

    public void deactivateComputer(Long id) {
        registeredComputerRepository.findById(id).ifPresent(computer -> {
            computer.setActive(false);
            registeredComputerRepository.save(computer);
        });
    }
}