package org.mifosplatform.billing.action.service;

import java.util.List;

import org.mifosplatform.billing.action.data.ActionDetaislData;
import org.mifosplatform.billing.action.domain.EventAction;
import org.mifosplatform.billing.action.domain.EventActionRepository;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;


@Service
public class EventActionWritePlatformServiceImpl implements ActiondetailsWritePlatformService{
	
	
	
	private final JdbcTemplate jdbcTemplate;
	private final EventActionRepository eventActionRepository;
 //  private MyStoredProcedure myStoredProcedure; 
	

	@Autowired
	public EventActionWritePlatformServiceImpl(final TenantAwareRoutingDataSource dataSource,
			final EventActionRepository eventActionRepository)
	{
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.eventActionRepository=eventActionRepository;
		 //this. myStoredProcedure=new MyStoredProcedure(this.jdbcTemplate.getDataSource());
	}
	
	@Override
	public void AddNewActions(List<ActionDetaislData> actionDetaislDatas,final Long clientId) {
    try{
    	
    
		
		if(actionDetaislDatas!=null){
			
			for(ActionDetaislData detaislData:actionDetaislDatas){
				  if(detaislData.getActionType().equalsIgnoreCase("Send Mail")){
					 
					  this.jdbcTemplate.update("call "+detaislData.getProcedureName()+" (?,?,?,@error)",new Object[] {clientId,28,29});
					  
				//	MyStoredProcedure sproc = new MyStoredProcedure();
				    /*   Map<String, Object> string= myStoredProcedure.execute(clientId);
				       
				       boolean string2= ((Boolean) string.get("status")).booleanValue();
				       System.out.println(string2);
				    */   
				      //  printMap(results);
				        
					 
					  /*   @SuppressWarnings("rawtypes")
					   Map<String, Object> parameters = new HashMap();
				        parameters.put("keyid", clientId);
				        parameters.put("eventid", 28);
				        parameters.put("actionid", 29);
				   //     parameters.put("error_status", null);
				    
					 MyStoredProcedure myStoredProcedure=new MyStoredProcedure(detaislData,clientId,this.jdbcTemplate.getDataSource());
                     myStoredProcedure.execute(parameters);*/
					  
					    /* private EmployeeSP sproc=new EmployeeSP();

					     #   public String getEmployeeName(int emp_id)  
					     #    {   
					     #     return (String) sproc.execute(emp_id);   
					     #     }   */


					  
				  }else{
					  this.jdbcTemplate.update("call "+detaislData.getProcedureName()+" (?,?,?,@error)",new Object[] {clientId,28,29});
				  }
				  
				  EventAction eventAction=this.eventActionRepository.findOne(detaislData.getId());
				  eventAction.update();
				  this.eventActionRepository.save(eventAction);
				
			}
		}
		
		

		
		
    }catch(Exception exception){
    	exception.printStackTrace();
    }
    
   
	}}
	/* @SuppressWarnings("unused")
		final class MyStoredProcedure extends StoredProcedure {
		        
		  final static String SQL ="custom_validate_eventorders";//.getProcedureName();

			public MyStoredProcedure(DataSource dataSource) {
				 super(dataSource,SQL);  				 declareParameter( new SqlParameter( "clientid", Types.INTEGER) ); //declaring sql in parameter to pass input   
			      declareParameter( new SqlOutParameter( "status", Types.BOOLEAN ) ); //declaring sql out parameter   
			      compile();     
		        }
				
					@SuppressWarnings("rawtypes")
				public Map execute(int emp_id) {
						MyStoredProcedure myStoredProcedure;
						  Map<String, Object> results = super.execute(emp_id);   
						   return (Map) results.get("name"); 
		    }
					}*/
	 


