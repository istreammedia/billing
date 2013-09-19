package org.mifosplatform.billing.clientprospect.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.clientprospect.data.ClientProspectData;
import org.mifosplatform.billing.clientprospect.data.ProspectDetailAssignedToData;
import org.mifosplatform.billing.clientprospect.data.ProspectDetailData;
import org.mifosplatform.billing.clientprospect.data.ProspectPlanCodeData;
import org.mifosplatform.billing.pricing.service.PriceReadPlatformService;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DaoSupport;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.stereotype.Service;

@Service
public class ClientProspectReadPlatformServiceImp implements
		ClientProspectReadPlatformService {

	
	private final JdbcTemplate jdbcTemplate;
	private final PriceReadPlatformService priceReadPlatformService;
	private final PlatformSecurityContext context;
	
	@Autowired
	public ClientProspectReadPlatformServiceImp(final TenantAwareRoutingDataSource dataSource, final PriceReadPlatformService priceReadPlatformService, final PlatformSecurityContext context) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.priceReadPlatformService = priceReadPlatformService;
		this.context = context;
	}
	
	
	@Override
	public ClientProspectData retriveClientProspectTemplate() {
		context.authenticatedUser();
		ClientProspectMapper rowMapper = new ClientProspectMapper();
		String sql = "select "+rowMapper.query()+" from b_prospect cp limit 1";
		return jdbcTemplate.queryForObject(sql, rowMapper);
	}
	
	public Collection<ClientProspectData> retriveClientProspect() {
		context.authenticatedUser();
		ClientProspectMapper rowMapper = new ClientProspectMapper();
		String sql = "select "+rowMapper.query()+" from b_prospect p";
		return jdbcTemplate.query(sql, rowMapper);
	}

	
	@Override
	public ProspectDetailData retriveClientProspect(Long clientProspectId) {	
		return new ProspectDetailData();
	}
	
	
	@Override
	public List<ProspectDetailAssignedToData> retrieveUsers() {
		context.authenticatedUser();

		UserMapper mapper = new UserMapper();

		String sql = "select " + mapper.schema();

		return this.jdbcTemplate.query(sql, mapper, new Object[] {});
	}

	@Override
	public List<ProspectDetailData> retriveProspectDetailHistory(Long prospectdetailid) {
		context.authenticatedUser();
		HistoryMapper mapper = new HistoryMapper();
		String sql = "select "+mapper.query()+" where d.prospect_id=? order by d.id desc";
		return jdbcTemplate.query(sql, mapper,new Object[]{prospectdetailid});
	}
	
	private static final class HistoryMapper implements RowMapper<ProspectDetailData>{
		@Override
		public ProspectDetailData mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			Long id = rs.getLong("id");
			Long prospectId = rs.getLong("prospectId");
			String callStatus = rs.getString("callStatus");
			Date nextTime = rs.getDate("nextTime");
			String notes = rs.getString("notes");
			String assignedTo = rs.getString("assignedTo");
			System.out.println("Date : "+nextTime);
			System.out.println("LocalDate : "+JdbcSupport.getLocalDate(rs, "nextTime"));
			return new ProspectDetailData(id, prospectId, callStatus, nextTime, notes, assignedTo);
		}
		
		public String query(){
			String sql = "d.id as id, d.prospect_id as prospectId, d.next_time as nextTime, d.notes as notes, cv.code_value as callStatus, au.username as assignedTo from b_prospect_detail d left outer join m_code_value cv on d.call_status=cv.id left outer join m_appuser au on au.id=d.assigned_to";
			return sql;
		}
	} 
	
	
	private static final class UserMapper implements
			RowMapper<ProspectDetailAssignedToData> {

		public String schema() {
			return "u.id as id,u.username as assignedTo from m_appuser u where u.is_deleted=0";

		}

		@Override
		public ProspectDetailAssignedToData mapRow(ResultSet rs, int rowNum)
				throws SQLException {

			Long id = rs.getLong("id");
			String username = rs.getString("assignedTo");
			return new ProspectDetailAssignedToData(id, username);

		}

	}


	
	
	
	
	public class ClientProspectMapper implements RowMapper<ClientProspectData>{
		
		@Override
		public ClientProspectData mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			Long id = rs.getLong("id");
			Short prospectType = rs.getShort("prospectType");
			String firstName = rs.getString("firstName");
			String middleName = rs.getString("middleName");
			String lastName = rs.getString("lastName");
			String homePhoneNumber = rs.getString("homePhoneNumber");
			String workPhoneNumber = rs.getString("workPhoneNumber");
			String mobileNumber = rs.getString("mobileNumber");
			String email = rs.getString("email");
			String sourceOfPublicity = rs.getString("sourceOfPublicity");
			String preferredPlan = rs.getString("preferredPlan");
			Date preferredCallingTime = rs.getDate("preferredCallingTime");
			String note = rs.getString("note");
			String address = rs.getString("address");
			String streetArea = rs.getString("streetArea");
			String cityDistrict = rs.getString("cityDistrict");
			String state = rs.getString("state");
			String country = rs.getString("country");
			String status = rs.getString("status");
			String statusRemark = rs.getString("statusRemark");
			String isDeleted = rs.getString("isDeleted");
			return new ClientProspectData(id,prospectType, firstName, middleName, lastName, homePhoneNumber, workPhoneNumber, mobileNumber, email, sourceOfPublicity, preferredCallingTime, note, address, streetArea, cityDistrict, state, country, preferredPlan, status, statusRemark,isDeleted);
		}
		
		public String query(){
			String sql = "p.id as id, p.prospect_type as prospectType, p.first_name as firstName, p.middle_name as middleName, p.last_name as lastName, p.home_phone_number as homePhoneNumber, p.work_phone_number as workPhoneNumber, p.mobile_number as mobileNumber, p.email as email, p.source_of_publicity as sourceOfPublicity, p.preferred_plan as preferredPlan, p.preferred_calling_time as preferredCallingTime, p.address as address, p.street_area as streetArea, p.city_district as cityDistrict, p.state as state, p.country as country, p.status as status, p.status_remark as statusRemark, p.is_deleted as isDeleted, (select notes FROM b_prospect_detail pd where pd.prospect_id =p.id and pd.id=(select max(id) from b_prospect_detail where b_prospect_detail.prospect_id = p.id)) as note";
			return sql;
		}
	}
	
	@Override
	public Collection<ProspectPlanCodeData> retrivePlans() {
		  context.authenticatedUser();

	        String sql = "select s.id as id,s.plan_description as planDescription from b_plan_master s where s.plan_status=1 and  s.is_deleted='n'  ";

	        RowMapper<ProspectPlanCodeData> rm = new PeriodMapper();

	        return this.jdbcTemplate.query(sql, rm, new Object[] {});
	}

	 private static final class PeriodMapper implements RowMapper<ProspectPlanCodeData> {

	        @Override
	        public ProspectPlanCodeData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {

	        	Long id = rs.getLong("id");
	            String planCode = rs.getString("planDescription");
	            return new ProspectPlanCodeData(id,planCode);

	        }


	 }
}













