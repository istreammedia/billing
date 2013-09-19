package org.mifosplatform.billing.message.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BillingMessageTemplateRepository extends JpaRepository<BillingMessageTemplate, Long>, JpaSpecificationExecutor<BillingMessageTemplate>
{
}
