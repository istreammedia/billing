package org.mifosplatform.billing.randomgenerator.domain;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RandomGenertatorRepository extends
JpaRepository<RandomGenerator, Long>,
JpaSpecificationExecutor<RandomGenerator>{

}
