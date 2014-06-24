package com.rancard.mobility.contentserver;

import com.rancard.common.CPConnection;
import com.rancard.common.VMCampaign;

/**
 * Created by Mustee on 4/14/2014.
 */
public abstract class ViralMarketing {
    final VMCampaign vmCampaign;

    public ViralMarketing(VMCampaign campaign){
        this.vmCampaign = campaign;
    }

    public abstract void sendHowToMessage(CPConnection cnxn, String msisdn, String smsc, String shortCode);

    public abstract void updateVMTransaction(CPConnection cnxn, String recruiterMSISDN, String smsc) throws Exception;

    public VMCampaign getCampaign(){
        return vmCampaign;
    }

}
