package org.mifosplatform.billing.order.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.Days;
import org.joda.time.LocalDate;
import org.mifosplatform.billing.allocation.service.AllocationReadPlatformService;
import org.mifosplatform.billing.association.domain.PlanHardwareMapping;
import org.mifosplatform.billing.association.service.HardwareAssociationWriteplatformService;
import org.mifosplatform.billing.billingorder.service.ReverseInvoice;
import org.mifosplatform.billing.contract.domain.Contract;
import org.mifosplatform.billing.contract.domain.SubscriptionRepository;
import org.mifosplatform.billing.discountmaster.domain.DiscountMaster;
import org.mifosplatform.billing.discountmaster.domain.DiscountMasterRepository;
import org.mifosplatform.billing.discountmaster.exceptions.DiscountMasterNoRecordsFoundException;
import org.mifosplatform.billing.eventorder.service.PrepareRequestWriteplatformService;
import org.mifosplatform.billing.onetimesale.data.AllocationDetailsData;
import org.mifosplatform.billing.order.data.OrderStatusEnumaration;
import org.mifosplatform.billing.order.domain.Order;
import org.mifosplatform.billing.order.domain.OrderDiscount;
import org.mifosplatform.billing.order.domain.OrderHistory;
import org.mifosplatform.billing.order.domain.OrderHistoryRepository;
import org.mifosplatform.billing.order.domain.OrderLine;
import org.mifosplatform.billing.order.domain.OrderPrice;
import org.mifosplatform.billing.order.domain.OrderPriceRepository;
import org.mifosplatform.billing.order.domain.OrderReadPlatformImpl;
import org.mifosplatform.billing.order.domain.OrderRepository;
import org.mifosplatform.billing.order.domain.PlanHardwareMappingRepository;
import org.mifosplatform.billing.order.exceptions.NoOrdersFoundException;
import org.mifosplatform.billing.order.exceptions.NoRegionalPriceFound;
import org.mifosplatform.billing.order.serialization.OrderCommandFromApiJsonDeserializer;
import org.mifosplatform.billing.plan.data.ServiceData;
import org.mifosplatform.billing.plan.domain.Plan;
import org.mifosplatform.billing.plan.domain.PlanRepository;
import org.mifosplatform.billing.plan.domain.StatusTypeEnum;
import org.mifosplatform.billing.plan.domain.UserActionStatusTypeEnum;
import org.mifosplatform.billing.pricing.data.PriceData;
import org.mifosplatform.billing.transactionhistory.service.TransactionHistoryWritePlatformService;
import org.mifosplatform.infrastructure.codes.exception.CodeNotFoundException;
import org.mifosplatform.infrastructure.configuration.domain.GlobalConfigurationProperty;
import org.mifosplatform.infrastructure.configuration.domain.GlobalConfigurationRepository;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;



@Service
public class OrderWritePlatformServiceImpl implements OrderWritePlatformService {
	
	private final PlatformSecurityContext context;
	private final OrderRepository orderRepository;
	private final PlanRepository planRepository;
	private final SubscriptionRepository subscriptionRepository;
	private final OrderPriceRepository OrderPriceRepository;
	private final JdbcTemplate jdbcTemplate;
	private final OrderCommandFromApiJsonDeserializer fromApiJsonDeserializer;
	private final PrepareRequestWriteplatformService prepareRequestWriteplatformService;
    private final DiscountMasterRepository discountMasterRepository;
    private final TransactionHistoryWritePlatformService transactionHistoryWritePlatformService;
    private final OrderHistoryRepository orderHistoryRepository;
    private final ClientRegionDetails  clientRegionDetails;
    private final ReverseInvoice reverseInvoice;
    private final GlobalConfigurationRepository configurationRepository;
    private final AllocationReadPlatformService allocationReadPlatformService; 
    private final HardwareAssociationWriteplatformService associationWriteplatformService;
    private final PlanHardwareMappingRepository hardwareMappingRepository;
    
    public final static String CONFIG_PROPERTY="Implicit Association";
    
    
    
	@Autowired
	public OrderWritePlatformServiceImpl(final PlatformSecurityContext context,final OrderRepository orderRepository,final ClientRegionDetails  clientRegionDetails,
			final PlanRepository planRepository,final OrderPriceRepository OrderPriceRepository,final TenantAwareRoutingDataSource dataSource,
			final SubscriptionRepository subscriptionRepository,final OrderCommandFromApiJsonDeserializer fromApiJsonDeserializer,final ReverseInvoice reverseInvoice,
			final PrepareRequestWriteplatformService prepareRequestWriteplatformService,final DiscountMasterRepository discountMasterRepository,
			final TransactionHistoryWritePlatformService transactionHistoryWritePlatformService,final OrderHistoryRepository orderHistoryRepository,
			final  GlobalConfigurationRepository configurationRepository,final AllocationReadPlatformService allocationReadPlatformService,
			final HardwareAssociationWriteplatformService associationWriteplatformService,final PlanHardwareMappingRepository hardwareMappingRepository) {
		
		this.context = context;
		this.orderRepository = orderRepository;
		this.OrderPriceRepository = OrderPriceRepository;
		this.planRepository = planRepository;
		this.prepareRequestWriteplatformService=prepareRequestWriteplatformService;
		this.subscriptionRepository = subscriptionRepository;
		this.fromApiJsonDeserializer=fromApiJsonDeserializer;
		this.discountMasterRepository=discountMasterRepository;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.transactionHistoryWritePlatformService = transactionHistoryWritePlatformService;
		this.orderHistoryRepository=orderHistoryRepository;
		this.clientRegionDetails=clientRegionDetails;
		this.reverseInvoice=reverseInvoice;
		this.configurationRepository=configurationRepository;
		this.allocationReadPlatformService=allocationReadPlatformService;
		this.associationWriteplatformService=associationWriteplatformService;
		this.hardwareMappingRepository=hardwareMappingRepository;

	}
	
	@Override
	public CommandProcessingResult createOrder(Long clientId,JsonCommand command) {
	
		try{
			context.authenticatedUser();
			this.fromApiJsonDeserializer.validateForCreate(command.json());
			List<OrderLine> serviceDetails = new ArrayList<OrderLine>();
			List<OrderPrice> orderprice = new ArrayList<OrderPrice>();
			List<PriceData> datas = new ArrayList<PriceData>();
			
			     OrderReadPlatformImpl obj = new OrderReadPlatformImpl(context,jdbcTemplate);
                 Order order=Order.fromJson(clientId,command);
			     Plan plan = this.planRepository.findOne(order.getPlanId());
			   
			//   String clientRegion=this.clientRegionDetails.getTheClientRegionDetails(clientId);
			List<ServiceData> details = obj.retrieveAllServices(order.getPlanId());

			datas=obj.retrieveAllPrices(order.getPlanId(),order.getBillingFrequency(),clientId);
			
			 if(datas.isEmpty()){
				 
				 datas=obj.retrieveDefaultPrices(order.getPlanId(),order.getBillingFrequency(),clientId);
			  }
			 
			 if(datas.isEmpty()){
				 
				  throw new NoRegionalPriceFound();
			  }
			
			 LocalDate endDate = null;
			Contract subscriptionData = this.subscriptionRepository.findOne(order.getContarctPeriod());
			LocalDate startDate=new LocalDate(order.getStartDate());
			Long orderStatus=null;
			
			if(plan.getProvisionSystem().equalsIgnoreCase("None")){
				
				orderStatus = OrderStatusEnumaration.OrderStatusType(StatusTypeEnum.ACTIVE).getId();
			
			}else{
			
				orderStatus = OrderStatusEnumaration.OrderStatusType(StatusTypeEnum.PENDING).getId();
			}
			//Calculate EndDate
			endDate = calculateEndDate(startDate,subscriptionData);
			
			order=new Order(order.getClientId(),order.getPlanId(),orderStatus,null,order.getBillingFrequency(),startDate, endDate,
					order.getContarctPeriod(), serviceDetails, orderprice,order.getbillAlign(),UserActionStatusTypeEnum.ACTIVATION.toString());
			
			BigDecimal priceforHistory=BigDecimal.ZERO;
			
			for (PriceData data : datas) {
				
				LocalDate billstartDate = startDate;
				LocalDate billEndDate = null;
				
				
				// end date is null for rc
				if (data.getChagreType().equalsIgnoreCase("RC")	&& endDate != null) {
					billEndDate = endDate;
				} else if(data.getChagreType().equalsIgnoreCase("NRC")) {
					billEndDate = billstartDate;
				}
				  final DiscountMaster discountMaster=this.discountMasterRepository.findOne(data.getDiscountId());
				  if(discountMaster == null){
					  throw new DiscountMasterNoRecordsFoundException();
				  }
				  
					  //If serviceId Not Exist
					  
				 OrderPrice price = new OrderPrice(data.getServiceId(),data.getChargeCode(), data.getCharging_variant(),
						data.getPrice(), null, data.getChagreType(),data.getChargeDuration(), data.getDurationType(),
						billstartDate.toDate(), billEndDate,data.isTaxInclusive());
				order.addOrderDeatils(price);
				priceforHistory=priceforHistory.add(data.getPrice());
				
				//discount Order
				OrderDiscount orderDiscount=new OrderDiscount(order,price,discountMaster.getId(),startDate.toDate(),endDate,
						discountMaster.getDiscountType(),discountMaster.getDiscountRate());
				price.addOrderDiscount(orderDiscount);
				
				
			}

			for (ServiceData data : details) {
				OrderLine orderdetails = new OrderLine(data.getPlanId(),
						data.getServiceType(), plan.getStatus(), 'n');
				order.addServiceDeatils(orderdetails);
			}
		     
			this.orderRepository.save(order);

			//Prepare a Requset For Order
			String requstStatus =UserActionStatusTypeEnum.ACTIVATION.toString();
			
			CommandProcessingResult processingResult=this.prepareRequestWriteplatformService.prepareNewRequest(order,plan,requstStatus);
			
			AppUser appUser=this.context.authenticatedUser();
			Long userId=appUser.getId();
			
			//For Order History
			OrderHistory orderHistory=new OrderHistory(order.getId(),new LocalDate(),new LocalDate(),processingResult.commandId(),requstStatus,userId);
			this.orderHistoryRepository.save(orderHistory);
			
          //For Plan And HardWare Association
			GlobalConfigurationProperty configurationProperty=this.configurationRepository.findOneByName(CONFIG_PROPERTY);
			
			//For Transaction History
			transactionHistoryWritePlatformService.saveTransactionHistory(order.getClientId(), "New Order", order.getStartDate(),"Price:"+priceforHistory,
			     "PlanId:"+order.getPlanId(),"contarctPeriod:"+order.getContarctPeriod(),"OrderID:"+order.getId(),
			     "BillingAlign:"+order.getbillAlign());
			
			if(configurationProperty.isEnabled()){
				
			    if(plan.isHardwareReq() == 'Y'){
			    	
			    	   PlanHardwareMapping hardwareMapping=this.hardwareMappingRepository.findOneByPlanCode(plan.getPlanCode());
			    	   
			    	   if(hardwareMapping!=null){
			    		   
			    		   List<AllocationDetailsData> allocationDetailsDatas=this.allocationReadPlatformService.retrieveHardWareDetailsByItemCode(clientId,hardwareMapping.getItemCode());
			    		   
			    		   if(!allocationDetailsDatas.isEmpty())
			    		   {
			    				this.associationWriteplatformService.createNewHardwareAssociation(clientId,plan.getId(),allocationDetailsDatas.get(0).getSerialNo(),order.getId());
			    				transactionHistoryWritePlatformService.saveTransactionHistory(order.getClientId(), "Implicit Association", new Date(),"Serial No:"
			    				+allocationDetailsDatas.get(0).getSerialNo(),"Item Code:"+hardwareMapping.getItemCode(),"Plan Code:"+plan.getPlanCode());
			    				
			    		   }
			    		   
			    	   }
			    	
			    }
			
			
			
		}
			return new CommandProcessingResult(order.getId());	
	
	}catch (DataIntegrityViolationException dve) {
		handleCodeDataIntegrityIssues(command, dve);
		return new CommandProcessingResult(Long.valueOf(-1));
	}
	}
	
	
	//Calculate EndDate
	public LocalDate calculateEndDate(LocalDate startDate,Contract subscriptionData) {
		
		LocalDate contractEndDate = null;
		if (subscriptionData.getSubscriptionType().equalsIgnoreCase("DAY(s)")) {
			
			 contractEndDate = startDate.plusDays(subscriptionData.getUnits().intValue() - 1);
		} else if (subscriptionData.getSubscriptionType().equalsIgnoreCase("MONTH(s)")) {
			
			 contractEndDate = startDate.plusMonths(subscriptionData.getUnits().intValue()).minusDays(1);
		} else if (subscriptionData.getSubscriptionType().equalsIgnoreCase("YEAR(s)")) {
			
			 contractEndDate = startDate.plusYears(subscriptionData.getUnits().intValue()).minusDays(1);
		} else if (subscriptionData.getSubscriptionType().equalsIgnoreCase("week(s)")) {
			
			 contractEndDate = startDate.plusWeeks(subscriptionData.getUnits().intValue()).minusDays(1);
		}
 
		return contractEndDate;
		
	}

	private void handleCodeDataIntegrityIssues(JsonCommand command,DataIntegrityViolationException dve) {
	}
	
	@Override
	public CommandProcessingResult updateOrderPrice(Long orderId,JsonCommand command) {
		try
		{
		 context.authenticatedUser();
	     final OrderPrice orderPrice = retrievePriceBy(orderId);
	     orderPrice.setPrice(command);
		 this.OrderPriceRepository.save(orderPrice);
		 Long id=orderPrice.getOrder().getId();
		 Order order=this.orderRepository.findOne(id);
		 
			AppUser appUser=this.context.authenticatedUser();
			Long userId=appUser.getId();
			 
			
			//For Order History
			OrderHistory orderHistory=new OrderHistory(order.getId(),new LocalDate(),new LocalDate(),null,"Update Price",userId);
	
			this.orderHistoryRepository.save(orderHistory);
		 
		 
         return new CommandProcessingResultBuilder() //
         .withCommandId(command.commandId()) //
         .withEntityId(orderId) //
         .with(null) //
         .build();
	} catch (DataIntegrityViolationException dve) {
		handleCodeDataIntegrityIssues(command, dve);
		return new CommandProcessingResult(Long.valueOf(-1));
	}
	}

	private OrderPrice retrievePriceBy(Long orderId) {
		 final OrderPrice orderPrice = this.OrderPriceRepository.findOne(orderId);
	        if (orderPrice == null) { throw new CodeNotFoundException(orderId.toString()); }
	        return orderPrice;
	}

	@Override
	public CommandProcessingResult deleteOrder(Long orderId, JsonCommand command) {
		
		Order order = this.orderRepository.findOne(orderId);
		
		List<OrderLine> orderline = order.getServices();
		List<OrderPrice> orderPrices=order.getPrice();
		for(OrderPrice price:orderPrices){
			price.delete();
		}
		for (OrderLine orderData : orderline) {
			orderData.delete();
		}
		order.delete();
		this.orderRepository.save(order);
		
		//For OrderHistory
		//String requstStatus = OrderStatusEnumaration.OrderStatusType(StatusTypeEnum.).getValue();
		

		AppUser appUser=this.context.authenticatedUser();
		Long userId=appUser.getId();
		 
		
		//For Order History
		OrderHistory orderHistory=new OrderHistory(order.getId(),new LocalDate(),new LocalDate(),null,"Cancelled",userId);
		
		this.orderHistoryRepository.save(orderHistory);
		
		
		transactionHistoryWritePlatformService.saveTransactionHistory(order.getClientId(), "OrderDelete", order.getEndDate(),"Price:"+order.getPrice(),"PlanId:"+order.getPlanId(),"contarctPeriod:"+order.getContarctPeriod(),"services"+order.getServices(),"OrderID:"+order.getId(),"BillingAlign:"+order.getbillAlign());
		return new CommandProcessingResult(order.getId());
	}

	@Override
	public CommandProcessingResult disconnectOrder(JsonCommand command,Long orderId ) {
		try {
			
			this.fromApiJsonDeserializer.validateForDisconnectOrder(command.json());
			Order order = this.orderRepository.findOne(orderId);
			LocalDate currentDate = new LocalDate();
			currentDate.toDate();
			List<OrderPrice> orderPrices=order.getPrice();
			for(OrderPrice price:orderPrices){
				price.updateDates(new LocalDate());
			}
			Plan plan=this.planRepository.findOne(order.getPlanId());
			Long orderStatus=null;
	         if(plan.getProvisionSystem().equalsIgnoreCase("None")){
				
				orderStatus = OrderStatusEnumaration.OrderStatusType(StatusTypeEnum.DISCONNECTED).getId();
			}else{
			
				orderStatus = OrderStatusEnumaration.OrderStatusType(StatusTypeEnum.PENDING).getId();
			}
	         
	         this.reverseInvoice.reverseInvoiceServices(orderId, order.getClientId(),new LocalDate());
			order.update(command,orderStatus);
			order.setuerAction(UserActionStatusTypeEnum.DISCONNECTION.toString());
			this.orderRepository.save(order);
			
			//for Prepare Request
			String requstStatus =UserActionStatusTypeEnum.DISCONNECTION.toString();
			CommandProcessingResult processingResult=this.prepareRequestWriteplatformService.prepareNewRequest(order,plan,requstStatus);
			
			//For Order History
			//String requstStatus = OrderStatusEnumaration.OrderStatusType(StatusTypeEnum.).getValue();
			
			AppUser appUser=this.context.authenticatedUser();
			Long userId=appUser.getId();
			 
			
			//For Order History
			OrderHistory orderHistory=new OrderHistory(order.getId(),new LocalDate(),new LocalDate(),processingResult.commandId(),requstStatus,userId);
			this.orderHistoryRepository.save(orderHistory);
 
			//for TransactionHistory
			transactionHistoryWritePlatformService.saveTransactionHistory(order.getClientId(),"ORDER_"+UserActionStatusTypeEnum.DISCONNECTION.toString(), order.getStartDate(),
					"Price:"+order.getPrice(),"PlanId:"+order.getPlanId(),"contarctPeriod:"+order.getContarctPeriod(),"services"+order.getServices(),"OrderID:"+order.getId(),"BillingAlign:"+order.getbillAlign());
			return new CommandProcessingResult(Long.valueOf(order.getId()));
		} catch (DataIntegrityViolationException dve) {
			handleCodeDataIntegrityIssues(null,dve);
			return new CommandProcessingResult(Long.valueOf(-1));
		}
	}

	@Override
	public CommandProcessingResult renewalClientOrder(JsonCommand command) {
		
		try{
			
			this.fromApiJsonDeserializer.validateForRenewalOrder(command.json());
			Order orderDetails=this.orderRepository.findOne(command.entityId());
			
			if(orderDetails == null){
				throw new NoOrdersFoundException(command.entityId());
			}
		    final Long contractPeriod = command.longValueOfParameterNamed("renewalPeriod");
		    
		    Contract contractDetails=this.subscriptionRepository.findOne(contractPeriod);
		    
		    //Get The Plan Details
		    Plan plan=this.planRepository.findOne(orderDetails.getPlanId());
		    
		    LocalDate newStartdate=new LocalDate(orderDetails.getEndDate());
		    LocalDate topUpDate=new LocalDate();
		    newStartdate=newStartdate.plusDays(1);
		    if(plan.isPrepaid() == 'Y'){
		    	  
		    	  if(topUpDate.isAfter(newStartdate)){
		    		  newStartdate=topUpDate;
		    	  }else{
		    		  
		    		  int days=Days.daysBetween(topUpDate, newStartdate).getDays();
		    		   newStartdate=newStartdate.plusDays(days);
		    	  }
		    }
		    
		 
		    LocalDate renewalEndDate=calculateEndDate(newStartdate,contractDetails);
		      orderDetails.setEndDate(renewalEndDate);
		      
		      this.orderRepository.save(orderDetails);
		      
				//For Order History
     		      AppUser appUser=this.context.authenticatedUser();
	   			  Long userId=appUser.getId();
				
				//For Order History
				OrderHistory orderHistory=new OrderHistory(orderDetails.getId(),new LocalDate(),newStartdate,null,"Renewal",userId);
				this.orderHistoryRepository.save(orderHistory);
				
		  	   return new CommandProcessingResult(Long.valueOf(orderDetails.getId()));
			
		}catch (DataIntegrityViolationException dve) {
			handleCodeDataIntegrityIssues(null,dve);
			return new CommandProcessingResult(Long.valueOf(-1));
		}
		
	
		
		
	
	}

	@Override
	public CommandProcessingResult reconnectOrder(Long orderId) {
	  try{
		  this.context.authenticatedUser();
		  Order order=this.orderRepository.findOne(orderId);
		  if(order == null){
			  throw new NoOrdersFoundException(orderId);
		  }
		  final LocalDate startDate=new LocalDate();
		  
		  Long contractId=order.getContarctPeriod();
		  
			  Contract contractPeriod=this.subscriptionRepository.findOne(contractId);
		
		  LocalDate EndDate=calculateEndDate(startDate,contractPeriod);
		   order.setStartDate(startDate);
		   order.setEndDate(EndDate);
		   
		   Plan plan=this.planRepository.findOne(order.getPlanId());
		      if(plan.getProvisionSystem().equalsIgnoreCase("None")){
					
		    	  order.setStatus(OrderStatusEnumaration.OrderStatusType(StatusTypeEnum.ACTIVE).getId());
				}else{
				
					  order.setStatus(OrderStatusEnumaration.OrderStatusType(StatusTypeEnum.PENDING).getId());
				}
		   
		 
		 //  order.setStatus(OrderStatusEnumaration.OrderStatusType(StatusTypeEnum.ACTIVE).getId());
		        order.setuerAction(UserActionStatusTypeEnum.RECONNECTION.toString());
		      this.orderRepository.save(order);
		   
		 
			  
			//for Prepare Request
			String requstStatus = UserActionStatusTypeEnum.RECONNECTION.toString().toString();
			CommandProcessingResult processingResult=this.prepareRequestWriteplatformService.prepareNewRequest(order,plan,requstStatus);
			
			//For Order History
			  
		      AppUser appUser=this.context.authenticatedUser();
				Long userId=appUser.getId();
			OrderHistory orderHistory=new OrderHistory(order.getId(),new LocalDate(),new LocalDate(),processingResult.commandId(),requstStatus,userId);
			this.orderHistoryRepository.save(orderHistory);
		
			//for TransactionHistory
			transactionHistoryWritePlatformService.saveTransactionHistory(order.getClientId(),"ORDER_"+UserActionStatusTypeEnum.RECONNECTION.toString(), order.getStartDate(),
					"Price:"+order.getPrice(),"PlanId:"+order.getPlanId(),"contarctPeriod:"+order.getContarctPeriod(),"services"+order.getServices(),"OrderID:"+order.getId(),"BillingAlign:"+order.getbillAlign());
			
		   return new CommandProcessingResult(order.getId());
		  
		  
	  }catch(DataIntegrityViolationException dve){
		  handleCodeDataIntegrityIssues(null, dve);
		  return new CommandProcessingResult(Long.valueOf(-1));
	  }
		
		
	}	
	}
	

