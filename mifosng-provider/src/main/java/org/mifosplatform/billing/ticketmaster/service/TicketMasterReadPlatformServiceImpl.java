package org.mifosplatform.billing.ticketmaster.service;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.ticketmaster.data.ClientTicketData;
import org.mifosplatform.billing.ticketmaster.data.ProblemsData;
import org.mifosplatform.billing.ticketmaster.data.TicketMasterData;
import org.mifosplatform.billing.ticketmaster.data.UsersData;
import org.mifosplatform.billing.ticketmaster.domain.PriorityType;
import org.mifosplatform.billing.ticketmaster.domain.PriorityTypeEnum;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class TicketMasterReadPlatformServiceImpl  implements TicketMasterReadPlatformService{
	
	private final JdbcTemplate jdbcTemplate;
	private final PlatformSecurityContext context;

	@Autowired
	public TicketMasterReadPlatformServiceImpl(final PlatformSecurityContext context,
			final TenantAwareRoutingDataSource dataSource) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public List<TicketMasterData> retrieveTicketStatusData() {
		context.authenticatedUser();

		statusMapper mapper = new statusMapper();

		String sql = "select" + mapper.schema()+" where mc.code_name='Ticket Status' and order_position = 1";

		return this.jdbcTemplate.query(sql, mapper, new Object[] {});
	}

	private static final class statusMapper implements
			RowMapper<TicketMasterData> {

		public String schema() {
			return " mcv.id as statusCode, mcv.code_value as statusDesc from m_code_value mcv " +
					"inner join m_code mc on mc.id = mcv.code_id";

		}

		@Override
		public TicketMasterData mapRow(ResultSet rs, int rowNum)
				throws SQLException {

			Integer statusCode = rs.getInt("statusCode");
			String statusDesc = rs.getString("statusDesc");
	
			TicketMasterData data = new TicketMasterData(statusCode,statusDesc);
			return data;

		}

	}


	@Override
	public List<ProblemsData> retrieveProblemData() {

		context.authenticatedUser();

		DataMapper mapper = new DataMapper();

		String sql = "select " + mapper.schema();

		return this.jdbcTemplate.query(sql, mapper, new Object[] {});
	}

	private static final class DataMapper implements
			RowMapper<ProblemsData> {

		public String schema() {
			return  "mcv.id  as problemCode,mcv.code_value as problemDescription from m_code_value mcv"
					  +" inner join m_code mc on mc.id = mcv.code_id"
				   +" where mc.code_name='Problem Code' order by mcv.order_position";

		}

		@Override
		public ProblemsData mapRow(ResultSet rs, int rowNum)
				throws SQLException {

			Long  problemCode = rs.getLong("problemCode");
			String problemDescription = rs.getString("problemDescription");

			ProblemsData data = new ProblemsData(problemCode,problemDescription,null,null);

			return data;

		}

	}

	@Override
	public List<UsersData> retrieveUsers() {
		context.authenticatedUser();

		UserMapper mapper = new UserMapper();

		String sql = "select " + mapper.schema();

		return this.jdbcTemplate.query(sql, mapper, new Object[] {});
	}

	private static final class UserMapper implements
			RowMapper<UsersData> {

		public String schema() {
			return "u.id as id,u.username as username from m_appuser u where u.is_deleted=0";

		}

		@Override
		public UsersData mapRow(ResultSet rs, int rowNum)
				throws SQLException {

			Long id = rs.getLong("id");
			String username = rs.getString("username");

			UsersData data = new UsersData(id, username);

			return data;

		}

	}

	@Override
	public List<TicketMasterData> retrieveClientTicketDetails(Long clientId) {
		try {
			final ClientTicketMapper mapper = new ClientTicketMapper();

			final String sql = "select " + mapper.clientOrderLookupSchema()+" and tckt.client_id= ? ";

			return jdbcTemplate.query(sql, mapper, new Object[] { clientId});
			} catch (EmptyResultDataAccessException e) {
			return null;
			}

			}

			private static final class ClientTicketMapper implements RowMapper<TicketMasterData> {

			public String clientOrderLookupSchema() {
			return "tckt.id as id, tckt.priority as priority, tckt.ticket_date as ticketDate, tckt.assigned_to as userId,"
			        + " (select code_value from m_code_value mcv where tckt.problem_code=mcv.id)as problemDescription," 
					+ " tckt.status as status, "
			        + " (select m_appuser.username from m_appuser "
                    +		" inner join b_ticket_details td on td.assigned_to = m_appuser.id"
                    + " where td.id = (select max(id) from b_ticket_details where b_ticket_details.ticket_id = tckt.id)) as assignedTo,"
			        + " (select comments FROM b_ticket_details details where details.ticket_id =tckt.id and "
			        + " details.id=(select max(id) from b_ticket_details where b_ticket_details.ticket_id = tckt.id)) as lastComment"
			        + " from b_ticket_master tckt, m_appuser user where tckt.assigned_to = user.id"; 
			}

			@Override
			public TicketMasterData mapRow(final ResultSet rs, final int rowNum) throws SQLException {

			final Long id = rs.getLong("id");
			final String priority = rs.getString("priority");
			final String status = rs.getString("status");
			final String LastComment = rs.getString("LastComment");
			final String problemDescription = rs.getString("problemDescription");
			final String assignedTo = rs.getString("assignedTo");
			final String usersId = rs.getString("userId");
			LocalDate ticketDate=JdbcSupport.getLocalDate(rs,"ticketDate");
			int userId=new Integer(usersId);
			return new TicketMasterData(id, priority, status, userId, ticketDate,LastComment,problemDescription,assignedTo);
			}
			}

			@Override
			public TicketMasterData retrieveSingleTicketDetails(Long clientId, Long ticketId) {
				try {
					final ClientTicketMapper mapper = new ClientTicketMapper();

					final String sql = "select " + mapper.clientOrderLookupSchema()+" and tckt.client_id= ? and tckt.id=?";

					return jdbcTemplate.queryForObject(sql, mapper, new Object[] {clientId,ticketId});
					} catch (EmptyResultDataAccessException e) {
					return null;
					}

					}

			@Override
			public List<EnumOptionData> retrievePriorityData() {
				EnumOptionData low=PriorityTypeEnum.priorityType(PriorityType.LOW);
				EnumOptionData medium=PriorityTypeEnum.priorityType(PriorityType.MEDIUM);
				EnumOptionData high=PriorityTypeEnum.priorityType(PriorityType.HIGH);
				List<EnumOptionData> priorityType=Arrays.asList(low,medium,high);
				return priorityType;
			}

			@Override
			public List<TicketMasterData> retrieveTicketCloseStatusData() {
				statusMapper mapper = new statusMapper();
				String sql = "select " + mapper.schema()+" where mc.code_name='Ticket Status' and order_position = 2 order by mcv.id ";
				return this.jdbcTemplate.query(sql, mapper, new Object[] {});
			}

			@Override
			public List<TicketMasterData> retrieveClientTicketHistory(
					Long ticketId) {
				context.authenticatedUser();

				TicketDataMapper mapper = new TicketDataMapper();

				String sql = "select " + mapper.schema()+" where t.ticket_id=tm.id and t.ticket_id=?";

				return this.jdbcTemplate.query(sql, mapper, new Object[] { ticketId});
			}

			private static final class TicketDataMapper implements
					RowMapper<TicketMasterData> {

				public String schema() {
					return " t.id AS id,t.created_date AS createDate,user.username AS assignedTo,t.comments as description," +
							" t.attachments AS attachments  FROM b_ticket_master tm , b_ticket_details t  "
						   +" inner join m_appuser user on user.id = t.assigned_to ";

				}

				@Override
				public TicketMasterData mapRow(ResultSet rs, int rowNum)
						throws SQLException {

					Long id = rs.getLong("id");
					LocalDate createdDate=JdbcSupport.getLocalDate(rs,"createDate");
					String assignedTo = rs.getString("assignedTo");
					String description = rs.getString("description");
					String attachments = rs.getString("attachments");
					String fileName=null;
					if(attachments!=null){
					File file=new File(attachments);
					 fileName=file.getName();
					}
					TicketMasterData data = new TicketMasterData(id,createdDate, assignedTo,description,fileName);

					return data;

				}

			}


			@Override
			public List<ClientTicketData> retrieveAssignedTickets() {
				AppUser user = this.context.authenticatedUser();
				
				final UserTicketsMapper mapper = new UserTicketsMapper();

				final String sql = "select " + mapper.userTicketSchema();

				return jdbcTemplate.query(sql, mapper, new Object[] { });
				
			}
			
			private static final class UserTicketsMapper implements RowMapper<ClientTicketData> {
				
				public String userTicketSchema() {
					return "tckt.client_id as clientId,(select display_name from m_client where id = tckt.client_id) as clientName, tckt.priority as priority, tckt.status as status, tckt.ticket_date as ticketDate, tckt.assigned_to as userId,"+
							""+"(Select comments from b_ticket_details x where tckt.id=x.ticket_id and x.id=(select max(id) from b_ticket_details y where tckt.id=y.ticket_id)) as LastComment,"+
							""+"(select mcv.code_value from m_code_value mcv where mcv.id = tckt.problem_code) as problemDescription,"+
							""+"(select user.username from m_appuser user where tckt.assigned_to = user.id) as assignedTo,"+
							""+"CONCAT(TIMESTAMPDIFF(day,tckt.ticket_date,Now()) , ' d ',MOD(TIMESTAMPDIFF(hour,tckt.ticket_date,Now()), 24), ' hr ',MOD( TIMESTAMPDIFF(minute,tckt.ticket_date,Now()), 60), ' min ')as timeElapsed,"+
							""+"tckt.client_id as clientId from b_ticket_master tckt";
					}

				@Override
				public ClientTicketData mapRow(ResultSet rs, int rowNum) throws SQLException {
					
					final String priority = rs.getString("priority");
					final String status = rs.getString("status");
					final Long userId = rs.getLong("userId");
					final LocalDate ticketDate=JdbcSupport.getLocalDate(rs,"ticketDate");
					final String lastComment = rs.getString("LastComment");
					final String problemDescription = rs.getString("problemDescription");
					final String assignedTo = rs.getString("assignedTo");
					final Long clientId = rs.getLong("clientId");
					final String timeElapsed = rs.getString("timeElapsed");
					final String clientName = rs.getString("clientName");
					
					return new ClientTicketData(0L, priority, status, userId, ticketDate, lastComment, problemDescription, assignedTo, clientId,timeElapsed,clientName);
				}
				
			}

	}


