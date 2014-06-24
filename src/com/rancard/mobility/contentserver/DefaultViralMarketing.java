package com.rancard.mobility.contentserver;

import com.rancard.common.CPConnection;
import com.rancard.common.VMCampaign;
import com.rancard.common.VMTransaction;
import com.rancard.common.VMTransactionStatus;
import com.rancard.mobility.common.ThreadedMessageSender;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Mustee on 4/14/2014.
 */
public class DefaultViralMarketing extends ViralMarketing {

    public DefaultViralMarketing(VMCampaign campaign) {
        super(campaign);
    }

    @Override
    public void sendHowToMessage(CPConnection cnxn, String msisdn, String smsc, String shortCode) {
        if (vmCampaign == null || !vmCampaign.isActive()) {
            return;
        }


        String howToMessage = StringUtils.isBlank(vmCampaign.getHowToMessage()) ? VMCampaign.DEFAULT_HOW_TO_MESSAGE : vmCampaign.getHowToMessage();
        (new Thread(new ThreadedMessageSender(cnxn, msisdn, vmCampaign.getMessageSender(), howToMessage.replace("@@shortCode@@", shortCode), null,
                vmCampaign.getAccountID(), vmCampaign.getKeyword(),
                "RMCS", smsc, 0, 0))).start();
    }

    @Override
    public void updateVMTransaction(CPConnection cnxn, String recipientMSISDN, String smsc) throws Exception {
        if (vmCampaign == null || !vmCampaign.isActive()) {
            return;
        }

        VMTransaction transaction = VMTransaction.find(vmCampaign.getCampaignID(), recipientMSISDN);
        if(transaction == null || transaction.getStatus() == VMTransactionStatus.INV_ACCEPTED || transaction.getStatus() == VMTransactionStatus.inv_accepted){
            return;
        }
        VMTransaction.updateStatus(vmCampaign.getCampaignID(), recipientMSISDN, VMTransactionStatus.INV_ACCEPTED);

        String invitationSuccess = StringUtils.isBlank(vmCampaign.getInviteAcceptedMessage()) ? VMCampaign.DEFAULT_INVITATION_ACCEPTED_MESSAGE : vmCampaign.getInviteAcceptedMessage();
        (new Thread(new ThreadedMessageSender(cnxn, transaction.getRecruiterMSISDN(), vmCampaign.getMessageSender(), invitationSuccess, null,
                vmCampaign.getAccountID(), vmCampaign.getKeyword(), "RMCS", smsc, 0, 0))).start();
    }
}
