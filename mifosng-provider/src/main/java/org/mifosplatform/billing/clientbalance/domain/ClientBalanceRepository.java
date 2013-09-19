package org.mifosplatform.billing.clientbalance.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ClientBalanceRepository extends JpaRepository<ClientBalance, Long>, JpaSpecificationExecutor<ClientBalance>{

}
