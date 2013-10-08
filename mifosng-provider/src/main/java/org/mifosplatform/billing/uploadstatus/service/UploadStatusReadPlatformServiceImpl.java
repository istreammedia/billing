package org.mifosplatform.billing.uploadstatus.service;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.uploadstatus.data.UploadStatusData;
import org.mifosplatform.infrastructure.codes.exception.CodeNotFoundException;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class UploadStatusReadPlatformServiceImpl implements UploadStatusReadPlatformService {

    private final JdbcTemplate jdbcTemplate;
    private final PlatformSecurityContext context;

    @Autowired
    public UploadStatusReadPlatformServiceImpl(final PlatformSecurityContext context, final TenantAwareRoutingDataSource dataSource) {
        this.context = context;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    private static final class UploadStatusMapper implements RowMapper<UploadStatusData> {

        public String schema() {
            return " u.id as Id ,u.upload_process as uploadProcess,u.upload_filepath as uploadFilePath,u.total_records as totalRecords,u.process_date as processDate," +
            		"u.process_status as processStatus,u.process_records as processRecords,u.unprocess_records as unprocessRecords,u.error_message as errorMessage " +
            		"from uploads_status u ";
        }

        @Override
        public UploadStatusData mapRow(final ResultSet rs, @SuppressWarnings("unused") final int rowNum) throws SQLException {
        	
         final Long id = rs.getLong("Id");
        final String uploadProcess=rs.getString("uploadProcess");
       	 final String uploadFilePath=rs.getString("uploadFilePath");
       	 System.out.println(uploadFilePath );
       	 File  uploadFile=new File(uploadFilePath);
       	 String fileName=uploadFile.getName();
       	 String filePath=uploadFilePath.substring(0,11).concat("......").concat(File.separator+fileName);
       	 System.out.println(filePath);
      
         final LocalDate processDate= JdbcSupport.getLocalDate(rs,"processDate");
       	 final String processStatus=rs.getString("processStatus");
       	 final Long processRecords=rs.getLong("processRecords");
       	 final Long totalRecords=rs.getLong("totalRecords");
       	 final String errorMessage=rs.getString("errorMessage");
       	 final Long unprocessRecords=rs.getLong("unprocessRecords");
       	

     return new UploadStatusData(id,uploadProcess,filePath,processDate,processStatus,processRecords,unprocessRecords,errorMessage,totalRecords);
       	//return UploadStatusData.
        }
    }

    @Override
    public Collection<UploadStatusData> retrieveAllCodes() {
        context.authenticatedUser();

        final UploadStatusMapper rm = new UploadStatusMapper();
        final String sql = "select " + rm.schema() + " order by u.id";

        return this.jdbcTemplate.query(sql, rm, new Object[] {});
    }
    
    @Override
    public List<UploadStatusData> retrieveAllUploadStatusData() {
        context.authenticatedUser();

        final UploadStatusMapper rm = new UploadStatusMapper();
        final String sql = "select " + rm.schema() + " order by u.id";

        return this.jdbcTemplate.query(sql, rm, new Object[] {});
    }


    @Override
    public UploadStatusData retrieveCode(final Long codeId) {
        try {
            context.authenticatedUser();

            final UploadStatusMapper rm = new UploadStatusMapper();
            final String sql = "select " + rm.schema() + " where u.id = ?";

            return this.jdbcTemplate.queryForObject(sql, rm, new Object[] { codeId });
        } catch (EmptyResultDataAccessException e) {
            throw new CodeNotFoundException(codeId);
        }
    }
}