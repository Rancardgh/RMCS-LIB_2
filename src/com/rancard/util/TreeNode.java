package com.rancard.util;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class TreeNode {

  public TreeNode() {
  }

  private String id;
  private String name;
  private String parentId;
  private boolean hasContents;
  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setParentId(String parent) {
    this.parentId = parent;
  }

  public String getParentId() {
    return parentId;
  }

  public void setHasContents(boolean hasContents) {
    this.hasContents = hasContents;
  }

  public boolean isHasContents() {
    return hasContents;
  }
}