package org.mifosplatform.billing.currency.domain;

import org.mifosplatform.billing.pricing.domain.CountryCurrency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CountryCurrencyRepository extends JpaRepository<CountryCurrency, Long>,
JpaSpecificationExecutor<CountryCurrency>{




}
