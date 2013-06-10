package com.rancard.security;

/**
 * <p>Title: National Identification authority website and cms</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: Rancard Soltuions Ltd</p>
 * @author unascribed
 * @version 1.0
 */

public class roles {

  public roles() {
  }

  private String role_id;
  private String role_name;
  private boolean result;
  rolesDB rolDB = new rolesDB();
  private String createUser;
  private String deleteUser;
  private String manageUser;
  private String manageArticles;
  private String createFolder;
  private String deleteFolder;
  private String manageTemplate;
  private String publishTemplate;
  private String menu1;
  private String menu2;
  private String menu3;
  private String menu4;
  private String menu5;
  private String menu6;

  public String getRole_id() {
    return role_id;
  }

  public void setRole_id(String role_id) {
    this.role_id = role_id;
  }

  public void setRole_name(String role_name) {
    this.role_name = role_name;
  }

  public String getRole_name() {
    return role_name;
  }

  public boolean canCreateUser() {
    if ("1".equals(createUser)) {
      return true;
    }
    else {
      return false;

    }
  }

  /**
    public boolean canDeleteUser()
    {
    }
   **/
  public boolean canManageUsers() {
    if ("1".equals(manageUser)) {
      return true;
    }
    else {
      return false;

    }
  }

  /**
   public boolean canManageArticles()
     {
     }
   **/
  public boolean canDeleteFolder() {
    if ("1".equals(deleteFolder)) {
      return true;
    }
    else {
      return false;

    }
  }

  public boolean canCreateFolder() {
    if ("1".equals(createFolder)) {
      return true;
    }
    else {
      return false;

    }
  }


   public boolean canManageTemplate()
     {
       if ("1".equals(manageTemplate)) {
         return true;
       }
       else {
         return false;

       }

     }
   /**
   public boolean canPublishTemplate()
     {
     }**/

  public boolean canViewMenu1() {
    if ("1".equals(menu1)) {
      return true;
    }
    else {
      return false;

    }
  }

  public boolean canViewMenu2() {
    if ("1".equals(menu2)) {
      return true;
    }
    else {
      return false;

    }
  }

  public boolean canViewMenu3() {
    if ("1".equals(menu3)) {
      return true;
    }
    else {
      return false;

    }
  }

  public boolean canViewMenu4() {
    if ("1".equals(menu4)) {
      return true;
    }
    else {
      return false;

    }
  }

  public boolean canViewMenu5() {

    if ("1".equals(menu5)) {
      return true;
    }
    else {
      return false;

    }

  }

  public boolean canViewMenu6() {

    if ("1".equals(menu6)) {
      return true;
    }
    else {
      return false;

    }
  }

  public void viewRoles() {
    roles roleType = new roles();

    try {
      roleType = rolDB.viewRoles(role_id);
      role_id = roleType.getRole_id();
      role_name = roleType.getRole_name();
      createFolder = roleType.getCreateFolder();
      deleteFolder = roleType.getDeleteFolder();
      manageUser = roleType.getManageUser();
      manageTemplate=roleType.getManageTemplate();
      menu1 = roleType.getMenu1();
      menu2 = roleType.getMenu2();
      menu3 = roleType.getMenu3();
      menu4 = roleType.getMenu4();
      menu5 = roleType.getMenu5();
      menu6 = roleType.getMenu6();

    }
    catch (Exception e) {
    }
  }

  public java.util.HashMap viewAllRoles() throws Exception {
    /**
     * @todo
     add the neccessary parameters (if any)
     **/
    rolesDB roleInfo = new rolesDB();

    return roleInfo.getAllRoles();
  }

  public String getroleName(String id) {
    rolesDB roleInfo2 = new rolesDB();
    String rolename = "";
    try {
      rolename = roleInfo2.getroleName(id);
    }
    catch (Exception e) {
    }

    return rolename;
  }

  public void setCreateUser(String createUser) {
    this.createUser = createUser;
  }

  public String getCreateUser() {
    return createUser;
  }

  public void setDeleteUser(String deleteUser) {
    this.deleteUser = deleteUser;
  }

  public String getDeleteUser() {
    return deleteUser;
  }

  public void setManageUser(String manageUser) {
    this.manageUser = manageUser;
  }

  public String getManageUser() {
    return manageUser;
  }

  public void setManageArticles(String manageArticles) {
    this.manageArticles = manageArticles;
  }

  public String getManageArticles() {
    return manageArticles;
  }

  public void setCreateFolder(String createFolder) {
    this.createFolder = createFolder;
  }

  public String getCreateFolder() {
    return createFolder;
  }

  public void setDeleteFolder(String deleteFolder) {
    this.deleteFolder = deleteFolder;
  }

  public String getDeleteFolder() {
    return deleteFolder;
  }

  public void setManageTemplate(String manageTemplate) {
    this.manageTemplate = manageTemplate;
  }

  public String getManageTemplate() {
    return manageTemplate;
  }

  public void setPublishTemplate(String publishTemplate) {
    this.publishTemplate = publishTemplate;
  }

  public String getPublishTemplate() {
    return publishTemplate;
  }

  public void setMenu1(String menu1) {
    this.menu1 = menu1;
  }

  public String getMenu1() {
    return menu1;
  }

  public void setMenu2(String menu2) {
    this.menu2 = menu2;
  }

  public String getMenu2() {
    return menu2;
  }

  public void setMenu3(String menu3) {
    this.menu3 = menu3;
  }

  public String getMenu3() {
    return menu3;
  }

  public void setMenu4(String menu4) {
    this.menu4 = menu4;
  }

  public String getMenu4() {
    return menu4;
  }

  public void setMenu5(String menu5) {
    this.menu5 = menu5;
  }

  public String getMenu5() {
    return menu5;
  }

  public void setMenu6(String menu6) {
    this.menu6 = menu6;
  }

  public String getMenu6() {
    return menu6;
  }
}
