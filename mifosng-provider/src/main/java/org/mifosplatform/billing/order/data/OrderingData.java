package org.mifosplatform.billing.order.data;


public class OrderingData {

	private final OrderData data;

	public OrderingData(final OrderData datas)
	{
		this.data=datas;
	}

	public OrderData getData() {
		return data;
	}


}
