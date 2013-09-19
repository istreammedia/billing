package org.mifosplatform.billing.media.service;

import java.util.Map;

import org.mifosplatform.billing.media.domain.MediaAsset;
import org.mifosplatform.billing.media.serialization.MediaAssetCommandFromApiJsonDeserializer;
import org.mifosplatform.billing.mediadetails.domain.MediaAssetRepository;
import org.mifosplatform.billing.mediadetails.domain.MediaassetAttributes;
import org.mifosplatform.billing.mediadetails.domain.MediaassetLocation;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;


@Service
public class MediaAssetWritePlatformServiceImpl implements MediaAssetWritePlatformService {
	 private final static Logger logger = LoggerFactory.getLogger(MediaAssetWritePlatformServiceImpl.class);
	private final PlatformSecurityContext context;
	private final MediaAssetCommandFromApiJsonDeserializer fromApiJsonDeserializer;
	private final FromJsonHelper fromApiJsonHelper;
	private final MediaAssetRepository assetRepository;
	   
	@Autowired
	public MediaAssetWritePlatformServiceImpl(final PlatformSecurityContext context,
			final FromJsonHelper fromApiJsonHelper,final MediaAssetRepository assetRepository,
			final MediaAssetCommandFromApiJsonDeserializer fromApiJsonDeserializer) {
		this.context = context;
		this.assetRepository=assetRepository;
		this.fromApiJsonDeserializer=fromApiJsonDeserializer;
		this.fromApiJsonHelper=fromApiJsonHelper;

	}

	@Override
	public CommandProcessingResult createMediaAsset(JsonCommand command) {

		try {

		 this.context.authenticatedUser();
		     this.fromApiJsonDeserializer.validateForCreate(command.json());
			  MediaAsset mediaAsset=MediaAsset.fromJson(command);
			 final JsonArray mediaassetAttributesArray=command.arrayOfParameterNamed("mediaassetAttributes").getAsJsonArray();
			    String[] mediaassetAttributes =null;
			    mediaassetAttributes=new String[mediaassetAttributesArray.size()];
			    for(int i=0; i<mediaassetAttributesArray.size();i++){
			    	mediaassetAttributes[i] =mediaassetAttributesArray.get(i).toString();
			    	//JsonObject temp = mediaassetAttributesArray.getAsJsonObject();
			    	

			    }
			   //For Media Attributes
				 for (String mediaassetAttribute : mediaassetAttributes) 
				 {
					
					     final JsonElement element = fromApiJsonHelper.parse(mediaassetAttribute);
					     final String mediaAttributeType = fromApiJsonHelper.extractStringNamed("attributeType", element);
					     final String mediaattributeName = fromApiJsonHelper.extractStringNamed("attributeName", element);
					     final String mediaattributeValue = fromApiJsonHelper.extractStringNamed("attributevalue", element);
					     final String mediaattributeNickname= fromApiJsonHelper.extractStringNamed("attributeNickname", element);
					     final String mediaattributeImage= fromApiJsonHelper.extractStringNamed("attributeImage", element);
	                     MediaassetAttributes attributes=new MediaassetAttributes(mediaAttributeType,mediaattributeName,mediaattributeValue,
			              mediaattributeNickname,mediaattributeImage);
   	                       mediaAsset.add(attributes);
				  }
				 
				  final JsonArray mediaassetLocationsArray=command.arrayOfParameterNamed("mediaAssetLocations").getAsJsonArray();
				  String[] mediaassetLocations =null;
				  mediaassetLocations=new String[mediaassetLocationsArray.size()];
				  for(int i=0; i<mediaassetLocationsArray.size();i++){
					  
				    	mediaassetLocations[i] =mediaassetLocationsArray.get(i).toString();
   			       
				  }
				   //For Media Attributes
					 for (String mediaassetLocationData : mediaassetLocations) {
						 
						     final JsonElement element = fromApiJsonHelper.parse(mediaassetLocationData);
						     final Long languageId = fromApiJsonHelper.extractLongNamed("languageId", element);
						     final String formatType = fromApiJsonHelper.extractStringNamed("formatType", element);
						     final String location = fromApiJsonHelper.extractStringNamed("location", element);
		              MediaassetLocation mediaassetLocation = new MediaassetLocation(languageId,formatType,location);
		              mediaAsset.addMediaLocations(mediaassetLocation);
					  }		 
				 
                     this.assetRepository.save(mediaAsset);
			         return new CommandProcessingResult(mediaAsset.getId());

		} catch (DataIntegrityViolationException dve) {
			 handleCodeDataIntegrityIssues(command, dve);
			return new CommandProcessingResult(Long.valueOf(-1));
		}
	}

	private void handleCodeDataIntegrityIssues(JsonCommand command,
			DataIntegrityViolationException dve) {
		
	}

	@Override
	public CommandProcessingResult updateAsset(JsonCommand command) {
		try {
		    this.context.authenticatedUser();
		    this.fromApiJsonDeserializer.validateForCreate(command.json());
		    final MediaAsset mediaAsset=retriveMessageBy(command.entityId());
		    mediaAsset.getMediaassetAttributes().clear();
		    mediaAsset.getMediaassetLocations().clear();
		 
	        final Map<String, Object> changes = mediaAsset.updateAssetDetails(command);
	        
	        final JsonArray mediaassetAttributesArray=command.arrayOfParameterNamed("mediaassetAttributes").getAsJsonArray();
	        
		    String[] assetAttributes =null;
		    assetAttributes=new String[mediaassetAttributesArray.size()];
		    for(int i=0; i<mediaassetAttributesArray.size();i++){
		    	assetAttributes[i] =mediaassetAttributesArray.get(i).toString();			    
		    }
		   //For Media Attributes
			 for (String mediaassetAttribute : assetAttributes) {
				
				     final JsonElement element = fromApiJsonHelper.parse(mediaassetAttribute);
				     final String mediaAttributeType = fromApiJsonHelper.extractStringNamed("attributeType", element);
				     final String mediaattributeName = fromApiJsonHelper.extractStringNamed("attributeName", element);
				     final String mediaattributeValue = fromApiJsonHelper.extractStringNamed("attributevalue", element);
				     final String mediaattributeNickname= fromApiJsonHelper.extractStringNamed("attributeNickname", element);
				     final String mediaattributeImage= fromApiJsonHelper.extractStringNamed("attributeImage", element);
				     MediaassetAttributes attributes=new MediaassetAttributes(mediaAttributeType,mediaattributeName,
		               mediaattributeValue,mediaattributeNickname,mediaattributeImage);
	   	                     mediaAsset.add(attributes);	           
	            
			  }
			 
			  final JsonArray mediaassetLocationsArray=command.arrayOfParameterNamed("mediaAssetLocations").getAsJsonArray();
			 
			  String[] mediaassetLocations =null;
			  mediaassetLocations=new String[mediaassetLocationsArray.size()];
			  for(int i=0; i<mediaassetLocationsArray.size();i++){
				  
			    	mediaassetLocations[i] =mediaassetLocationsArray.get(i).toString();
			       
			  }
			   //For Media Attributes
				 for (String mediaassetLocationData : mediaassetLocations) {
					 
					     final JsonElement element = fromApiJsonHelper.parse(mediaassetLocationData);
					     final Long languageId = fromApiJsonHelper.extractLongNamed("languageId", element);
					     final String formatType = fromApiJsonHelper.extractStringNamed("formatType", element);
					     final String location = fromApiJsonHelper.extractStringNamed("location", element);
	              MediaassetLocation mediaassetLocation = new MediaassetLocation(languageId,formatType,location);
	              mediaAsset.addMediaLocations(mediaassetLocation);
				  }		 
				 if(!changes.isEmpty()){
				     this.assetRepository.save(mediaAsset);
				   }
                 return new CommandProcessingResult(mediaAsset.getId());
				 
                
             }catch (DataIntegrityViolationException dve) {
               			 handleCodeDataIntegrityIssues(command, dve);
               			return new CommandProcessingResult(Long.valueOf(-1));
              }
	}
	
	 private MediaAsset retriveMessageBy(Long assetId) {
         final MediaAsset mediaAsset = this.assetRepository.findOne(assetId);
          return mediaAsset;
}

	 @Override
		public CommandProcessingResult deleteAsset(JsonCommand command) {
			               context.authenticatedUser();
			               final MediaAsset mediaAsset=retriveMessageBy(command.entityId());
			               if (mediaAsset == null) {
				                throw new DataIntegrityViolationException(mediaAsset.toString());
			                }
			                  mediaAsset.isDelete();
			                  this.assetRepository.save(mediaAsset);
			                  return new CommandProcessingResult(mediaAsset.getId());
	    }
}
