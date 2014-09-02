/*  1:   */ package com.rancard.util;
/*  2:   */ 
/*  3:   */ public class TreeNode
/*  4:   */ {
/*  5:   */   private String id;
/*  6:   */   private String name;
/*  7:   */   private String parentId;
/*  8:   */   private boolean hasContents;
/*  9:   */   
/* 10:   */   public String getId()
/* 11:   */   {
/* 12:22 */     return this.id;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public void setId(String id)
/* 16:   */   {
/* 17:26 */     this.id = id;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void setName(String name)
/* 21:   */   {
/* 22:30 */     this.name = name;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String getName()
/* 26:   */   {
/* 27:34 */     return this.name;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void setParentId(String parent)
/* 31:   */   {
/* 32:38 */     this.parentId = parent;
/* 33:   */   }
/* 34:   */   
/* 35:   */   public String getParentId()
/* 36:   */   {
/* 37:42 */     return this.parentId;
/* 38:   */   }
/* 39:   */   
/* 40:   */   public void setHasContents(boolean hasContents)
/* 41:   */   {
/* 42:46 */     this.hasContents = hasContents;
/* 43:   */   }
/* 44:   */   
/* 45:   */   public boolean isHasContents()
/* 46:   */   {
/* 47:50 */     return this.hasContents;
/* 48:   */   }
/* 49:   */ }


/* Location:           C:\Users\Ahmed\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.TreeNode
 * JD-Core Version:    0.7.0.1
 */