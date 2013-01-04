package com.rancard.util;

import java.io.*;
import java.util.*;

/**
 * Represents a page of results (for page-by-page iteration).
 */
public class Page
    implements Serializable {

  // XXX
  /*
    public static final Page EMPTY_PAGE =
        new Page(Collections.EMPTY_LIST, 0, 0);
   */
  public static final Page EMPTY_PAGE =
      new Page(Collections.EMPTY_LIST, 0, false, 0);

  List objects;
  int start;
  int totalNoPages;
  int numAllResults; // not current limited resultset but total original number from profilesDB count query
  // XXX
  // int containingListSize;
  boolean hasNext;

  // XXX
  /*
    public Page(List l, int s, int cls) {
   */
  public Page(List l, int s, boolean hasNext, int t) {
    objects = new ArrayList(l);
    start = s;

    //System.out.println("Page constructor called, start = " + start);

    // XXX
    //containingListSize = cls;
    this.hasNext = hasNext;
    this.totalNoPages = t;
  }

  public Page(List l, int s, boolean hasNext, int t, int u) {
    objects = new ArrayList(l);
    start = s;

    this.hasNext = hasNext;
    this.totalNoPages = t;
    this.numAllResults = u;
  }

  public List getList() {
    return objects;
  }

  public boolean isNextPageAvailable() {
    // XXX
    /*
      return (start + objects.size()) < containingListSize;
     */
    return hasNext;
  }

  public boolean isPreviousPageAvailable() {
    // debugging
    //System.out.println("this.start in Page.isPrevioiusPage... = " + this.start);
    return start > 0;
  }

  public int getStartOfNextPage() {
    return start + objects.size();
  }

  public int getStartOfPreviousPage() {
    int prevStart = 0;
    if ( ( (start - objects.size()) % 10) > 0) {
      prevStart = ( (start - objects.size()) / 10) * 10;
    }
    else {
      prevStart = start - objects.size();
    }

    return Math.max(prevStart, 0);
  }

  public int getStartOfPreviousPage(Integer num_records) {
    int prevStart = 0;
    if ( ( (start - objects.size()) % num_records.intValue()) > 0) {
      prevStart = ( (start - objects.size()) / num_records.intValue()) *
          num_records.intValue();
    }
    else {
      prevStart = start - objects.size();
    }

    return Math.max(prevStart, 0);
  }

  public int getSize() {
    return objects.size();
  }

  public int getTotalNoPages() {
    return totalNoPages;
  }

  public int getnumAllResults() {
    return this.numAllResults;
  }

}
