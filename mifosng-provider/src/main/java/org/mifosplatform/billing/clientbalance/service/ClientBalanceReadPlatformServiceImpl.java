package org.mifosplatform.billing.clientbalance.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.mifosplatform.billing.clientbalance.data.ClientBalanceData;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class ClientBalanceReadPlatformServiceImpl implements ClientBalanceReadPlatformService {
	
	private final JdbcTemplate jdbcTemplate;
   
	@Autowired
	public ClientBalanceReadPlatformServiceImpl(final TenantAwareRoutingDataSource dataSource){
		 this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

    @Override
	public ClientBalanceData retrieveBalance(Long clientid){
            final BalanceMapper rm = new BalanceMapper();
            final String sql = rm.schema();
            List<ClientBalanceData> balanceDatas=this.jdbcTemplate.query(sql, rm, new Object[] { clientid });
            
            return balanceDatas.get(0);
        
    }
    private static final class BalanceMapper implements RowMapper<ClientBalanceData> {
        public String schema() {
            return  "(SELECT IFNULL(balance_amount, 0) balance_amount FROM b_client_balance WHERE client_id =?) UNION (SELECT 0 balance_amount)";
        }
		@Override
		public ClientBalanceData mapRow(ResultSet rs, int rowNum)
				throws SQLException {
			// TODO Auto-generated method stub
			BigDecimal balance=rs.getBigDecimal("balance_amount");
			if(balance==null){
				balance=BigDecimal.ZERO;
			}
			return new ClientBalanceData(balance);
		}
    }
	@Override
	public List<ClientBalanceData> retrieveAllClientBalances(Long clientId) {
		ClientBalanceMapper mapper = new ClientBalanceMapper();
		String sql = "select " + mapper.schema() + " where d.client_id=?";
		return this.jdbcTemplate.query(sql, mapper, new Object[] { clientId });
	}
	
	public static final class ClientBalanceMapper implements
	RowMapper<ClientBalanceData> {

  @Override
public ClientBalanceData mapRow(final ResultSet rs,
		@SuppressWarnings("unused") final int rowNum)
		throws SQLException {
	Long id = JdbcSupport.getLong(rs, "id");
	Long clientId = JdbcSupport.getLong(rs, "client_id");
	BigDecimal balanceAmount =rs.getBigDecimal("balance_amount");
	return new ClientBalanceData(id, clientId, balanceAmount);

}

public String schema() {
	return "d.id as id, d.client_id as client_id , d.balance_amount as balance_amount  from b_client_balance d";
}
}
}
    


