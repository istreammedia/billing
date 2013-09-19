package org.mifosplatform.billing.mediadetails.service;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import org.mifosplatform.billing.eventpricing.data.EventPricingData;
import org.mifosplatform.billing.mediadetails.data.MediaAssetDetailsData;
import org.mifosplatform.billing.mediadetails.data.MediaAssetLocationDetails;
import org.mifosplatform.billing.mediadetails.data.MediaLocationData;
import org.mifosplatform.billing.mediadetails.domain.MediaAssetRepository;
import org.mifosplatform.infrastructure.core.service.TenantAwareRoutingDataSource;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class MediaAssetDetailsReadPlatformServiceImpl implements MediaAssetDetailsReadPlatformService {
	
	private final PlatformSecurityContext context;
	private final JdbcTemplate jdbcTemplate;
	private final MediaAssetRepository mediaAssetRepository;
	
	
	@Autowired
	public MediaAssetDetailsReadPlatformServiceImpl(final PlatformSecurityContext context,
			final TenantAwareRoutingDataSource dataSource,final MediaAssetRepository mediaAssetRepository) {
		this.context = context;
		this.jdbcTemplate = new JdbcTemplate(dataSource);
		this.mediaAssetRepository=mediaAssetRepository;

	}


	@Override
	public MediaAssetDetailsData retrieveMediaAssetDetailsData(Long mediaId) {
		MediaAssestDataMapper mediaAssestDataMapper = new MediaAssestDataMapper();
		String sql = "select " + mediaAssestDataMapper.mediaAssestDataSchema()+" where a.id =?" ;
		return this.jdbcTemplate.queryForObject(sql, mediaAssestDataMapper,new Object[] { mediaId });
	/*	MediaAsset mediaAsset= this.mediaAssetRepository.findOne(mediaId);
		
		return new MediaAssetDetailsData(mediaAsset.getId(),mediaAsset.getTitle(),mediaAsset.getType(),mediaAsset.getMediaClass(),
				mediaAsset.getOverview(),mediaAsset.getSubject(),mediaAsset.getImage(),mediaAsset.getDuration(),mediaAsset.getContentProvider(),mediaAsset.getRated(),
				mediaAsset.getRating(),mediaAsset.getRatingCount(),mediaAsset.getStatus(),mediaAsset.getReleaseDate(),mediaAsset.getMediaassetLocations(),mediaAsset.getMediaassetAttributes()
				);
		*/

	}

	private static final class MediaAssestDataMapper implements
			RowMapper<MediaAssetDetailsData> {

		@Override
		public MediaAssetDetailsData mapRow(ResultSet rs, int rowNum)

				throws SQLException {
			Long mediaId = rs.getLong("id");
			String title = rs.getString("title");
			String type = rs.getString("type");
			String classType = rs.getString("genre");
			String overview = rs.getString("overview");
			String subject = rs.getString("subject");
			String image = rs.getString("image");
			String duration = rs.getString("duration");
			String contentProvider = rs.getString("contentProvider");
			String rated = rs.getString("rated");
			BigDecimal rating = rs.getBigDecimal("rating");
			int ratingCount = rs.getInt("ratingCount");
			//String location = rs.getString("location");
			String status=rs.getString("status");
			Date releaseDate = rs.getDate("releaseDate");

			return new MediaAssetDetailsData(mediaId,title,type,classType,overview,subject,image,duration,contentProvider,rated,rating,ratingCount,null,status,releaseDate,
					null,null,null, null, null, null, null,null);
		}

		public String mediaAssestDataSchema() {

			return " a.id AS id,a.title AS title,a.type AS type,a.genre AS genre,a.release_date AS releaseDate,a.overview AS overview,a.subject AS subject,"
				  +" a.image AS image,a.duration AS duration,a.content_provider AS contentProvider,a.rated AS rated,a.rating AS rating,a.rating_count AS ratingCount,"
				  +" a.status AS status FROM b_media_asset a ";


		}
	}

	@Override
	public List<String> retrieveGenresData(Long mediaId) {
		MediaAssestAttributeDataMapper attributeMapper = new MediaAssestAttributeDataMapper();
		String sql = "select " + attributeMapper.attributeDataSchema()+" where a.attribute_name='Genre' and a.media_id=?;" ;
		return this.jdbcTemplate.query(sql, attributeMapper,new Object[] { mediaId });
		
	}
		private static final class MediaAssestAttributeDataMapper implements RowMapper<String> {
	@Override
	public String mapRow(ResultSet rs, int rowNum)
			throws SQLException {
		String attributeValue = rs.getString("attributeValue");
		return attributeValue;
	}
	public String attributeDataSchema() {

		return " a.attribute_value as attributeValue from b_mediaasset_attributes a";


	}
}
		private static final class MediaAssestAttributeLocationDataMapper implements RowMapper<MediaLocationData> {
			@Override
			public MediaLocationData mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Long languageId = rs.getLong("languageId");
				String formatType = rs.getString("formatType");
				String location = rs.getString("location");
				return new MediaLocationData(languageId,formatType,location);
			}
			public String attributeLocationchema() {

				return " a.language_id AS languageId,a.format_type as formatType,a.location as location FROM b_mediaasset_location a";


			}
		}

		@Override
		public List<String> retrieveProductions(Long mediaId) {
			MediaAssestAttributeDataMapper attributeMapper = new MediaAssestAttributeDataMapper();
			String sql = "select " + attributeMapper.attributeDataSchema()+" where a.attribute_name='Producer' and a.media_id=?" ;
			return this.jdbcTemplate.query(sql, attributeMapper,new Object[] { mediaId });
	}



		@Override
		public List<MediaLocationData> retrieveFilmLocation(Long mediaId) {
			MediaAssestAttributeLocationDataMapper attributeMapper = new MediaAssestAttributeLocationDataMapper();
			String sql = "select " + attributeMapper.attributeLocationchema()+" where a.media_id =?" ;
			return this.jdbcTemplate.query(sql, attributeMapper,new Object[] { mediaId });
		}



		@Override
		public List<String> retrieveWriters(Long mediaId) {
			MediaAssestAttributeDataMapper attributeMapper = new MediaAssestAttributeDataMapper();
			String sql = "select " + attributeMapper.attributeDataSchema()+" where a.attribute_name='Writer' and a.media_id=?;" ;
			return this.jdbcTemplate.query(sql, attributeMapper,new Object[] { mediaId });
		}



		@Override
		public List<String> retrieveDirectors(Long mediaId) {
			MediaAssestAttributeDataMapper attributeMapper = new MediaAssestAttributeDataMapper();
			String sql = "select " + attributeMapper.attributeDataSchema()+" where a.attribute_name='Director' and a.media_id=?;" ;
			return this.jdbcTemplate.query(sql, attributeMapper,new Object[] { mediaId });
		}



		@Override
		public List<String> retrieveActors(Long mediaId) {
			MediaAssestAttributeDataMapper attributeMapper = new MediaAssestAttributeDataMapper();
			String sql = "select " + attributeMapper.attributeDataSchema()+" where a.attribute_name='Actor' and a.media_id=?;" ;
			return this.jdbcTemplate.query(sql, attributeMapper,new Object[] { mediaId });
		}



		@Override
		public List<String> retrieveCountry(Long mediaId) {
			MediaAssestAttributeDataMapper attributeMapper = new MediaAssestAttributeDataMapper();
			String sql = "select " + attributeMapper.attributeDataSchema()+" where a.attribute_name='Country' and a.media_id=?;" ;
			return this.jdbcTemplate.query(sql, attributeMapper,new Object[] { mediaId });
		}


		@Override
		public List<MediaAssetLocationDetails> retrieveMediaAssetLocationData(
				Long mediaId) {
			MediaAssetLocationMapper locationMapper = new MediaAssetLocationMapper();
			String sql = "SELECT " + locationMapper.mediaAssetLocationDataSchema() + " where media_id = ?";
			return jdbcTemplate.query(sql, locationMapper,new Object[] {mediaId});
		}
		
		private static final class MediaAssetLocationMapper implements
		RowMapper<MediaAssetLocationDetails> {

			@Override
			public MediaAssetLocationDetails mapRow(ResultSet rs, int rowNum)
					throws SQLException {
				Long id = rs.getLong("id");
				Long mediaId = rs.getLong("mediaId");
				Integer languageId = rs.getInt("languageId");
				String formatType = rs.getString("formatType");
				String location = rs.getString("location");
				return new MediaAssetLocationDetails(id, mediaId, languageId, formatType, location);
			}
			
			public String mediaAssetLocationDataSchema() {
				return "id as id, media_id as mediaId,language_id as languageId," 
						+ " format_type as formatType, location as location " 
							+ " from b_mediaasset_location";
			}
			
		}

		@Override
		public List<EventPricingData> getEventPriceDetails(Long eventId,String clientType) {
			
			MediaPriceDetailsMapper attributeMapper = new MediaPriceDetailsMapper();
			String sql = "select " + attributeMapper.attributeDataSchema();
			return this.jdbcTemplate.query(sql, attributeMapper,new Object[] { eventId,clientType });
			
		}
			private static final class MediaPriceDetailsMapper implements RowMapper<EventPricingData> {
		@Override
		public EventPricingData mapRow(ResultSet rs, int rowNum) throws SQLException {
			Long eventId=rs.getLong("id");
			//String customerCategory = rs.getString("customer_category");
			String optType = rs.getString("opt_type");
			String formatType = rs.getString("format_type");
			BigDecimal price=rs.getBigDecimal("price");
			return new EventPricingData(eventId,null,formatType,optType, null, null, price,null,null);
			
		}
		public String attributeDataSchema() {

			return"b_event_master.id,b_event_master.event_name,b_event_master.event_description,Ifnull(m_code_value.code_value,'ALL') as CUSTOMER_CATEGORY," +
					" b_event_pricing.opt_type,b_event_pricing.format_type,b_event_pricing.price FROM(b_event_pricing b_event_pricing LEFT OUTER JOIN" +
					" m_code_value m_code_value ON (b_event_pricing.client_typeid = m_code_value.id) and (m_code_value.code_id = 10)) INNER JOIN" +
					" b_event_master b_event_master  ON (b_event_pricing.event_id = b_event_master.id) where b_event_master.id=?" +
					" and Ifnull(m_code_value.code_value,'ALL')=?";


		}
}
}
