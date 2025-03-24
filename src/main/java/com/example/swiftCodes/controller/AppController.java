package com.example.swiftCodes.controller;

import com.example.swiftCodes.model.BankEntity;
import com.example.swiftCodes.model.Branch;
import com.example.swiftCodes.service.BankService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/swift-codes")
public class AppController {
    private final BankService bankService;

    public AppController(BankService bankService) {
        this.bankService = bankService;
    }

    @GetMapping("/banks")
    public ResponseEntity<List<BankEntity>> getAllBanks() {
        // return ResponseEntity.ok(csvParser.parseCsvFile(csvConfig.getFilePath()));
        return ResponseEntity.ok(bankService.findAll()); // @todo after integrating database
    }

    @GetMapping("{swiftCode}")
    public ResponseEntity<?> getBankBySwiftCode(@PathVariable String swiftCode) {
        if (swiftCode == null) {
            return ResponseEntity.badRequest().build();
        }
        if (swiftCode.endsWith("XXX")) {
            BankEntity bank = bankService.findBySwiftCode(swiftCode);
            if (bank == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(bank);
        }
        Branch branch = bankService.findBranchBySwiftCode(swiftCode);
        if (branch == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(bankService.findBranchBySwiftCode(swiftCode));
    }

    @GetMapping("/country/{countryISO2code}")
    public ResponseEntity<List<BankEntity>> getBanksByCountry(@PathVariable String countryISO2code) {
        if (countryISO2code == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(bankService.findByCountryISO2(countryISO2code));
    }

    @PostMapping
    public ResponseEntity<String> addBank(@RequestBody BankEntity bank) {
        if (bank == null) {
            return ResponseEntity.badRequest().build();
        }
        if (bank.getSwiftCode().endsWith("XXX")) {
            bankService.save(bank);
            return ResponseEntity.ok("Bank added successfully");
        }
        Branch branch = new Branch(bank.getBankName(), bank.getSwiftCode(), bank.getAddress(), bank.getCountryISO2(), bank.getCountryName(), bank.isHeadquarter());
        bankService.saveBranch(branch);
        return ResponseEntity.ok("Branch added successfully");
    }

    @DeleteMapping("{swiftCode}")
    public ResponseEntity<String> deleteBank(@PathVariable String swiftCode) {
        if (swiftCode == null) {
            return ResponseEntity.badRequest().build();
        }
        bankService.deleteBySwiftCode(swiftCode);
        return ResponseEntity.ok("Bank deleted successfully");
    }
}
