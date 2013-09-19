package org.mifosplatform.billing.payments.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ChequePaymentRepository extends JpaRepository<ChequePayment, Long>,
JpaSpecificationExecutor<ChequePayment> {

}
