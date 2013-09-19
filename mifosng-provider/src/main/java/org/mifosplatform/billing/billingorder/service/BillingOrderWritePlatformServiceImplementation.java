package org.mifosplatform.billing.billingorder.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.billingorder.commands.BillingOrderCommand;
import org.mifosplatform.billing.billingorder.commands.InvoiceCommand;
import org.mifosplatform.billing.billingorder.commands.InvoiceTaxCommand;
import org.mifosplatform.billing.billingorder.domain.BillingOrder;
import org.mifosplatform.billing.billingorder.domain.BillingOrderRepository;
import org.mifosplatform.billing.billingorder.domain.Invoice;
import org.mifosplatform.billing.billingorder.domain.InvoiceRepository;
import org.mifosplatform.billing.billingorder.domain.InvoiceTax;
import org.mifosplatform.billing.billingorder.domain.InvoiceTaxRepository;
import org.mifosplatform.billing.billingorder.exceptions.BillingOrderNoRecordsFoundException;
import org.mifosplatform.billing.clientbalance.data.ClientBalanceData;
import org.mifosplatform.billing.clientbalance.domain.ClientBalance;
import org.mifosplatform.billing.clientbalance.domain.ClientBalanceRepository;
import org.mifosplatform.billing.clientbalance.service.UpdateClientBalance;
import org.mifosplatform.billing.order.domain.Order;
import org.mifosplatform.billing.order.domain.OrderPrice;
import org.mifosplatform.billing.order.domain.OrderPriceRepository;
import org.mifosplatform.billing.order.domain.OrderRepository;
import org.mifosplatform.billing.transactionhistory.service.TransactionHistoryWritePlatformService;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BillingOrderWritePlatformServiceImplementation implements BillingOrderWritePlatformService {

	private final static Logger logger = LoggerFactory.getLogger(BillingOrderWritePlatformServiceImplementation.class);

	private final PlatformSecurityContext context;
	private final BillingOrderRepository invoiceChargeRepository;
	private final InvoiceTaxRepository invoiceTaxRepository;
	private final InvoiceRepository invoiceRepository;
	private final OrderRepository orderRepository;
	private final UpdateClientBalance updateClientBalance;
	private final ClientBalanceRepository clientBalanceRepository;
	private final TransactionHistoryWritePlatformService transactionHistoryWritePlatformService; 
	
	@Autowired
	public BillingOrderWritePlatformServiceImplementation(final PlatformSecurityContext context,final BillingOrderRepository invoiceChargeRepository,
			final OrderPriceRepository orderPriceRepository,final InvoiceTaxRepository invoiceTaxRepository,final InvoiceRepository invoiceRepository,
			final OrderRepository orderRepository,final UpdateClientBalance updateClientBalance,
			final ClientBalanceRepository clientBalanceRepository,final TransactionHistoryWritePlatformService transactionHistoryWritePlatformService) {

		this.context = context;
		this.invoiceChargeRepository = invoiceChargeRepository;
		this.invoiceTaxRepository = invoiceTaxRepository;
		this.invoiceRepository = invoiceRepository;
		this.orderRepository = orderRepository;
		this.updateClientBalance = updateClientBalance;
		this.clientBalanceRepository = clientBalanceRepository;
		this.transactionHistoryWritePlatformService=transactionHistoryWritePlatformService;
	}

	@Transactional
	@Override
	public List<BillingOrder> createBillingProduct(
			List<BillingOrderCommand> billingOrderCommands) {
		// check whether list size is zero
		List<BillingOrder> listOfBillingOrders = null;
		if (billingOrderCommands.size() != 0) {
			listOfBillingOrders = new ArrayList<BillingOrder>();
			for (BillingOrderCommand billingOrderCommand : billingOrderCommands) {
				BigDecimal discount = BigDecimal.ZERO;
				BigDecimal netCharge = billingOrderCommand.getPrice().add(
						discount);
				BillingOrder order = new BillingOrder(
						billingOrderCommand.getClientId(),
						billingOrderCommand.getClientOrderId(),
						billingOrderCommand.getOrderPriceId(),
						billingOrderCommand.getChargeCode(),
						billingOrderCommand.getChargeType(), "abc",
						billingOrderCommand.getPrice(), discount, netCharge,
						billingOrderCommand.getStartDate(),
						billingOrderCommand.getEndDate());

				/*for (InvoiceTax invoiceTax : billingOrderCommand.getListOfTax()) {
					//invoiceTax.setInvoiceChargeId(order.getId());
					//this.invoiceTaxRepository.save(invoiceTax);
				}*/

				listOfBillingOrders.add(order);
			}

		} else if (billingOrderCommands.size() == 0) {
			throw new BillingOrderNoRecordsFoundException();
		}
		return listOfBillingOrders;
	}

	@Transactional
	@Override
	public CommandProcessingResult updateBillingOrder(
			List<BillingOrderCommand> commands) {
		Order clientOrder = null;
		for (BillingOrderCommand billingOrderCommand : commands) {
			clientOrder = this.orderRepository.findOne(billingOrderCommand.getClientOrderId());

			if (clientOrder != null) {

				// if(billingOrderCommand.getChargeType().equalsIgnoreCase("RC")){
				clientOrder.setNextBillableDay(billingOrderCommand.getNextBillableDate());
				// }else
				// if(billingOrderCommand.getChargeType().equalsIgnoreCase("NRC")){
				// clientOrder.setNextBillableDay(new
				// LocalDate("3099-12-12").toDate());
				// }
				
			}
		}

		// clientOrder.setEndDate(command.getEndDate());
		this.orderRepository.save(clientOrder);

		return new CommandProcessingResult(Long.valueOf(clientOrder.getId()));

	}

	@Override
	public CommandProcessingResult updateOrderPrice(
			List<BillingOrderCommand> billingOrderCommands) {
		Order orderData = null;
		for (BillingOrderCommand billingOrderCommand : billingOrderCommands) {
			orderData = this.orderRepository.findOne(billingOrderCommand
					.getClientOrderId());
			List<OrderPrice> orderPrices = orderData.getPrice();

			for (OrderPrice orderPriceData : orderPrices) {
				if (orderPriceData.getChargeType().equalsIgnoreCase("RC")
						&& billingOrderCommand.getChargeType()
								.equalsIgnoreCase("RC")) {

					orderPriceData.setInvoiceTillDate(billingOrderCommand
							.getInvoiceTillDate());
					orderPriceData.setNextBillableDay(billingOrderCommand
							.getNextBillableDate());

				} else if (orderPriceData.getChargeType().equalsIgnoreCase(
						"NRC")
						&& billingOrderCommand.getChargeType()
								.equalsIgnoreCase("NRC")) {
					orderPriceData.setInvoiceTillDate(billingOrderCommand
							.getInvoiceTillDate());
					orderPriceData.setNextBillableDay(billingOrderCommand
							.getNextBillableDate());
				}
			}
			this.orderRepository.save(orderData);
		}

		return new CommandProcessingResult(Long.valueOf(orderData.getId()));
	}

	@Override
	public List<InvoiceTax> createInvoiceTax(List<InvoiceTaxCommand> commands) {
		List<InvoiceTax> invoiceTaxes = new ArrayList<InvoiceTax>();
		if (commands != null) {
			/*for (InvoiceTaxCommand invoiceTaxCommand : commands) {
				InvoiceTax invoiceTax = new InvoiceTax(0l, 0l,
						invoiceTaxCommand.getTaxCode(), null,
						invoiceTaxCommand.getTaxPercentage(),
						invoiceTaxCommand.getTaxAmount());
				invoiceTax = this.invoiceTaxRepository.save(invoiceTax);
				invoiceTaxes.add(invoiceTax);
			}*/
		}

		return invoiceTaxes;
	}

	@Override
	public Invoice createInvoice(InvoiceCommand invoiceCommand,
			List<ClientBalanceData> clientBalanceDatas) {

		Invoice invoice = new Invoice(invoiceCommand.getClientId(),
				new LocalDate().toDate(), invoiceCommand.getInvoiceAmount(),
				invoiceCommand.getNetChargeAmount(),
				invoiceCommand.getTaxAmount(),
				invoiceCommand.getInvoiceStatus(),
				invoiceCommand.getCreatedBy(), invoiceCommand.getCreatedDate(),
				invoiceCommand.getLastModifiedDate(),
				invoiceCommand.getLastModifiedId());
		
		List<BillingOrder> charges = new ArrayList<BillingOrder>();
		

		Long clientBalanceId = null;
		if (clientBalanceDatas.size() >= 1) {
			clientBalanceId = clientBalanceDatas.get(0).getId();
		}

		ClientBalance clientBalance = null;
		if (clientBalanceId != null) {
			clientBalance = this.clientBalanceRepository.findOne(clientBalanceId);
		}

		if (clientBalance != null) {

			clientBalance = updateClientBalance.calculateUpdateClientBalance("DEBIT",
					invoice.getInvoiceAmount(),	clientBalance);
		} else if (clientBalance == null) {
			clientBalance = updateClientBalance.calculateCreateClientBalance("DEBIT",
					invoice.getInvoiceAmount(), clientBalance,invoice.getClientId());
		}

		this.clientBalanceRepository.save(clientBalance);
		invoice = this.invoiceRepository.save(invoice);

		transactionHistoryWritePlatformService.saveTransactionHistory(invoice.getClientId(), "Invoice", invoice.getInvoiceDate(),"InvoiceID:"+invoice.getId(),
				"AmountPaid:"+invoice.getInvoiceAmount(),"InvoiceStatus:"+invoice.getInvoiceStatus(),"NetChargeAmount:"+invoice.getNetChargeAmount(),"TaxAmount:"+invoice.getTaxAmount());
		
		return invoice;
	}

	@Override
	public void updateInvoiceTax(Invoice invoice,
			List<BillingOrderCommand> billingOrderCommands,
			List<BillingOrder> orders) {

		List<List<InvoiceTax>> listOfListOfTaxes = new ArrayList<List<InvoiceTax>>();
		for (BillingOrderCommand billingCommand : billingOrderCommands) {
			List<InvoiceTaxCommand> listOfTaxes = billingCommand.getListOfTax();
			//listOfListOfTaxes.add(listOfTaxes);
		}
		for (BillingOrder billingOrder : orders) {

			for (List<InvoiceTax> listOfTaxs : listOfListOfTaxes) {
				for (InvoiceTax invoiceTax : listOfTaxs) {
					// invoiceTax.setInvoiceChargeId(billingOrder.getId());
					//invoiceTax.setInvoiceId(invoice.getId());
					this.invoiceTaxRepository.save(invoiceTax);
				}
			}

		}
	}

	@Override
	public void updateInvoiceCharge(Invoice invoice,
			List<BillingOrder> billingOrders) {
		for (BillingOrder billingOrder : billingOrders) {
			//billingOrder.setInvoiceId(invoice.getId());
			this.invoiceChargeRepository.save(billingOrder);

		}
	}

	@Override
	public void updateClientBalance(Invoice invoice,
			List<ClientBalanceData> clientBalancesDatas) {
		Long clientBalanceId = null;
		if (clientBalancesDatas.size() >= 1) {
			clientBalanceId = clientBalancesDatas.get(0).getId();
		}

		ClientBalance clientBalance = null;
		if (clientBalanceId != null) {
			clientBalance = this.clientBalanceRepository.findOne(clientBalanceId);
		}

		if (clientBalance != null) {

			clientBalance = updateClientBalance.calculateUpdateClientBalance("DEBIT",
					invoice.getInvoiceAmount(),	clientBalance);
		} else if (clientBalance == null) {
			clientBalance = updateClientBalance.calculateCreateClientBalance("DEBIT",
					invoice.getInvoiceAmount(), clientBalance,invoice.getClientId());
		}

		this.clientBalanceRepository.save(clientBalance);
		
	}

}
