 package com.rancard.util;
 
 import java.util.List;
 import org.displaytag.properties.SortOrderEnum;
 
 public class PaginatedList
   extends Page
   implements org.displaytag.pagination.PaginatedList
 {
   private final int pageSize;
   private final int pageNumber;
   private final String sortOrder;
   private final String sortCriterion;
   
   public PaginatedList(List partialList, int fullListSize, int pageSize, int pageNumber, String sortOrder, String sortCriterion)
   {
     super(partialList, fullListSize);
     this.pageSize = pageSize;
     this.sortOrder = sortOrder;
     this.sortCriterion = sortCriterion;
     this.pageNumber = pageNumber;
   }
   
   public int getFullListSize()
   {
     return super.getnumAllResults();
   }
   
   public List getList()
   {
     return super.getList();
   }
   
   public int getObjectsPerPage()
   {
     return this.pageSize;
   }
   
   public int getPageNumber()
   {
     return this.pageNumber;
   }
   
   public String getSearchId()
   {
     return null;
   }
   
   public String getSortCriterion()
   {
     return this.sortCriterion;
   }
   
   public SortOrderEnum getSortDirection()
   {
     return "asc".equals(this.sortOrder) ? SortOrderEnum.ASCENDING : SortOrderEnum.DESCENDING;
   }
   
   public static int getStartOfNextPage(int currPage, int pageSize)
   {
     currPage = currPage == 0 ? 1 : currPage;
     
 
 
     return (currPage - 1) * pageSize;
   }
 }



/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar

 * Qualified Name:     com.rancard.util.PaginatedList

 * JD-Core Version:    0.7.0.1

 */