package com.example.swiftCodes.repository;

import com.example.swiftCodes.model.BankEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BankEntityRepository extends MongoRepository<BankEntity, String> {
    BankEntity findBySwiftCode(String swiftCode);

    List<BankEntity> findByCountryISO2(String countryISO2);

    void deleteBySwiftCode(String swiftCode);
}
