package org.mifosplatform.billing.adjustment.domain;

import java.util.ArrayList;
import java.util.List;

public class Discount {

	/*
	 * @SuppressWarnings("unused") private List<String> discountOptions =
	 * Arrays.asList("percentage","flatamount");
	*/
	private List<DiscountValues> discountOptions;

	public void setdiscount1() {
		discountOptions = new ArrayList<DiscountValues>();
		discountOptions.add(new DiscountValues("percentage"));
		discountOptions.add(new DiscountValues("flatamount"));
	}

	public void setadjustment_type() {

		discountOptions = new ArrayList<DiscountValues>();
		discountOptions.add(new DiscountValues("CREDIT"));
		discountOptions.add(new DiscountValues("DEBIT"));

	}

}
