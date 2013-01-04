package com.rancard.mobility.contentserver;
import java.util.*;

public abstract class CsCpRelationship {

       
    public static void insertRelationship(String cs_id, String listId) throws
            Exception {

        RelationshipDB.insertRelationship(cs_id, listId);
    }

    /*
         Deletes an entry
     */
    public static void deleteRelationshipForProvider(String id) throws Exception {
        RelationshipDB.deleteRelationshipForProvider(id);
    }

    public static void deleteRelationshipForContentSubscriber(String id) throws Exception {
        RelationshipDB.deleteRelationshipForContentSubscriber(id);
    }

    public static void deleteRelationship(String csid, String cpid) throws Exception {
        RelationshipDB.deleteRelationship(csid, cpid);
    }

    public static void  deleteRelationship(String[] csid, String cpid)throws Exception {
        RelationshipDB.deleteRelationship(csid, cpid);
    }
    
    public static void     deleteProviderRelationship(String[] cpid, String csid) throws Exception {
        RelationshipDB.deleteProviderRelationship(cpid, csid);
    }
    
    /*
         Retrieves an item as a bean
     */
    public static ArrayList viewProvidersForContentSubscriber(String id) throws
            Exception {
        return RelationshipDB.viewProvidersForContentSubscriber(id);
    }

    public static ArrayList viewContentSubscribersForProvider(String id) throws
            Exception {
        return RelationshipDB.viewContentSubscribersForProvider(id);
    }
 public static ArrayList viewProvidersDetailsForContentSubscriber(String id) throws
            Exception {
        return RelationshipDB.viewProvidersDetailsForContentSubscriber(id);
    }

    public static ArrayList viewContentSubscribersDetailsForProvider(String id) throws
            Exception {
        return RelationshipDB.viewContentSubscriberDetailsForProvider(id);
    }

}
