package com.example.swiftCodes.service;

import com.example.swiftCodes.csv.CsvConfig;
import com.example.swiftCodes.csv.CsvParser;
import com.example.swiftCodes.model.BankEntity;
import com.example.swiftCodes.model.Branch;
import com.example.swiftCodes.repository.BankEntityRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {
    private final BankEntityRepository bankEntityRepository;
    private final CsvParser csvParser;

    public BankService(BankEntityRepository bankEntityRepository, CsvParser csvParser) {
        this.bankEntityRepository = bankEntityRepository;
        this.csvParser = csvParser;
    }

    public void saveAll(List<BankEntity> banks) {
        bankEntityRepository.saveAll(banks);
    }

    public void save(BankEntity bank) {
        if (bankEntityRepository.findBySwiftCode(bank.getSwiftCode()) != null) {
            throw new IllegalArgumentException("Bank already exists");
        }
        bankEntityRepository.save(bank);
    }

    public void importFromCsv(String filePath) {
        bankEntityRepository.saveAll(csvParser.parseCsvFile(filePath));
    }

    public List<BankEntity> findAll() {
        return bankEntityRepository.findAll();
    }

    public BankEntity findBySwiftCode(String swiftCode) {
        return bankEntityRepository.findBySwiftCode(swiftCode);
    }

    public Branch findBranchBySwiftCode(String swiftCode) {
        return bankEntityRepository.findBySwiftCode(swiftCode.substring(0, 8) + "XXX")
                .getBranches()
                .stream()
                .filter(branch -> branch.getSwiftCode().equals(swiftCode))
                .findFirst()
                .orElse(null);
    }

    public List<BankEntity> findByCountryISO2(String countryISO2) {
        return bankEntityRepository.findByCountryISO2(countryISO2);
    }

    public void saveBranch(Branch bank) {
        BankEntity headquarter = bankEntityRepository.findBySwiftCode(bank.getSwiftCode().substring(0, 8) + "XXX");
        if (headquarter == null) {
            throw new IllegalArgumentException("Headquarter not found in database");
        }
        if (findBranchBySwiftCode(bank.getSwiftCode()) != null) {
            throw new IllegalArgumentException("Branch already exists");
        }
        headquarter.getBranches().add(bank);
        bankEntityRepository.save(headquarter);
    }

    public void deleteBySwiftCode(String swiftCode) {
        if (swiftCode.endsWith("XXX")) {
            if (bankEntityRepository.findBySwiftCode(swiftCode) == null) {
                throw new IllegalArgumentException("Bank not found");
            }
            bankEntityRepository.deleteBySwiftCode(swiftCode);
            return;
        }
        BankEntity headquarter = bankEntityRepository.findBySwiftCode(swiftCode.substring(0, 8) + "XXX");
        if (headquarter == null) {
            throw new IllegalArgumentException("Bank not found");
        }
        if (!headquarter.getBranches().removeIf(branch -> branch.getSwiftCode().equals(swiftCode))) {
            throw new IllegalArgumentException("Branch not found");
        }
        bankEntityRepository.save(headquarter);
    }
}
