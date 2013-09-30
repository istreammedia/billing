package org.mifosplatform.billing.entitlements.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.entitlements.data.EntitlementsData;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;


@Service
public class EntitlementReadPlatformServiceImpl implements EntitlementReadPlatformService {

	
	private final JdbcTemplate jdbcTemplate;
	   
	@Autowired
	public EntitlementReadPlatformServiceImpl(final TenantAwareRoutingDataSource dataSource){
		 this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	
	@Override
	public List<EntitlementsData> getProcessingData(Long id) {
		// TODO Auto-generated method stub
		String sql="";
		ServicesMapper mapper = new ServicesMapper();
		if(id==null){
			sql = "select " + mapper.schema();
		} 
		else{
			sql = "select " + mapper.schema()+"order by id limit "+id;
		}
		List<EntitlementsData> detailsDatas= jdbcTemplate.query(sql, mapper, new Object[] {});
		
		return detailsDatas;
	}
	
	protected static final class ServicesMapper implements RowMapper<EntitlementsData> {

		@Override
		public EntitlementsData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum)
				throws SQLException {
			Long id=rs.getLong("id");
		//	String userName = rs.getString("provisioingSystem");
			//String planName=rs.getString("provisioingSystem");
			Long prepareReqId=rs.getLong("prepareRequestId");
			String requestType=rs.getString("requestType");
            String hardwareId=rs.getString("hardwareId");
            String provisioingSystem=rs.getString("provisioingSystem");
			
          return new EntitlementsData(id,null,null,prepareReqId,requestType,hardwareId,provisioingSystem);

		}
		public String schema() {
			return "p.id AS id,p.prepareRequest_id as prepareRequestId,p.provisioing_system as provisioingSystem,pr.hardware_id as hardwareId, p.request_type AS requestType " +
					"FROM b_process_request p,b_process_request_detail pr WHERE p.id=pr.processrequest_id AND p.is_processed = 'N' ";
}

}

	
	
}
