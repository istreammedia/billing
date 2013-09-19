package org.mifosplatform.billing.preparerequest.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.allocation.service.AllocationReadPlatformService;
import org.mifosplatform.billing.eventorder.domain.PrepareRequest;
import org.mifosplatform.billing.eventorder.domain.PrepareRequsetRepository;
import org.mifosplatform.billing.onetimesale.data.AllocationDetailsData;
import org.mifosplatform.billing.order.data.OrderStatusEnumaration;
import org.mifosplatform.billing.order.domain.Order;
import org.mifosplatform.billing.order.domain.OrderLine;
import org.mifosplatform.billing.order.domain.OrderRepository;
import org.mifosplatform.billing.plan.domain.StatusTypeEnum;
import org.mifosplatform.billing.preparerequest.data.PrepareRequestData;
import org.mifosplatform.billing.processrequest.domain.ProcessRequest;
import org.mifosplatform.billing.processrequest.domain.ProcessRequestDetails;
import org.mifosplatform.billing.processrequest.domain.ProcessRequestRepository;
import org.mifosplatform.billing.servicemaster.domain.ProvisionServiceDetails;
import org.mifosplatform.billing.servicemaster.domain.ProvisionServiceDetailsRepository;
import org.mifosplatform.infrastructure.core.domain.MifosPlatformTenant;
import org.mifosplatform.infrastructure.core.service.DataSourcePerTenantService;
import org.mifosplatform.infrastructure.core.service.ThreadLocalContextUtil;
import org.mifosplatform.infrastructure.security.service.TenantDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class PrepareRequestReadplatformServiceImpl  implements PrepareRequestReadplatformService{

	
	  private final TenantDetailsService tenantDetailsService;
	  private final DataSourcePerTenantService dataSourcePerTenantService;
	  private final OrderRepository orderRepository;
	  private final ProcessRequestRepository processRequestRepository;
	  private final PrepareRequsetRepository prepareRequsetRepository;
	  private final AllocationReadPlatformService allocationReadPlatformService;
	  private final ProvisionServiceDetailsRepository provisionServiceDetailsRepository;
	  public final static String PROVISIONGSYS_COMVENIENT="comvenient";
	

	    @Autowired
	    public PrepareRequestReadplatformServiceImpl(final DataSourcePerTenantService dataSourcePerTenantService,
	            final TenantDetailsService tenantDetailsService,final OrderRepository orderRepository,
		   final ProcessRequestRepository processRequestRepository,final AllocationReadPlatformService allocationReadPlatformService,
		   final PrepareRequsetRepository prepareRequsetRepository,final ProvisionServiceDetailsRepository provisionServiceDetailsRepository) {
	            
	    	    this.dataSourcePerTenantService = dataSourcePerTenantService;
	            this.tenantDetailsService = tenantDetailsService;
	            this.orderRepository=orderRepository;
	            this.processRequestRepository=processRequestRepository;
	            this.prepareRequsetRepository=prepareRequsetRepository;
	            this.allocationReadPlatformService=allocationReadPlatformService;
	            this.provisionServiceDetailsRepository=provisionServiceDetailsRepository;
	        
	    }

	
	@Override
	public List<PrepareRequestData> retrieveDataForProcessing() {
		try {
			
			  
	        final MifosPlatformTenant tenant = this.tenantDetailsService.loadTenantById("default");
	        ThreadLocalContextUtil.setTenant(tenant);
	        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourcePerTenantService.retrieveDataSource());
			final ClientOrderMapper mapper = new ClientOrderMapper();

			final String sql = "select " + mapper.clientOrderLookupSchema();

			return jdbcTemplate.query(sql, mapper, new Object[] { });
			} catch (EmptyResultDataAccessException e) {
			return null;
			}

			}

			private static final class ClientOrderMapper implements RowMapper<PrepareRequestData> {

			public String clientOrderLookupSchema() {
			return " pr.id AS id,pr.client_id AS clientId,pr.order_id AS orderId,pr.provisioning_sys AS provisioningSystem,c.firstname AS userName," +
					"p.is_hw_req as hwRequired,pw.plan_code AS planName,pr.request_type AS requestType FROM b_prepare_request pr,m_client c,b_hw_plan_mapping pw," +
					" b_orders o, b_plan_master p WHERE pr.client_id = c.id AND o.plan_id =p.id and p.plan_code = pw.plan_code AND pr.order_id = o.id" +
					" AND (pr.is_provisioning = 'N' OR pr.status = 'PENDING')";
			}

			@Override
			public PrepareRequestData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

			final Long id = rs.getLong("id");
			final Long clientId = rs.getLong("clientId");
			final Long orderId = rs.getLong("orderId");
			final String requestType = rs.getString("requestType");
		    final String planName=rs.getString("planName");	
			
			final String userName=rs.getString("userName");
			final String ishwReq=rs.getString("hwRequired");
			final String provisioningSys=rs.getString("provisioningSystem");
			
			return new PrepareRequestData(id, clientId,orderId, requestType,null,userName,provisioningSys,planName,ishwReq);
			}
			}
			
			@Override
			public List<Long> retrieveRequestClientOrderDetails(Long clientId) {
				try {
				
				  final MifosPlatformTenant tenant = this.tenantDetailsService.loadTenantById("default");
			        ThreadLocalContextUtil.setTenant(tenant);
			        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSourcePerTenantService.retrieveDataSource());
			        
			        
				OrderIdMapper planIdMapper = new OrderIdMapper();
				String sql = "select" + planIdMapper.planIdSchema();
				return jdbcTemplate.query(sql, planIdMapper,new Object[] { clientId });
				
			
			} catch (EmptyResultDataAccessException e) {
				return null;
				}
			}
			private static final class OrderIdMapper implements RowMapper<Long> {

				@Override
				public Long  mapRow(ResultSet resultSet, int rowNum) throws SQLException {
//					Long orderId = resultSet.getLong("orderId");
//					String durationType = resultSet.getString("durationType");
//					Date billStartDate = resultSet.getDate("billStartDate");
					return resultSet.getLong("orderId");
				}
				

				public String planIdSchema() {
					return "  os.id as orderId FROM b_orders os where os.client_id=?";
							
				}
				
				
			}

			@Override
			public void processingClientDetails(PrepareRequestData requestData) {

				
					
					 final MifosPlatformTenant tenant = this.tenantDetailsService.loadTenantById("default");
				        ThreadLocalContextUtil.setTenant(tenant);
				        
					  Order order=this.orderRepository.findOne(requestData.getOrderId());
					 AllocationDetailsData detailsData=this.allocationReadPlatformService.getTheHardwareItemDetails(requestData.getOrderId());
					 
					 if(requestData.getIshardwareReq().equalsIgnoreCase("Y") && detailsData == null){
						 
						  PrepareRequest prepareRequest=this.prepareRequsetRepository.findOne(requestData.getRequestId());
						  
						      String status=OrderStatusEnumaration.OrderStatusType(StatusTypeEnum.PENDING).getValue().toString();
         	                         prepareRequest.setStatus(status);
	                             this.prepareRequsetRepository.save(prepareRequest);
	                         
	                         //Update Order Status
	                         order.setStatus(OrderStatusEnumaration.OrderStatusType(StatusTypeEnum.PENDING).getId());
	                         this.orderRepository.saveAndFlush(order);
						 
					 }else if(requestData.getProvisioningSystem().equalsIgnoreCase(PROVISIONGSYS_COMVENIENT)){

						 ProcessRequest processRequest=new ProcessRequest(order.getClientId(), order.getId(), requestData.getProvisioningSystem(),
								 'N',requestData.getUserName(),requestData.getRequestType(),requestData.getRequestId());
					  
					          List<OrderLine> orderLineData=order.getServices();
					          for(OrderLine orderLine:orderLineData){
						  
						  //ProcessRequestDetails processRequestDetails=new ProcessRequestDetails(orderLine.getId(),orderLine.getServiceId(),"Sent","Recieved",'N');
						  String HardWareId=null;
						  if(detailsData!=null){
							  HardWareId=detailsData.getSerialNo();
						  }
						  
						  ProvisionServiceDetails provisionServiceDetails=this.provisionServiceDetailsRepository.findOneByServiceId(orderLine.getServiceId());
						  if(provisionServiceDetails!=null){
							  
						  ProcessRequestDetails processRequestDetails=new ProcessRequestDetails(orderLine.getId(),orderLine.getServiceId(),provisionServiceDetails.getServiceIdentification(),"Recieved",
								  HardWareId,order.getStartDate(),order.getEndDate(),null,null,'N');
						  processRequest.add(processRequestDetails);
						  }
					  }
	                       this.processRequestRepository.save(processRequest);				
	                       PrepareRequest prepareRequest=this.prepareRequsetRepository.findOne(requestData.getRequestId());
                           prepareRequest.updateProvisioning();
                           this.prepareRequsetRepository.save(prepareRequest);
                         //UPdate Order Status After Activating the Order
                         //  order.setStatus(StatusTypeEnum.ACTIVE.getValue().longValue());
                          // this.orderRepository.save(order);
                           
				}
					 if(requestData.getProvisioningSystem().equalsIgnoreCase("None")){
						 order.setStatus(new Long(1));
						 this.orderRepository.save(order);
					 }
	              
				}
				
			
}
