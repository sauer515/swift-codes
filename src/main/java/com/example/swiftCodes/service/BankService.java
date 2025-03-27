package com.example.swiftCodes.service;

import com.example.swiftCodes.csv.CsvParser;
import com.example.swiftCodes.exception.BankAlreadyExistsException;
import com.example.swiftCodes.exception.BankNotFoundException;
import com.example.swiftCodes.model.BankEntity;
import com.example.swiftCodes.model.Branch;
import com.example.swiftCodes.repository.BankEntityRepository;
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

    public BankEntity saveBank(BankEntity bank) {
        bankEntityRepository.findBySwiftCode(bank.getSwiftCode()).ifPresent(entity ->
        {
            throw new BankAlreadyExistsException("Bank " + bank.getSwiftCode() + "already exists");
        });
        return bankEntityRepository.save(bank);
    }

    public void importFromCsv(String filePath) {
        bankEntityRepository.saveAll(csvParser.parseCsvFile(filePath));
    }

    public List<BankEntity> findAll() {
        return bankEntityRepository.findAll();
    }

    public BankEntity findBySwiftCode(String swiftCode) {
        if (swiftCode == null) {
            throw new IllegalArgumentException("Swift code was not present");
        }
        return bankEntityRepository.findBySwiftCode(swiftCode)
                .orElseThrow(() -> new BankNotFoundException("Bank with " + swiftCode + " not found"));
    }

    public Branch findBranchBySwiftCode(String swiftCode) {
        BankEntity headquarter = bankEntityRepository.findBySwiftCode(swiftCode.substring(0,8)+"XXX").orElseThrow(() -> new BankNotFoundException("Bank with " + swiftCode + " not found"));
        return headquarter
                .getBranches()
                .stream()
                .filter(branch -> branch.getSwiftCode().equals(swiftCode))
                .findFirst()
                .orElseThrow(() -> new BankNotFoundException("Branch with " + swiftCode + " not found in " + headquarter.getBankName()));
    }

    public List<BankEntity> findByCountryISO2(String countryISO2) {
        return bankEntityRepository.findByCountryISO2(countryISO2);
    }

    public Branch saveBranch(Branch bank) {
        BankEntity headquarter = bankEntityRepository.findBySwiftCode(bank.getSwiftCode().substring(0, 8) + "XXX")
                .orElseThrow(() -> new BankNotFoundException("Bank with " + bank.getSwiftCode() + " not found"));
        if (headquarter.getBranches().contains(bank)) {
            throw new BankAlreadyExistsException("Branch already exists");
        }
        headquarter.getBranches().add(bank);
        bankEntityRepository.save(headquarter);
        return headquarter.getBranches().get(headquarter.getBranches().size() - 1);
    }

    public void deleteBySwiftCode(String swiftCode) {
        if (swiftCode.endsWith("XXX")) {
            bankEntityRepository.findBySwiftCode(swiftCode)
                    .orElseThrow(() -> new BankNotFoundException("Bank with " + swiftCode + " not found"));
            bankEntityRepository.deleteBySwiftCode(swiftCode);
            return;
        }
        BankEntity headquarter = bankEntityRepository.findBySwiftCode(swiftCode.substring(0, 8) + "XXX")
                .orElseThrow(() -> new BankNotFoundException("Bank with " + swiftCode + " not found"));
        if (!headquarter.getBranches().removeIf(branch -> branch.getSwiftCode().equals(swiftCode))) {
            throw new BankNotFoundException("Branch with " + swiftCode + " not found in " + headquarter.getBankName());
        }
        bankEntityRepository.save(headquarter);
    }
}
