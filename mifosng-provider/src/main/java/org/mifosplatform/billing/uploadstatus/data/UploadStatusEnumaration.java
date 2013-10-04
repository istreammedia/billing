package org.mifosplatform.billing.uploadstatus.data;


import org.mifosplatform.billing.plan.domain.StatusTypeEnum;
import org.mifosplatform.billing.uploadstatus.domain.UploadStatusEnum;
import org.mifosplatform.infrastructure.core.data.EnumOptionData;

public class UploadStatusEnumaration {

	public static EnumOptionData OrderStatusType(final int id) {
		return OrderStatusType(UploadStatusEnum.fromInt(id));
	}

	public static EnumOptionData OrderStatusType(final UploadStatusEnum type) {
		final String codePrefix = "upload.files.process.status.";
		EnumOptionData optionData = null;
		switch (type) {
		case NEW:
			optionData = new EnumOptionData(UploadStatusEnum.NEW.getValue().longValue(), codePrefix + UploadStatusEnum.NEW.getCode(), "NEW");
			break;
		case COMPLETED:
			optionData = new EnumOptionData(UploadStatusEnum.COMPLETED.getValue().longValue(), codePrefix + UploadStatusEnum.COMPLETED.getCode(), "COMPLETED");
			break;

		case ERROR:
			optionData = new EnumOptionData(UploadStatusEnum.ERROR.getValue().longValue(), codePrefix + UploadStatusEnum.ERROR.getCode(), "ERROR");
			break;

		default:
			optionData = new EnumOptionData(StatusTypeEnum.INVALID.getValue().longValue(), StatusTypeEnum.INVALID.getCode(), "INVALID");
			break;
		}
		return optionData;
	}

}
