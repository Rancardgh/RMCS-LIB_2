/*   1:    */ package com.rancard.util;
/*   2:    */ 
/*   3:    */ import java.io.Serializable;
/*   4:    */ import java.util.ArrayList;
/*   5:    */ import java.util.Collections;
/*   6:    */ import java.util.List;
/*   7:    */ 
/*   8:    */ public class Page
/*   9:    */   implements Serializable
/*  10:    */ {
/*  11: 17 */   public static final Page EMPTY_PAGE = new Page(Collections.EMPTY_LIST, 0, false, 0);
/*  12:    */   List objects;
/*  13:    */   int start;
/*  14:    */   int totalNoPages;
/*  15:    */   int numAllResults;
/*  16:    */   boolean hasNext;
/*  17:    */   
/*  18:    */   public Page(List l, int s, boolean hasNext, int t)
/*  19:    */   {
/*  20: 33 */     this.objects = new ArrayList(l);
/*  21: 34 */     this.start = s;
/*  22:    */     
/*  23:    */ 
/*  24:    */ 
/*  25:    */ 
/*  26:    */ 
/*  27: 40 */     this.hasNext = hasNext;
/*  28: 41 */     this.totalNoPages = t;
/*  29:    */   }
/*  30:    */   
/*  31:    */   public Page(List l, int s, boolean hasNext, int t, int u)
/*  32:    */   {
/*  33: 45 */     this.objects = new ArrayList(l);
/*  34: 46 */     this.start = s;
/*  35:    */     
/*  36: 48 */     this.hasNext = hasNext;
/*  37: 49 */     this.totalNoPages = t;
/*  38: 50 */     this.numAllResults = u;
/*  39:    */   }
/*  40:    */   
/*  41:    */   public Page(List l, int u)
/*  42:    */   {
/*  43: 53 */     this.objects = new ArrayList(l);
/*  44: 54 */     this.numAllResults = u;
/*  45:    */   }
/*  46:    */   
/*  47:    */   public List getList()
/*  48:    */   {
/*  49: 58 */     return this.objects;
/*  50:    */   }
/*  51:    */   
/*  52:    */   public boolean isNextPageAvailable()
/*  53:    */   {
/*  54: 66 */     return this.hasNext;
/*  55:    */   }
/*  56:    */   
/*  57:    */   public boolean isPreviousPageAvailable()
/*  58:    */   {
/*  59: 72 */     return this.start > 0;
/*  60:    */   }
/*  61:    */   
/*  62:    */   public int getStartOfNextPage()
/*  63:    */   {
/*  64: 76 */     return this.start + this.objects.size();
/*  65:    */   }
/*  66:    */   
/*  67:    */   public int getStartOfPreviousPage()
/*  68:    */   {
/*  69: 80 */     int prevStart = 0;
/*  70: 81 */     if ((this.start - this.objects.size()) % 10 > 0) {
/*  71: 82 */       prevStart = (this.start - this.objects.size()) / 10 * 10;
/*  72:    */     } else {
/*  73: 85 */       prevStart = this.start - this.objects.size();
/*  74:    */     }
/*  75: 88 */     return Math.max(prevStart, 0);
/*  76:    */   }
/*  77:    */   
/*  78:    */   public int getStartOfPreviousPage(Integer num_records)
/*  79:    */   {
/*  80: 92 */     int prevStart = 0;
/*  81: 93 */     if ((this.start - this.objects.size()) % num_records.intValue() > 0) {
/*  82: 94 */       prevStart = (this.start - this.objects.size()) / num_records.intValue() * num_records.intValue();
/*  83:    */     } else {
/*  84: 98 */       prevStart = this.start - this.objects.size();
/*  85:    */     }
/*  86:101 */     return Math.max(prevStart, 0);
/*  87:    */   }
/*  88:    */   
/*  89:    */   public int getSize()
/*  90:    */   {
/*  91:105 */     return this.objects.size();
/*  92:    */   }
/*  93:    */   
/*  94:    */   public int getTotalNoPages()
/*  95:    */   {
/*  96:109 */     return this.totalNoPages;
/*  97:    */   }
/*  98:    */   
/*  99:    */   public int getnumAllResults()
/* 100:    */   {
/* 101:113 */     return this.numAllResults;
/* 102:    */   }
/* 103:    */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.Page
 * JD-Core Version:    0.7.0.1
 */