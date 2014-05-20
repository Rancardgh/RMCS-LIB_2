/*   1:    */ package com.rancard.util;
/*   2:    */ 
/*   3:    */ import java.util.HashMap;
/*   4:    */ 
/*   5:    */ public class PathManager
/*   6:    */ {
/*   7: 13 */   private static HashMap pathMap = new HashMap(5, 0.75F);
/*   8:    */   private String article_id;
/*   9:    */   private String Parent_article_id;
/*  10:    */   private String parent_title;
/*  11:    */   private String title;
/*  12:    */   
/*  13:    */   public HashMap getPathMap()
/*  14:    */   {
/*  15:250 */     return pathMap;
/*  16:    */   }
/*  17:    */   
/*  18:    */   public void setPathMap(HashMap pathMap)
/*  19:    */   {
/*  20:254 */     pathMap = pathMap;
/*  21:    */   }
/*  22:    */   
/*  23:    */   public String getParent_article_id()
/*  24:    */   {
/*  25:258 */     return this.Parent_article_id;
/*  26:    */   }
/*  27:    */   
/*  28:    */   public void setParent_article_id(String Parent_article_id)
/*  29:    */   {
/*  30:262 */     this.Parent_article_id = Parent_article_id;
/*  31:    */   }
/*  32:    */   
/*  33:    */   public void setParent_title(String parent_title)
/*  34:    */   {
/*  35:266 */     this.parent_title = parent_title;
/*  36:    */   }
/*  37:    */   
/*  38:    */   public String getParent_title()
/*  39:    */   {
/*  40:270 */     return this.parent_title;
/*  41:    */   }
/*  42:    */   
/*  43:    */   public String getTitle()
/*  44:    */   {
/*  45:274 */     return this.title;
/*  46:    */   }
/*  47:    */   
/*  48:    */   public void setTitle(String title)
/*  49:    */   {
/*  50:278 */     this.title = title;
/*  51:    */   }
/*  52:    */   
/*  53:    */   public String getArticle_id()
/*  54:    */   {
/*  55:282 */     return this.article_id;
/*  56:    */   }
/*  57:    */   
/*  58:    */   public void setArticle_id(String article_id)
/*  59:    */   {
/*  60:286 */     this.article_id = article_id;
/*  61:    */   }
/*  62:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib (1).jar
 * Qualified Name:     com.rancard.util.PathManager
 * JD-Core Version:    0.7.0.1
 */