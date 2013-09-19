package org.mifosplatform.billing.adjustment.data;

import java.util.Collection;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.adjustment.domain.Discount;

public class AdjustmentCodeData {

	private final Collection<Discount> datass;
	private final List<AdjustmentData> data;
	private final LocalDate adjustment_date;

	public AdjustmentCodeData(Collection<Discount> datass,
			List<AdjustmentData> data) {
		this.data=data;
		this.datass=datass;
		this.adjustment_date=new LocalDate();
	}

	public Collection<Discount> getDatass() {
		return datass;
	}

	public List<AdjustmentData> getData() {
		return data;
	}

	public LocalDate getStartDate() {
		return adjustment_date;
	}


}
