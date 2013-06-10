/*
 * PaginatedList.java
 *
 * Created on April 25, 2007, 5:55 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package com.rancard.util;

//import org.displaytag.pagination.PaginatedList;
import org.displaytag.properties.SortOrderEnum;
import java.io.*;
import java.util.*;

/**
 *
 * @author Administrator
 */
public class PaginatedList extends Page implements org.displaytag.pagination.PaginatedList{
    
    
    // private final int fullListSize;
     private final int pageSize;
      private final int pageNumber;
      private final String sortOrder;
     private final String sortCriterion;
     // private final List list = new ArrayList();
    
       //PaginatedResultSet(ResultSet rs, int fullListSize, int pageSize, int pageNumber, boolean sortOrder, String sortCriterion)
    /** Creates a new instance of PaginatedList */
    public PaginatedList(List partialList,int fullListSize, int pageSize, int pageNumber, String sortOrder, String sortCriterion) {
         //Page(List l, int s, boolean hasNext, int t, int u) 
        super(partialList,fullListSize);
        this.pageSize = pageSize;
        this.sortOrder = sortOrder;
        this.sortCriterion = sortCriterion;
        this.pageNumber = pageNumber;
        
    }

 /* Returns the size of the full list*/
     public int getFullListSize()
      {
          return super.getnumAllResults();
      }
     
    
 /* Returns the current partial list*/
      public List getList()
      {
          return super.getList();
      }
    

/* Returns the number of objects per page.*/
      public int getObjectsPerPage()
      {
          return pageSize;
      }
  
  /*  Returns the page number of the partial list (starts from 1)*/
      public int getPageNumber()
      {
          return pageNumber;
      }
   
/* Returns an ID for the search used to get the list.*/ 

      public String getSearchId()
      {
          return null;
      }
    
 /*Returns the sort criterion used to externally sort the full list*/
      public String getSortCriterion()
      {
          return sortCriterion;
      }
  
/*Returns the sort direction used to externally sort the full list*/  
      public SortOrderEnum getSortDirection()
      {
          return ("asc".equals(sortOrder)) ? SortOrderEnum.ASCENDING : SortOrderEnum.DESCENDING;
      }
      
  //----helper Methods--------------------------------------
      public static int getStartOfNextPage(int currPage, int pageSize) {
       // int currPageInt = 1;
        currPage = (currPage==0)? 1 : currPage;
        /*if (currPageStr != null && !"".equals(currPageStr) )
            currPageInt = Integer.parseInt(currPageStr);*/
        
        return  (currPage- 1) * pageSize;
        
    }

}







