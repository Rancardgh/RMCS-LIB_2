/*   1:    */ package com.rancard.util.payment;
/*   2:    */ 
/*   3:    */ import java.beans.BeanDescriptor;
/*   4:    */ import java.beans.EventSetDescriptor;
/*   5:    */ import java.beans.MethodDescriptor;
/*   6:    */ import java.beans.PropertyDescriptor;
/*   7:    */ import java.beans.SimpleBeanInfo;
/*   8:    */ 
/*   9:    */ public class ePinBeanInfo
/*  10:    */   extends SimpleBeanInfo
/*  11:    */ {
/*  12: 17 */   private static BeanDescriptor beanDescriptor = null;
/*  13:    */   
/*  14:    */   private static BeanDescriptor getBdescriptor()
/*  15:    */   {
/*  16: 23 */     return beanDescriptor;
/*  17:    */   }
/*  18:    */   
/*  19: 27 */   private static PropertyDescriptor[] properties = null;
/*  20:    */   
/*  21:    */   private static PropertyDescriptor[] getPdescriptor()
/*  22:    */   {
/*  23: 33 */     return properties;
/*  24:    */   }
/*  25:    */   
/*  26: 36 */   private static EventSetDescriptor[] eventSets = null;
/*  27:    */   
/*  28:    */   private static EventSetDescriptor[] getEdescriptor()
/*  29:    */   {
/*  30: 42 */     return eventSets;
/*  31:    */   }
/*  32:    */   
/*  33: 45 */   private static MethodDescriptor[] methods = null;
/*  34:    */   
/*  35:    */   private static MethodDescriptor[] getMdescriptor()
/*  36:    */   {
/*  37: 51 */     return methods;
/*  38:    */   }
/*  39:    */   
/*  40: 54 */   private static int defaultPropertyIndex = -1;
/*  41: 55 */   private static int defaultEventIndex = -1;
/*  42:    */   
/*  43:    */   public BeanDescriptor getBeanDescriptor()
/*  44:    */   {
/*  45: 72 */     return getBdescriptor();
/*  46:    */   }
/*  47:    */   
/*  48:    */   public PropertyDescriptor[] getPropertyDescriptors()
/*  49:    */   {
/*  50: 88 */     return getPdescriptor();
/*  51:    */   }
/*  52:    */   
/*  53:    */   public EventSetDescriptor[] getEventSetDescriptors()
/*  54:    */   {
/*  55: 99 */     return getEdescriptor();
/*  56:    */   }
/*  57:    */   
/*  58:    */   public MethodDescriptor[] getMethodDescriptors()
/*  59:    */   {
/*  60:110 */     return getMdescriptor();
/*  61:    */   }
/*  62:    */   
/*  63:    */   public int getDefaultPropertyIndex()
/*  64:    */   {
/*  65:122 */     return defaultPropertyIndex;
/*  66:    */   }
/*  67:    */   
/*  68:    */   public int getDefaultEventIndex()
/*  69:    */   {
/*  70:133 */     return defaultEventIndex;
/*  71:    */   }
/*  72:    */ }


/* Location:           C:\Users\Mustee\Downloads\rmcs_211_lib.jar
 * Qualified Name:     com.rancard.util.payment.ePinBeanInfo
 * JD-Core Version:    0.7.0.1
 */