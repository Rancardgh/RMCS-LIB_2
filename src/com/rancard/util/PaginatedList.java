/*  1:   */ package com.rancard.util;
/*  2:   */ 
/*  3:   */ import java.util.List;
/*  4:   */ import org.displaytag.properties.SortOrderEnum;
/*  5:   */ 
/*  6:   */ public class PaginatedList
/*  7:   */   extends Page
/*  8:   */   implements org.displaytag.pagination.PaginatedList
/*  9:   */ {
/* 10:   */   private final int pageSize;
/* 11:   */   private final int pageNumber;
/* 12:   */   private final String sortOrder;
/* 13:   */   private final String sortCriterion;
/* 14:   */   
/* 15:   */   public PaginatedList(List partialList, int fullListSize, int pageSize, int pageNumber, String sortOrder, String sortCriterion)
/* 16:   */   {
/* 17:35 */     super(partialList, fullListSize);
/* 18:36 */     this.pageSize = pageSize;
/* 19:37 */     this.sortOrder = sortOrder;
/* 20:38 */     this.sortCriterion = sortCriterion;
/* 21:39 */     this.pageNumber = pageNumber;
/* 22:   */   }
/* 23:   */   
/* 24:   */   public int getFullListSize()
/* 25:   */   {
/* 26:46 */     return super.getnumAllResults();
/* 27:   */   }
/* 28:   */   
/* 29:   */   public List getList()
/* 30:   */   {
/* 31:53 */     return super.getList();
/* 32:   */   }
/* 33:   */   
/* 34:   */   public int getObjectsPerPage()
/* 35:   */   {
/* 36:60 */     return this.pageSize;
/* 37:   */   }
/* 38:   */   
/* 39:   */   public int getPageNumber()
/* 40:   */   {
/* 41:66 */     return this.pageNumber;
/* 42:   */   }
/* 43:   */   
/* 44:   */   public String getSearchId()
/* 45:   */   {
/* 46:73 */     return null;
/* 47:   */   }
/* 48:   */   
/* 49:   */   public String getSortCriterion()
/* 50:   */   {
/* 51:79 */     return this.sortCriterion;
/* 52:   */   }
/* 53:   */   
/* 54:   */   public SortOrderEnum getSortDirection()
/* 55:   */   {
/* 56:85 */     return "asc".equals(this.sortOrder) ? SortOrderEnum.ASCENDING : SortOrderEnum.DESCENDING;
/* 57:   */   }
/* 58:   */   
/* 59:   */   public static int getStartOfNextPage(int currPage, int pageSize)
/* 60:   */   {
/* 61:91 */     currPage = currPage == 0 ? 1 : currPage;
/* 62:   */     
/* 63:   */ 
/* 64:   */ 
/* 65:95 */     return (currPage - 1) * pageSize;
/* 66:   */   }
/* 67:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.util.PaginatedList
 * JD-Core Version:    0.7.0.1
 */