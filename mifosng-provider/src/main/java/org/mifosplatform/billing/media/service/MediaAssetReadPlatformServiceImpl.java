package org.mifosplatform.billing.media.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.joda.time.LocalDate;
import org.mifosplatform.billing.media.data.MediaAssetData;
import org.mifosplatform.billing.media.data.MediaassetAttribute;
import org.mifosplatform.billing.media.data.MediaassetAttributeData;
import org.mifosplatform.billing.media.domain.MediaEnum;
import org.mifosplatform.billing.media.domain.MediaTypeEnumaration;
import org.mifosplatform.billing.mediadetails.data.MediaLocationData;
import org.mifosplatform.billing.paymode.data.McodeData;
import org.mifosplatform.infrastructure.core.data.MediaEnumoptionData;
import org.mifosplatform.infrastructure.core.domain.JdbcSupport;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class MediaAssetReadPlatformServiceImpl implements MediaAssetReadPlatformService {
	
	private final PlatformSecurityContext context;
	private final JdbcTemplate jdbcTemplate;
	
	
	@Autowired
	public MediaAssetReadPlatformServiceImpl(final PlatformSecurityContext context,
			final TenantAwareRoutingDataSource dataSource) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);

	}


	@Override
	public List<MediaAssetData> retrievemediaAssetdata(Long pageNo) {
		
		
		
		
		MediaAssestDataMapper mediaAssestDataMapper = new MediaAssestDataMapper();
		String sql = "select " + mediaAssestDataMapper.mediaAssestDataSchema()+" GROUP BY m.id  having  count( ed.media_id) = 1  LIMIT ?, 10" ;
		return this.jdbcTemplate.query(sql, mediaAssestDataMapper,new Object[] {"", pageNo });

	}

	private static final class MediaAssestDataMapper implements
			RowMapper<MediaAssetData> {

		@Override
		public MediaAssetData mapRow(ResultSet rs, int rowNum)

				throws SQLException {
			Long mediaId = rs.getLong("mediaId");
			String mediaTitle = rs.getString("title");
			String mediaImage = rs.getString("image");
			BigDecimal rating = rs.getBigDecimal("rating");
			Long eventId=rs.getLong("eventId");
			String assetTag=rs.getString("assetTag");

			return new MediaAssetData(mediaId,mediaTitle,mediaImage,rating,eventId,assetTag);
		}
		public String mediaAssestDataSchemaforCommingMovice() {

			return "m.id AS mediaId, m.title AS title,m.image AS image,m.rating AS rating,0 as eventId,'C' as assetTag  FROM b_media_asset m ";


		}
		
		public String mediaAssestDataSchema() {

			return " m.id AS mediaId,m.title AS title, m.image AS image, m.rating AS rating,? as assetTag,ed.event_id as eventId, count(ed.media_id)"
				  +" FROM b_media_asset m inner join b_event_detail ed on ed.media_id = m.id  inner join b_event_master em on em.id = ed.event_id";
		}
		public String mediaAssestDataSchemaforWatchedMovies() {

			return "  m.id AS mediaId,m.title AS title,m.image AS image, m.rating AS rating,'W' as assetTag,m.release_date,ed.event_Id as eventId, COUNT(eo.id) FROM b_media_asset m"
				+" inner join b_event_detail ed on m.id=ed.media_id  inner JOIN b_eventorder eo    ON (eo.event_id = ed.event_id)  ORDER BY 6 DESC LIMIT ?, 10";
		}
		
		public String mediaAssestDataSchemaforPromotionalMovies() {

			return " ed.event_id,ma.id AS mediaId,ma.title AS title,ma.image AS image, ed.event_Id as eventId,ma.rating AS rating,? as assetTag FROM b_media_asset ma inner join b_event_detail ed"
				+"  on ed.media_id = ma.id  Where ed.event_id in (Select event_id  from b_event_master em  inner join b_event_detail ed on em.id=ed.event_id"
				+"  group by ed.event_id  having count(ed.event_id)>1) LIMIT ?, 10";
		}
	}

		@Override
		public List<MediaAssetData> retrievemediaAssetdatabyNewRealease(Long pageNo) {
			MediaAssestDataMapper mediaAssestDataMapper = new MediaAssestDataMapper();
			String sql = "select " + mediaAssestDataMapper.mediaAssestDataSchema()+"  where m.release_date <= adddate(now(),INTERVAL -3 MONTH)"
					+"  group by m.id  having count(distinct ed.event_id) >=1 LIMIT ?, 10" ;
			return this.jdbcTemplate.query(sql, mediaAssestDataMapper,new Object[] { "N",pageNo });
     }
		 

		@Override
		public List<MediaAssetData> retrievemediaAssetdatabyRating(Long pageNo) {
			MediaAssestDataMapper mediaAssestDataMapper = new MediaAssestDataMapper();
			String sql = "select " + mediaAssestDataMapper.mediaAssestDataSchema()+"    where m.rating >=4.5 group by m.id  having count(distinct ed.event_id) >=1 "
					+" LIMIT ?, 10" ;
			return this.jdbcTemplate.query(sql, mediaAssestDataMapper,new Object[] { "R",pageNo });
		}


		@Override
		public List<MediaAssetData> retrieveAllmediaAssetdata() {
			MediaAssestDataMapper mediaAssestDataMapper = new MediaAssestDataMapper();
			String sql = "select " + mediaAssestDataMapper.mediaAssestDataSchema();
			return this.jdbcTemplate.query(sql, mediaAssestDataMapper,new Object[] { "As" });
		}

		@Override
		public Long retrieveNoofPages( String query) {
			NOOfPages pages = new NOOfPages();
			String sql = "select " + pages.mediaAssestDataSchemaForPages(query) ;
			return this.jdbcTemplate.queryForObject(sql, pages,new Object[] { });
     }
		private static final class NOOfPages implements	RowMapper<Long> {

	public Long mapRow(ResultSet rs, int rowNum) throws SQLException {
		Long noOfpages = rs.getLong("no_ofpages");

		return noOfpages;
	}
	public String mediaAssestDataSchemaForPages(String query) {
		return " ceil(count(0)/10) as no_ofpages from ("+query+")a";
	}
		}

		
		@Override
		public MediaAssetData retrievemediaAsset(Long id) {
			try{
			AssestDataMapper mapper = new AssestDataMapper();
			String sql = "Select " + mapper.scheme() + "  m.id=?";
			return this.jdbcTemplate.queryForObject(sql, mapper,new Object[]{ id });
			}catch(EmptyResultDataAccessException accessException){
				return null;
			}
		} 
		
		@Override
		public List<MediaAssetData> retrievemediaAssetdatabyDiscountedMovies(Long pageNo) {
			MediaAssestDataMapper mediaAssestDataMapper = new MediaAssestDataMapper();
			String sql = "select " + mediaAssestDataMapper.mediaAssestDataSchema()+" inner join  b_event_pricing ep on em.id=ep.event_id"
					+" where discount_id>=1  group by m.id  having count(distinct ed.event_id) >=1 LIMIT ?, 10 ";
			return this.jdbcTemplate.query(sql, mediaAssestDataMapper,new Object[] { "D",pageNo });
}


		@Override
		public List<MediaAssetData> retrievemediaAssetdatabyPromotionalMovies(Long pageNo) {
			MediaAssestDataMapper mediaAssestDataMapper = new MediaAssestDataMapper();
			String sql = "select " + mediaAssestDataMapper.mediaAssestDataSchemaforPromotionalMovies();
			return this.jdbcTemplate.query(sql, mediaAssestDataMapper,new Object[] {"P", pageNo });
}


		@Override
		public List<MediaAssetData> retrievemediaAssetdatabyComingSoonMovies(Long pageNo) {
			MediaAssestDataMapper mediaAssestDataMapper = new MediaAssestDataMapper();
			String sql = "select " + mediaAssestDataMapper.mediaAssestDataSchemaforCommingMovice()+"   where category_id=19 LIMIT ?, 10;";
			return this.jdbcTemplate.query(sql, mediaAssestDataMapper,new Object[] { pageNo });
		}


		@Override
		public List<MediaAssetData> retrievemediaAssetdatabyMostWatchedMovies(Long pageNo) {
			MediaAssestDataMapper mediaAssestDataMapper = new MediaAssestDataMapper();
			String sql = "select " + mediaAssestDataMapper.mediaAssestDataSchemaforWatchedMovies();
			return this.jdbcTemplate.query(sql, mediaAssestDataMapper,new Object[] { pageNo });
		}

		@Override
		public List<MediaAssetData> retrievemediaAssetdatabySearching(Long pageNo, String filterType) {
			MediaAssestDataMapper mediaAssestDataMapper = new MediaAssestDataMapper();
			String sql = "select " + mediaAssestDataMapper.mediaAssestDataSchema()+"  where upper(m.title) like upper('%"+filterType+"%')  GROUP BY m.id  having  count( ed.media_id) = 1  LIMIT ?, 10" ;
			return this.jdbcTemplate.query(sql, mediaAssestDataMapper,new Object[] {"A", pageNo });

		}


		@Override
		public List<MediaAssetData> retrieveAllAssetdata() {
			MediaDataMapper mediaAssestDataMapper = new MediaDataMapper();
			String sql = "select " + mediaAssestDataMapper.mediaAssestDataSchema() ;
			return this.jdbcTemplate.query(sql, mediaAssestDataMapper,new Object[] {});

		}


		@Override
		public List<MediaAssetData> retrieveAllMediaTemplatedata() {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		public List<MediaassetAttribute> retrieveMediaAttributes() {
			MediaAttributeMapper mapper=new MediaAttributeMapper();
			String sql="select "+mapper.scheme() +" m.code_name='MediaAttribute'";
			return this.jdbcTemplate.query(sql, mapper,new Object[] {});
		}
		
		
		private static final class MediaAttributeMapper implements
		RowMapper<MediaassetAttribute> {

	@Override
	public MediaassetAttribute mapRow(ResultSet rs, int rowNum)

			throws SQLException {
		Long mediaId = rs.getLong("id");
		String mediaName = rs.getString("codeValue");
		
		return new MediaassetAttribute(mediaId,mediaName);
	}
	public String scheme() {

		return " mc.id as id,mc.code_value as codeValue FROM m_code_value mc,m_code m where mc.code_id=m.id and ";


	}

		}
		
		
		private static final class MediaDataMapper implements
		RowMapper<MediaAssetData> {

	@Override
	public MediaAssetData mapRow(ResultSet rs, int rowNum)

			throws SQLException {
		Long mediaId = rs.getLong("id");
		String mediaTitle = rs.getString("mediaTitle");
		String status=rs.getString("status");
		LocalDate releaseDate=JdbcSupport.getLocalDate(rs,"releaseDate");
		BigDecimal rating=rs.getBigDecimal("rating");
		
		return new MediaAssetData(mediaId,mediaTitle,status,releaseDate,rating);
	}
	public String mediaAssestDataSchema() {
		return " m.id AS id,  m.title AS mediaTitle,m.status AS status,m.release_date as releaseDate,m.rating as rating"
				+" FROM b_media_asset m where m.is_deleted='N'";
	}
	

		}

		@Override
		public List<MediaassetAttribute> retrieveMediaFormatType() {
			MediaAttributeMapper mapper=new MediaAttributeMapper();
			String sql="select "+mapper.scheme() +" m.code_name='MediaFormat'";
			return this.jdbcTemplate.query(sql, mapper,new Object[] {});
		}
		private static final class AssestDataMapper implements
		RowMapper<MediaAssetData> {

	@Override
	public MediaAssetData mapRow(ResultSet rs, int rowNum)

			throws SQLException {
		final Long mediaId = rs.getLong("id");
		final String mediatitle = rs.getString("title");
		final String type = rs.getString("type");
		final String genre = rs.getString("genre");
		final Long catageoryId = rs.getLong("catageoryId");
		final LocalDate releaseDate = JdbcSupport.getLocalDate(rs,"releaseDate");
		final String subject = rs.getString("subject");
		final String overview = rs.getString("overview");
		final String image = rs.getString("image");
		final String contentProvider = rs.getString("contentProvider");
		final String rated=rs.getString("rated");
		final BigDecimal rating=rs.getBigDecimal("rating");
		final Long ratingCount=rs.getLong("ratingCount");
		final String status=rs.getString("status");
		final String duration=rs.getString("duration");
		
		return new MediaAssetData(mediaId,mediatitle,type,genre,catageoryId,releaseDate,subject,overview,image,contentProvider,
				rated,rating,ratingCount,status,duration);
	}
	public String scheme() {

		return " m.id as id,m.title as title,m.type as type,m.category_id as catageoryId,m.genre as genre,m.release_date as releaseDate,"
			  +"m.overview as overview,m.subject as subject,m.image as image,m.content_provider as contentProvider,m.rated as rated, "
			 +"m.rating as rating,m.rating_count as ratingCount,m.status as status,m.duration as duration FROM b_media_asset m where m.is_deleted='N' and ";


	}


		}

		@Override
		public List<MediaEnumoptionData> retrieveMediaTypeData() {
			MediaEnumoptionData movies = MediaTypeEnumaration.enumOptionData(MediaEnum.MOVIES);
            MediaEnumoptionData tvSerails=MediaTypeEnumaration.enumOptionData(MediaEnum.TV_SERIALS);
            MediaEnumoptionData comingSoon=MediaTypeEnumaration.enumOptionData(MediaEnum.COMING_SOON);
            
			List<MediaEnumoptionData> categotyType = Arrays.asList(movies,tvSerails,comingSoon);
				return categotyType;
		}

		@Override
		public List<McodeData> retrieveMedaiCategory() {
			context.authenticatedUser();

			SystemDataMapper mapper = new SystemDataMapper();

			String sql = "select " + mapper.schema();

			return this.jdbcTemplate.query(sql, mapper, new Object[] { "Asset Category" });
		}

		private static final class SystemDataMapper implements RowMapper<McodeData> {

			public String schema() {


				return " mc.id as id,mc.code_value as codeValue from m_code m,m_code_value mc where m.id = mc.code_id and m.code_name=? ";

			}

			@Override
			public McodeData mapRow(ResultSet rs, int rowNum) throws SQLException {
				
				Long id=rs.getLong("id");
				String codeValue = rs.getString("codeValue");
				return new McodeData(id,codeValue);
				
			}

		}

		@Override
		public List<McodeData> retrieveLanguageCategeories() {
			context.authenticatedUser();

			SystemDataMapper mapper = new SystemDataMapper();

			String sql = "select " + mapper.schema();

			return this.jdbcTemplate.query(sql, mapper, new Object[] { "Asset language" });
		}


		@Override
		public List<MediaassetAttributeData> retrieveMediaassetAttributesData(
				Long mediaId) {
			try{
				MediaassetAttributesDataMapper mapper=new MediaassetAttributesDataMapper();			
			    String sql="select "+mapper.scheme() +mediaId;
			    return this.jdbcTemplate.query(sql, mapper,new Object[] {});
		     
			    }catch (final EmptyResultDataAccessException e) {
					return null;
				}
		}
private static final class MediaassetAttributesDataMapper implements RowMapper<MediaassetAttributeData> {

			
			public String scheme() {
				return " ma.id as id,ma.attribute_type as attributeType,ma.attribute_name as attributeName,ma.attribute_value as attributeValue," +
						"ma.attribute_nickname as attributeNickname,ma.attribute_image as attributeImage from b_mediaasset_attributes ma " +
						"where ma.media_id= ";
			}
			
			@Override
			public MediaassetAttributeData mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				// TODO Auto-generated method stub
				Long id=rs.getLong("id");
				String attributeType=rs.getString("attributeType");
				String attributeName=rs.getString("attributeName");
				String attributeValue=rs.getString("attributeValue");
				String attributeNickname=rs.getString("attributeNickname");
				String attributeImage=rs.getString("attributeImage");
				
				return new MediaassetAttributeData(attributeType,attributeName,attributeValue,attributeNickname,attributeImage,id);
			}
			
			
		}

	

		@Override
		public List<MediaLocationData> retrievemediaAssetLocationdata(
				Long mediaId) {
			try{
				MediaLocationDataMapper mapper=new MediaLocationDataMapper();			
		    	String sql="select "+mapper.scheme() +mediaId;
		    	return this.jdbcTemplate.query(sql, mapper,new Object[] {});
			
			}catch (final EmptyResultDataAccessException e) {
			    return null;
			}
		
		}
		
private static final class MediaLocationDataMapper implements RowMapper<MediaLocationData> {

			
			public String scheme() {
				return " ml.id as id,ml.language_id as languageId,ml.format_type as formatType,ml.location as location from " +
						"b_mediaasset_location ml where ml.media_id= ";
			}
			
			@Override
			public MediaLocationData mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				// TODO Auto-generated method stub
				Long id=rs.getLong("id");
				Long languageId=rs.getLong("languageId");
				String formatType=rs.getString("formatType");
				String location=rs.getString("location");
				
				return new MediaLocationData(languageId,formatType,location);
			}
			
			
		}

}
