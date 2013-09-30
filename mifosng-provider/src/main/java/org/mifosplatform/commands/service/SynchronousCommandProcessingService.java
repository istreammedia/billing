/**
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this file,
 * You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.mifosplatform.commands.service;

import java.util.Map;

import org.joda.time.DateTime;
import org.mifosplatform.commands.domain.CommandSource;
import org.mifosplatform.commands.domain.CommandSourceRepository;
import org.mifosplatform.commands.domain.CommandWrapper;
import org.mifosplatform.commands.exception.RollbackTransactionAsCommandIsNotApprovedByCheckerException;
import org.mifosplatform.commands.exception.UnsupportedCommandException;
import org.mifosplatform.commands.handler.NewCommandSourceHandler;
import org.mifosplatform.infrastructure.configuration.domain.ConfigurationDomainService;
import org.mifosplatform.infrastructure.core.api.JsonCommand;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResult;
import org.mifosplatform.infrastructure.core.data.CommandProcessingResultBuilder;
import org.mifosplatform.infrastructure.core.serialization.ToApiJsonSerializer;
import org.mifosplatform.infrastructure.security.service.PlatformSecurityContext;
import org.mifosplatform.useradministration.domain.AppUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SynchronousCommandProcessingService implements
		CommandProcessingService {

	private PlatformSecurityContext context;
	private final ApplicationContext applicationContext;
	private final ToApiJsonSerializer<Map<String, Object>> toApiJsonSerializer;
	private CommandSourceRepository commandSourceRepository;
	private final ConfigurationDomainService configurationDomainService;

	@Autowired
	public SynchronousCommandProcessingService(
			final PlatformSecurityContext context,
			final ApplicationContext applicationContext,
			final ToApiJsonSerializer<Map<String, Object>> toApiJsonSerializer,
			final CommandSourceRepository commandSourceRepository,
			final ConfigurationDomainService configurationDomainService) {
		this.context = context;
		this.context = context;
		this.applicationContext = applicationContext;
		this.toApiJsonSerializer = toApiJsonSerializer;
		this.commandSourceRepository = commandSourceRepository;
		this.commandSourceRepository = commandSourceRepository;
		this.configurationDomainService = configurationDomainService;
	}

	@Transactional
	@Override
	public CommandProcessingResult processAndLogCommand(
			final CommandWrapper wrapper, final JsonCommand command,
			final boolean isApprovedByChecker) {

		final boolean rollbackTransaction = this.configurationDomainService
				.isMakerCheckerEnabledForTask(wrapper.taskPermissionName())
				&& !isApprovedByChecker;

		final NewCommandSourceHandler handler = findCommandHandler(wrapper);
		final CommandProcessingResult result = handler.processCommand(command);

		final AppUser maker = context.authenticatedUser();

		CommandSource commandSourceResult = null;
		if (command.commandId() != null) {
			commandSourceResult = this.commandSourceRepository.findOne(command
					.commandId());
			commandSourceResult.markAsChecked(maker, DateTime.now());
		} else {
			commandSourceResult = CommandSource.fullEntryFrom(wrapper, command,
					maker);
		}
		commandSourceResult.updateResourceId(result.resourceId());
		commandSourceResult.updateForAudit(result.getOfficeId(),
				result.getGroupId(), result.getClientId(), result.getLoanId(),
				result.getSavingsId());

		String changesOnlyJson = null;
		if (result.hasChanges()) {
			changesOnlyJson = this.toApiJsonSerializer.serializeResult(result
					.getChanges());
			commandSourceResult.updateJsonTo(changesOnlyJson);
		}

		if (!result.hasChanges() && wrapper.isUpdateOperation()) {
			commandSourceResult.updateJsonTo(null);
		}

		if (commandSourceResult.hasJson()) {
			commandSourceRepository.save(commandSourceResult);
		}

		if (rollbackTransaction) {
			throw new RollbackTransactionAsCommandIsNotApprovedByCheckerException(
					commandSourceResult);
		}

		return result;
	}

	@Transactional
	@Override
	public CommandProcessingResult logCommand(
			final CommandSource commandSourceResult) {

		commandSourceResult.markAsAwaitingApproval();
		commandSourceRepository.save(commandSourceResult);

		return new CommandProcessingResultBuilder()
				.withCommandId(commandSourceResult.getId())
				.withEntityId(commandSourceResult.getResourceId()).build();
	}

	private NewCommandSourceHandler findCommandHandler(final CommandWrapper wrapper) {
		NewCommandSourceHandler handler = null;

		if (wrapper.isConfigurationResource()) {
			handler = applicationContext.getBean("updateGlobalConfigurationCommandHandler",NewCommandSourceHandler.class);
		} else if (wrapper.isDatatableResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createDatatableEntryCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdateMultiple()) {
				handler = applicationContext.getBean("updateOneToManyDatatableEntryCommandHandler",	NewCommandSourceHandler.class);
			} else if (wrapper.isUpdateOneToOne()) {
				handler = applicationContext.getBean("updateOneToOneDatatableEntryCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDeleteMultiple()) {
				handler = applicationContext.getBean("deleteOneToManyDatatableEntryCommandHandler",	NewCommandSourceHandler.class);
			} else if (wrapper.isDeleteOneToOne()) {
				handler = applicationContext.getBean("deleteOneToOneDatatableEntryCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isNoteResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createNoteCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateNoteCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteNoteCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isClientIdentifierResource()) {

			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createClientIdentifierCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateClientIdentifierCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteClientIdentifierCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
 		} else if (wrapper.isClientResource()	&& !wrapper.isClientNoteResource()
				&& !wrapper.isClientIdentifierResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createClientCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateClientCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteClientCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isClientActivation()) {
				handler = applicationContext.getBean("activateClientCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
			// end of client
		} else if (wrapper.isUpdateRolePermissions()) {
			handler = applicationContext.getBean("updateRolePermissionsCommandHandler",NewCommandSourceHandler.class);
		} else if (wrapper.isPermissionResource()) {
			handler = applicationContext.getBean("updateMakerCheckerPermissionsCommandHandler",NewCommandSourceHandler.class);
		} else if (wrapper.isRoleResource()) {

			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createRoleCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateRoleCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}

		} else if (wrapper.isUserResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createUserCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateUserCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteUserCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isStaffResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createStaffCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateStaffCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isGuarantorResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createGuarantorCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateGuarantorCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteGuarantorCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isCollateralResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createCollateralCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateCollateralCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteCollateralCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isCodeResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createCodeCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateCodeCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteCodeCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isCodeValueResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createCodeValueCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateCodeValueCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteCodeValueCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isCurrencyResource()) {
			handler = applicationContext.getBean("updateCurrencyCommandHandler",NewCommandSourceHandler.class);
		} else if (wrapper.isFundResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createFundCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateFundCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isOfficeResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createOfficeCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateOfficeCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isOfficeTransactionResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createOfficeTransactionCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean(
						"deleteOfficeTransactionCommandHandler",
						NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isChargeDefinitionResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createChargeDefinitionCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateChargeDefinitionCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteChargeDefinitionCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isLoanProductResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createLoanProductCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateLoanProductCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isLoanResource()) {
			if (wrapper.isApproveLoanApplication()) {
				handler = applicationContext.getBean("loanApplicationApprovalCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUndoApprovalOfLoanApplication()) {
				handler = applicationContext.getBean("loanApplicationApprovalUndoCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isApplicantWithdrawalFromLoanApplication()) {
				handler = applicationContext.getBean("loanApplicationWithdrawnByApplicantCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isRejectionOfLoanApplication()) {
				handler = applicationContext.getBean("loanApplicationRejectedCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDisbursementOfLoan()) {
				handler = applicationContext.getBean("disburseLoanCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUndoDisbursementOfLoan()) {
				handler = applicationContext.getBean("undoDisbursalLoanCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isLoanRepayment()) {
				handler = applicationContext.getBean("loanRepaymentCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isLoanRepaymentAdjustment()) {
				handler = applicationContext.getBean("loanRepaymentAdjustmentCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isWaiveInterestPortionOnLoan()) {
				handler = applicationContext.getBean("waiveInterestPortionOnLoanCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isLoanWriteOff()) {
				handler = applicationContext.getBean("writeOffLoanCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isCloseLoanAsObligationsMet()) {
				handler = applicationContext.getBean("closeLoanCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isCloseLoanAsRescheduled()) {
				handler = applicationContext.getBean("closeLoanAsRescheduledCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdateLoanOfficer()) {
				handler = applicationContext.getBean("updateLoanOfficerCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isRemoveLoanOfficer()) {
				handler = applicationContext.getBean("removeLoanOfficerCommandHandler",	NewCommandSourceHandler.class);
			} else if (wrapper.isBulkUpdateLoanOfficer()) {
				handler = applicationContext.getBean("bulkUpdateLoanOfficerCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isCreate()) {
				handler = applicationContext.getBean("loanApplicationSubmittalCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("loanApplicationModificationCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("loanApplicationDeletionCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isLoanChargeResource()) {
			if (wrapper.isAddLoanCharge()) {
				handler = applicationContext.getBean("addLoanChargeCommandHandler",	NewCommandSourceHandler.class);
			} else if (wrapper.isDeleteLoanCharge()) {
				handler = applicationContext.getBean("deleteLoanChargeCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdateLoanCharge()) {
				handler = applicationContext.getBean("updateLoanChargeCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isWaiveLoanCharge()) {
				handler = applicationContext.getBean("waiveLoanChargeCommandHandler",NewCommandSourceHandler.class);
			}
		} else if (wrapper.isGLAccountResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createGLAccountCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateGLAccountCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteGLAccountCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isGLClosureResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createGLClosureCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateGLClosureCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteGLClosureCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isJournalEntryResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createJournalEntryCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isRevertJournalEntry()) {
				handler = applicationContext.getBean("reverseJournalEntryCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isSavingsProductResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createSavingsProductCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateSavingsProductCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteSavingsProductCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isSavingsAccountResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createSavingsAccountCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateSavingsAccountCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteSavingsAccountCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isSavingsAccountDeposit()) {
				handler = applicationContext.getBean("depositSavingsAccountCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isSavingsAccountWithdrawal()) {
				handler = applicationContext.getBean("withdrawSavingsAccountCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isSavingsAccountActivation()) {
				handler = applicationContext.getBean("activateSavingsAccountCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isSavingsAccountInterestCalculation()) {
				handler = applicationContext.getBean("calculateInterestSavingsAccountCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isSavingsAccountInterestPosting()) {
				handler = applicationContext.getBean("postInterestSavingsAccountCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isCalendarResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createCalendarCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateCalendarCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteCalendarCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isGroupResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createGroupCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateGroupCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUnassignStaff()) {
				handler = applicationContext.getBean("unassignStaffCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteGroupCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isGroupActivation()) {
				handler = applicationContext.getBean("activateGroupCommandHandler",	NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isCenterResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createCenterCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateCenterCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteCenterCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isCenterActivation()) {
				handler = applicationContext.getBean("activateCenterCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isCollectionSheetResource()) {

			if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateCollectionSheetCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}

		} else if (wrapper.isReportResource()) {

			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createReportCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateReportCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteReportCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}

		} else if (wrapper.isServiceResource()) {

			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createServiceCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateServiceCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteServiceCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}

		}

		else if (wrapper.isContractResource()) {

			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createContractCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateContractCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteContractCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isPlanResource()) {

			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createPlanCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updatePlanCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deletePlanCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		} else if (wrapper.isPriceResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createPriceCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updatePriceCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deletePriceCommandHandler",NewCommandSourceHandler.class);
			} else {
				throw new UnsupportedCommandException(wrapper.commandName());
			}
		}

		else if (wrapper.isOrderResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createOrderCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateOrderCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteOrderCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdatePrice()) {
				handler = applicationContext.getBean("updateOrderCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isRenewOrder()) {
				handler = applicationContext.getBean("renewalOrderCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isReconnectOrder()) {
				handler = applicationContext.getBean("reconnectOrderCommandHandler",NewCommandSourceHandler.class);
			}
		} else if (wrapper.isOrderPriceResource()) {
			if (wrapper.isUpdatePrice()) {
				handler = applicationContext.getBean("updateOrderPriceCommandHandler",NewCommandSourceHandler.class);
			}
		} else if (wrapper.isOneTimeSaleResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createOneTimeSaleCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateOneTimeSaleCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteOneTimeSaleCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isCalculatePrice()) {
				handler = applicationContext.getBean("calculateOneTimeSalePriceCommandHandler",NewCommandSourceHandler.class);
			}
		} else if (wrapper.isAddressResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createAddressCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateAddressCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteAddressCommandHandler",NewCommandSourceHandler.class);
			}else if (wrapper.isNewRecord()) {
				handler = applicationContext.getBean("createNewRecordCommandHandler",NewCommandSourceHandler.class);
			}
		}

		else if (wrapper.isItemResource()) {
			if (wrapper.isCreate()) {
				handler = applicationContext.getBean("createItemCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isUpdate()) {
				handler = applicationContext.getBean("updateItemCommandHandler",NewCommandSourceHandler.class);
			} else if (wrapper.isDelete()) {
				handler = applicationContext.getBean("deleteItemCommandHandler",NewCommandSourceHandler.class);
			}
		}
			else if (wrapper.isInvoiceResource()) {
				if (wrapper.isCreate()) {
					handler = applicationContext.getBean("createInvoiceCommandHandler",NewCommandSourceHandler.class);
				}
			
		} 
			
			else if (wrapper.isAdjustmentResource()) {

	        	if (wrapper.isCreate()) {
	                handler = applicationContext.getBean("createAdjustmentCommandHandler", NewCommandSourceHandler.class);
	            } else if (wrapper.isUpdate()) {
	                handler = applicationContext.getBean("updateAdjustmentCommandHandler", NewCommandSourceHandler.class);
	            } else if (wrapper.isDelete()) {
	                handler = applicationContext.getBean("deleteAdjustmentCommandHandler", NewCommandSourceHandler.class);
	            } else {
	                throw new UnsupportedCommandException(wrapper.commandName());
	            }

	        }else if (wrapper.isPaymentResource()) {
	        	if (wrapper.isCreate()) {
	                handler = applicationContext.getBean("createPaymentCommandHandler", NewCommandSourceHandler.class);
	        	}else if (wrapper.isCreatePaypal()) {
	                handler = applicationContext.getBean("createPaypalPaymentCommandHandler", NewCommandSourceHandler.class);
	            }else {
	                    throw new UnsupportedCommandException(wrapper.commandName());
	                }
			} else if (wrapper.isPaymodeResource()) {
				if (wrapper.isCreate()) {
	                handler = applicationContext.getBean("createPaymodeCommandHandler", NewCommandSourceHandler.class);
	            } else if (wrapper.isUpdate()) {
	                handler = applicationContext.getBean("updatePaymodeCommandHandler", NewCommandSourceHandler.class);
	            } else if (wrapper.isDelete()) {
	                handler = applicationContext.getBean("deletePaymodeCommandHandler", NewCommandSourceHandler.class);
	            } else {
	                throw new UnsupportedCommandException(wrapper.commandName());
	            }
			} else if (wrapper.isBillMasterResource()) {
				if (wrapper.isCreate()) {
	                handler = applicationContext.getBean("createBillMasterCommandHandler", NewCommandSourceHandler.class);
	            } else if (wrapper.isUpdate()) {
	                handler = applicationContext.getBean("updateBillMasterCommandHandler", NewCommandSourceHandler.class);
	            } else if (wrapper.isDelete()) {
	                handler = applicationContext.getBean("deleteBillMasterCommandHandler", NewCommandSourceHandler.class);
	            } else {
	                throw new UnsupportedCommandException(wrapper.commandName());
	            }
			} else if (wrapper.isInventoryResource()) {
	        	if (wrapper.isCreate()) {
					handler = applicationContext.getBean("createInventoryItemsCommandHandler", NewCommandSourceHandler.class);
	        	}
			} else if (wrapper.isGrnResource()) {
				if (wrapper.isCreate()){
					handler = applicationContext.getBean("createInventoryGrnCommandHandler", NewCommandSourceHandler.class);
				}
			} else if (wrapper.isAllocateHardwareResource()) {
				if(wrapper.isCreate()){
					handler = applicationContext.getBean("createInventoryItemAllocationCommandHandler",NewCommandSourceHandler.class);
				}
			} else if (wrapper.isUploadStatusResource()) {
	        	if (wrapper.isCreate()) {
					handler = applicationContext.getBean("uploadStatusWritePlatformService", NewCommandSourceHandler.class);
				}	
	        } else if (wrapper.isChargeCodeResource()) {
	        	if(wrapper.isCreate()){
	        		handler = applicationContext.getBean("createChargeCodeCommandHandler",NewCommandSourceHandler.class);
	        	}if (wrapper.isUpdateChargeCode()) {
					handler = applicationContext.getBean("updateChargeCodeCommandHandler",NewCommandSourceHandler.class);
				}
			} else if (wrapper.isTaxMapResource()) {
				if(wrapper.isCreate()){
					handler = applicationContext.getBean("createTaxMapCommandHandler",NewCommandSourceHandler.class);
				}if(wrapper.isUpdateTaxMap()){
					handler = applicationContext.getBean("updateTaxMapCommandHandler",NewCommandSourceHandler.class);
				}
			} else if (wrapper.isTicketResource()) {
	            if(wrapper.isCreateTicket()) {
	                handler = applicationContext.getBean("createTicketMasterCommandHandler", NewCommandSourceHandler.class);
	               } else if (wrapper.isUpdateTicket()) {
	                handler = applicationContext.getBean("updateTicketMasterCommandHandler", NewCommandSourceHandler.class);
	               } else if (wrapper.isCloseTicket()) {
	                handler = applicationContext.getBean("deleteTicketMasterCommandHandler", NewCommandSourceHandler.class);
	               } else {
	                throw new UnsupportedCommandException(wrapper.commandName());
	               }
	              } else if(wrapper.isEventResource()) {
	               if(wrapper.isCreateEvent()) {
	                handler = applicationContext.getBean("createEventMasterCommandHandler",NewCommandSourceHandler.class);
	               } else if(wrapper.isUpdateEvent()) {
	                handler = applicationContext.getBean("updateEventMasterCommandHandler",NewCommandSourceHandler.class);
	               } else if(wrapper.isCloseEvent()) {
	                handler = applicationContext.getBean("closeEventMasterCommandHandler",NewCommandSourceHandler.class);
	               } else {
	                throw new UnsupportedCommandException(wrapper.commandName());
	               }
	              } else if(wrapper.isEventPricingResource()){
	               if(wrapper.isCreateEventPrice()) {
	                handler = applicationContext.getBean("createEventPriceCommandHandler",NewCommandSourceHandler.class);
	               } else if(wrapper.isUpdateEventPrice()) {
	                handler = applicationContext.getBean("updateEventPriceCommandHandler",NewCommandSourceHandler.class);
	               } else if(wrapper.isCloseEventPrice()) {
	                handler = applicationContext.getBean("closeEventPriceCommandHandler",NewCommandSourceHandler.class);
	               } else {
	                throw new UnsupportedCommandException(wrapper.commandName());
	               }
	               }else if(wrapper.isEventOrderResource()){
		             if(wrapper.isCreateEventOrder()) {
			          handler = applicationContext.getBean("createEventOrderCommandHandler",NewCommandSourceHandler.class);
		           } else if(wrapper.isUpdateEventPrice()) {
			           handler = applicationContext.getBean("updateEventPriceCommandHandler",NewCommandSourceHandler.class);
			       } else if(wrapper.isCloseEventPrice()) {
			          handler = applicationContext.getBean("closeEventPriceCommandHandler",NewCommandSourceHandler.class);
			       } else {
			           throw new UnsupportedCommandException(wrapper.commandName());
			       }
	               }else if(wrapper.isMessageResource()){
		            	 if(wrapper.isCreateBillingMessage()) {
					            handler = applicationContext.getBean("createBillingMessageTemplateCommandHandler",NewCommandSourceHandler.class);
			               } else if(wrapper.isUpdateBillingMessage()) {
					        		handler = applicationContext.getBean("updateBillingMessageTemplateCommandHandler",NewCommandSourceHandler.class);
			               } else if(wrapper.isDeleteBillingMessage()) {
					        		handler = applicationContext.getBean("deleteBillingMessageTemplateCommandHandler",NewCommandSourceHandler.class);
			               } else if(wrapper.isCreateMessageData()) {
					        		handler = applicationContext.getBean("createMessageDataCommandHandler",NewCommandSourceHandler.class);
			               } else {
					           throw new UnsupportedCommandException(wrapper.commandName());
					       }
			               }else if (wrapper.isInventoryItemAllocatable()) {
	       			handler = applicationContext.getBean("addInventoryItemAllocationCommandHandler",NewCommandSourceHandler.class);
	           	   }else if(wrapper.isMediaAssetResource()){
			         if(wrapper.isCreateMediaAsset()) {
					 handler = applicationContext.getBean("createMediaAssetCommandHandler",NewCommandSourceHandler.class);
				   } else if(wrapper.isUpdateMediaAsset()) {
					 handler = applicationContext.getBean("updateAssetCommandHandler",NewCommandSourceHandler.class);
				   } else if(wrapper.isCloseMediaAsset()) {
					 handler = applicationContext.getBean("deleteAssetCommandHandler",NewCommandSourceHandler.class);
				   }
			       } else if(wrapper.isBatch()){
						if(wrapper.isCreate()){
							handler = applicationContext.getBean("createBatchJobCommandHandler",NewCommandSourceHandler.class);
						}
					} else if (wrapper.isClientProspect()) {
						if(wrapper.isCreate()){
							handler = applicationContext.getBean("createClientProspectCommandHandler",NewCommandSourceHandler.class);
						} if(wrapper.isUpdateClientProspect()){
							handler = applicationContext.getBean("updateClientProspectCommandHandler",NewCommandSourceHandler.class);
						} if(wrapper.isDeleteClientProspect()){
							handler =applicationContext.getBean("deleteClientProspectCommandHandler",NewCommandSourceHandler.class);
						} if(wrapper.isConvertClientProspect()){
							handler = applicationContext.getBean("createClientProspectConvertCommandHandler",NewCommandSourceHandler.class);
						}
				    } else if (wrapper.isOwnedHardware()) {
				    	   if(wrapper.isCreate()){
				    		   handler = applicationContext.getBean("createOwnedHardwareCommandHandler",NewCommandSourceHandler.class);
				    	   }
				    } else if(wrapper.isSchedulling()){
						if(wrapper.isCreate()){
							handler = applicationContext.getBean("createBatchJobSchedulingCommandHandler", NewCommandSourceHandler.class);
					}
				    }else if (wrapper.isCountryCurrencyResource()) {
						if(wrapper.isCreate()){
							handler = applicationContext.getBean("createCountryCurrencyCommandHandler",NewCommandSourceHandler.class);
						}if(wrapper.isUpdateCurrencyConfig()){
							handler = applicationContext.getBean("updateCountryCurrencyCommandHandler",NewCommandSourceHandler.class);
						} if(wrapper.isDeleteCurrencyConfig()){
							handler =applicationContext.getBean("deleteCountryCurrencyCommandHandler",NewCommandSourceHandler.class);
						} 
				    }else if(wrapper.isEpgProgramGuide()){
				    	   if(wrapper.isCreate()){
				    		   handler = applicationContext.getBean("createEpgProgramGuideCommandHandler", NewCommandSourceHandler.class);
				    	   }
				    }else if(wrapper.isAssetResource()){
		           		 if(wrapper.isUpdateAsset()) {
				        		handler = applicationContext.getBean("updateAssetCommandHandler",NewCommandSourceHandler.class);
						   } else if(wrapper.isDeleteAsset()) {
				        		handler = applicationContext.getBean("deleteAssetCommandHandler",NewCommandSourceHandler.class);	
						   } else{
					           throw new UnsupportedCommandException(wrapper.commandName());
					       }
			       }else if(wrapper.isRegionResource()){
		           		 if(wrapper.isCreateRegion()) {
				        		handler = applicationContext.getBean("createRegionCommandHandler",NewCommandSourceHandler.class);
						   } else if(wrapper.isUpdateRegion()) {
				        		handler = applicationContext.getBean("updateRegionCommandHandler",NewCommandSourceHandler.class);	
						   } else if(wrapper.isDeleteRegion()) {
				        		handler = applicationContext.getBean("deleteRegionCommandHandler",NewCommandSourceHandler.class);	
						   }else{
					           throw new UnsupportedCommandException(wrapper.commandName());
					       }
			       }else if(wrapper.isSelfCare()){
			    	   if(wrapper.isCreate()){
			    		   handler = applicationContext.getBean("createSelfCareCommandHandler",NewCommandSourceHandler.class);
			    	   }
			       }else if(wrapper.isDiscountResource()){
		           		 if(wrapper.isCreateDiscount()) {
				        		handler = applicationContext.getBean("createDiscountCommandHandler",NewCommandSourceHandler.class);
						   } else if(wrapper.isUpdateDiscount()) {
				        		handler = applicationContext.getBean("updateDiscountCommandHandler",NewCommandSourceHandler.class);	
						   } else if(wrapper.isDeleteDiscount()) {
				        		handler = applicationContext.getBean("deleteDiscountCommandHandler",NewCommandSourceHandler.class);	
						   }else{
					           throw new UnsupportedCommandException(wrapper.commandName());
					       }
			       }else if(wrapper.isMRN()){
			           if(wrapper.isCreateMRN()){
			               handler = applicationContext.getBean("createMRNDetailsCommandHandler",NewCommandSourceHandler.class);
			              }else if(wrapper.moveMRN()){
			               handler = applicationContext.getBean("createMRNDetailsMoveCommandHandler",NewCommandSourceHandler.class);
			              }
			             } else if (wrapper.isSupplier()) {
			              if(wrapper.isCreateSupplier()){
			               handler = applicationContext.getBean("createSupplierCommandHandler",NewCommandSourceHandler.class);
			              }
			       }else if(wrapper.isRandomGeneratorResource()){
			                 if(wrapper.isCreateRandomGenerator()) {
			                     handler = applicationContext.getBean("createRandomGeneratorCommandHandler",NewCommandSourceHandler.class);
			                 }
			       }else if (wrapper.isSchedulerResource()) {
			            if (wrapper.isUpdate()) {
			                handler = this.applicationContext.getBean("updateJobDetailCommandhandler", NewCommandSourceHandler.class);
			            }else if (wrapper.isCreate()) {
			                handler = this.applicationContext.getBean("createJobDetailCommandhandler", NewCommandSourceHandler.class);
			            }else if (wrapper.isDelete()) {
			                handler = this.applicationContext.getBean("deleteJobDetailCommandhandler", NewCommandSourceHandler.class);
			            }else {
			                throw new UnsupportedCommandException(wrapper.commandName());
			            }
			        }else if(wrapper.isEntitlementResource()){
		                 if(wrapper.isCreateEntitlement()) {
		                     handler = applicationContext.getBean("createEntitlementCommandHandler",NewCommandSourceHandler.class);
		                 }
		           }
			       
			       else {
			               throw new UnsupportedCommandException(wrapper.commandName());
		              }
			       
					
	       return handler;
	      
	    
	}
}