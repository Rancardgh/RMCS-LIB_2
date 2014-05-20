/*  1:   */ package com.rancard.mobility.infoserver;
/*  2:   */ 
/*  3:   */ import java.util.Vector;
/*  4:   */ 
/*  5:   */ public class system_sms_queue_groupBean
/*  6:   */ {
/*  7:   */   private Integer category_id;
/*  8:   */   private String category_name;
/*  9:   */   
/* 10:   */   public void setcategory_id(Integer category_id)
/* 11:   */   {
/* 12:14 */     this.category_id = category_id;
/* 13:   */   }
/* 14:   */   
/* 15:   */   public Integer getcategory_id()
/* 16:   */   {
/* 17:18 */     return this.category_id;
/* 18:   */   }
/* 19:   */   
/* 20:   */   public void setcategory_name(String category_name)
/* 21:   */   {
/* 22:21 */     this.category_name = category_name;
/* 23:   */   }
/* 24:   */   
/* 25:   */   public String getcategory_name()
/* 26:   */   {
/* 27:25 */     return this.category_name;
/* 28:   */   }
/* 29:   */   
/* 30:   */   public void createsystem_sms_queue_group()
/* 31:   */     throws Exception
/* 32:   */   {
/* 33:28 */     system_sms_queue_groupDB system_sms_queue_group = new system_sms_queue_groupDB();
/* 34:   */     
/* 35:30 */     system_sms_queue_group.createsystem_sms_queue_group(this.category_id, this.category_name);
/* 36:   */   }
/* 37:   */   
/* 38:   */   public void updatesystem_sms_queue_group()
/* 39:   */     throws Exception
/* 40:   */   {
/* 41:33 */     system_sms_queue_groupDB system_sms_queue_group = new system_sms_queue_groupDB();
/* 42:   */     
/* 43:35 */     system_sms_queue_group.updatesystem_sms_queue_group(this.category_id, this.category_name);
/* 44:   */   }
/* 45:   */   
/* 46:   */   public void deletesystem_sms_queue_group()
/* 47:   */     throws Exception
/* 48:   */   {
/* 49:38 */     system_sms_queue_groupDB system_sms_queue_group = new system_sms_queue_groupDB();
/* 50:   */     
/* 51:40 */     system_sms_queue_group.deletesystem_sms_queue_group(this.category_id);
/* 52:   */   }
/* 53:   */   
/* 54:   */   public void viewsystem_sms_queue_group()
/* 55:   */     throws Exception
/* 56:   */   {
/* 57:43 */     system_sms_queue_groupBean bean = new system_sms_queue_groupBean();
/* 58:44 */     system_sms_queue_groupDB system_sms_queue_group = new system_sms_queue_groupDB();
/* 59:   */     
/* 60:46 */     bean = system_sms_queue_group.viewsystem_sms_queue_group(this.category_id);
/* 61:   */     
/* 62:48 */     this.category_id = bean.getcategory_id();
/* 63:49 */     this.category_name = bean.getcategory_name();
/* 64:   */   }
/* 65:   */   
/* 66:   */   public Vector Query1system_sms_queue_group()
/* 67:   */     throws Exception
/* 68:   */   {
/* 69:57 */     system_sms_queue_groupDB system_sms_queue_group = new system_sms_queue_groupDB();
/* 70:   */     
/* 71:59 */     Vector beans = new Vector();
/* 72:60 */     beans = system_sms_queue_group.Query1system_sms_queue_group();
/* 73:61 */     return beans;
/* 74:   */   }
/* 75:   */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.mobility.infoserver.system_sms_queue_groupBean
 * JD-Core Version:    0.7.0.1
 */