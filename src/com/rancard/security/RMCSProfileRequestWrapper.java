/*
 * RMCSProfileRequestWrapper.java
 *
 * Created on March 13, 2007, 5:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.security;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;
import com.rancard.mobility.contentserver.ContentType;
import com.rancard.mobility.contentprovider.User;
/**
 *
 * @author Mawudem
 */
public class RMCSProfileRequestWrapper extends HttpServletRequestWrapper{
    
    /** Creates a new instance of RMCSProfileRequestWrapper */
    
    
    public RMCSProfileRequestWrapper (HttpServletRequest servletRequest) {
        super (servletRequest);
    }
    
    public String getParameter (String paramName) {
        String paramVal = "";
        
        if(!"type".equals (paramName)) {
            paramVal = super.getParameter (paramName);
        } else {            
            paramVal = super.getParameter ("type");
            int paramIntVal = Integer.parseInt ( paramVal );
            
            boolean serviceExist = checkService (paramIntVal );
            if(!serviceExist) {
                return "0";
            }
            
        }
        return paramVal;
    }
    
    private boolean checkService (int serviceType ) {
        HttpSession session = super.getSession (true);//ensure that the session is created if not present already
        User profile = (User) session.getAttribute ("user");
        
        boolean serviceExist = false;
        
        try {
            serviceExist = ContentType.isRegisteredWithProfile (serviceType, profile.getId () );
        } catch(Exception ex) {
            
        }
        return serviceExist;
    }
    
}



