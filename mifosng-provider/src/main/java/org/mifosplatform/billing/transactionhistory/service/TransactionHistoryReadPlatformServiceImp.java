package org.mifosplatform.billing.transactionhistory.service;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.mifosplatform.billing.transactionhistory.data.TransactionHistoryData;
import org.mifosplatform.billing.transactionhistory.domain.TransactionHistoryRepository;
import org.mifosplatform.infrastructure.core.exception.PlatformDataIntegrityException;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;


@Service
public class TransactionHistoryReadPlatformServiceImp implements TransactionHistoryReadPlatformService{

	
	
	private JdbcTemplate jdbcTemplate;
	private PlatformSecurityContext context;
	
	@Autowired
	public TransactionHistoryReadPlatformServiceImp(final TenantAwareRoutingDataSource dataSource, final TransactionHistoryRepository transactionHistoryRepository,final PlatformSecurityContext context) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.context = context;
	}
	
	@Override
	public List<TransactionHistoryData> retriveTransactionHistoryTemplate() {
		   
		 return retriveAll();
		
	}
	
	@Override
	public List<TransactionHistoryData> retriveTransactionHistoryById(final Long clientId) {
		
		return retriveById(clientId);
	}
	
	
	private String query(){
		
		return " th.id AS id,th.client_id AS clientId,th.transaction_type AS transactionType,th.transaction_date AS transactionDate,th.history AS history," +
				" a.username as userName  FROM b_transaction_history th,m_appuser a WHERE a.id = th.createdby_id ";
	}
	
	private List<TransactionHistoryData> retriveById(Long id){
		try{
			context.authenticatedUser();
			String sql = "select "+query()+" and  th.client_id = ?  ";
			TransactionHistoryMapper rowMapper = new TransactionHistoryMapper();
			return jdbcTemplate.query(sql, rowMapper,new Object[]{id});
		}catch(DataIntegrityViolationException dve){
			throw new PlatformDataIntegrityException("", "", "");
		}
		
	}
	
	public List<TransactionHistoryData> retriveAll(){
		try{
		context.authenticatedUser();
		String sql = "select "+query();
		TransactionHistoryMapper rowMapper = new TransactionHistoryMapper();
		return jdbcTemplate.query(sql, rowMapper,new Object[]{});
		}catch(DataIntegrityViolationException dve){
			 throw new PlatformDataIntegrityException("transactionhistoryEXCEPTION-1", "transactionhistoryEXCEPTION-2", "transactionhistoryEXCEPTION-3");
		}
	}
	
	
	private class TransactionHistoryMapper implements RowMapper<TransactionHistoryData>{
		
		@Override
		public TransactionHistoryData mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long id = rs.getLong("id");
			Long clientId = rs.getLong("clientId");
			String transactionType = rs.getString("transactionType");
			Date transactionDate = rs.getDate("transactionDate");
			String history = rs.getString("history");
			String user=rs.getString("userName");
			return new TransactionHistoryData(id,clientId, transactionType, transactionDate, history,user);
		}

	}
}
