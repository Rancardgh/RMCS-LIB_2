package com.rancard.util;

/**
 * <p>Title:Path Manager </p>
 * <p>Description:This class provides a generic framework for resolving the full path to a particular article in a tree.it takes in the article Id and returns an array of Article VO's </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class PathManager {
  private static java.util.HashMap pathMap = new java.util.HashMap(5, 0.75f);
  private java.lang.String article_id;
  private java.lang.String Parent_article_id;
  private String parent_title;
  private String title;
  // variable to hold all info required to display a path to an article


  public PathManager() {
    //this.setPathMap();
  }

  /*public void updateMap(){
          //create

          this.setPathMap(this.updateMap(this.getPathMap(),this.getArticle_id(),this.getTitle(),this.getParent_article_id(),this.getParent_title()));
         }*/



  /* public java.util.HashMap updateMap(java.util.HashMap Mem_Path,java.lang.String article_id,java.lang.String parent_article_id){
   String current_Art_id = article_id;
   String old_Art_id = null;
   String Curr_Parent_Art_id = parent_article_id;
   String Parent_Art_id= parent_article_id;


   //locate node in hashmap
   //get hashmap from session
   java.util.HashMap path = Mem_Path;

   //String path = "GE";

    if(path.isEmpty() || !path.containsKey("BIZ000")){// dosent exist -
     path.put("BIZ000","BIZ000");
     // create two parent child objects
     com.rancard.cms.ArticleVO defaultArt = new com.rancard.cms.ArticleVO();
     defaultArt.setTitle("GE Business");
     defaultArt.setArticle_id("BIZ000");
     //
     path.put(defaultArt,defaultArt);
    return path;
    }



   if(path.containsKey(Parent_Art_id)){

   old_Art_id= (String)path.get(Parent_Art_id);

        // check if to see node has a child
     if(old_Art_id==Parent_Art_id){
     // then it has no children
     // if yes replace the old child
   path.put(Parent_Art_id,current_Art_id);
      //set the new node
   path.put(current_Art_id,current_Art_id);
     }else{
        // node exists but has children

        //find all children of old child and remove them.
     String child_article_id ="";
       while(path.values().iterator().hasNext()){
       if(!path.containsKey(old_Art_id)){
       break;
       }
       child_article_id = (String)path.get(old_Art_id);
        //old_Art_id  //old_child of current parent
        path.remove(old_Art_id);
        old_Art_id= child_article_id;
       //path.values().iterator().next();
        }

     path.put(Parent_Art_id,current_Art_id);
        //set the new node
     path.put(current_Art_id,current_Art_id);
    }
   }
        return path;
   }
       public java.util.HashMap updateMap(java.util.HashMap Mem_Path,java.lang.String articleid,java.lang.String title,java.lang.String parent_articleid,java.lang.String parent_title){

        com.rancard.cms.ArticleVO defaultArt = new com.rancard.cms.ArticleVO();
        defaultArt.setTitle("GE");
        defaultArt.setArticle_id("BIZ000");

   com.rancard.cms.ArticleVO current_Art  = new com.rancard.cms.ArticleVO();
   com.rancard.cms.ArticleVO Curr_Parent_Biz  = new com.rancard.cms.ArticleVO();
   com.rancard.cms.ArticleVO old_Biz = new com.rancard.cms.ArticleVO();
   com.rancard.cms.ArticleVO Parent_Biz= new com.rancard.cms.ArticleVO();
   //
    current_Art.setArticle_id(articleid);
    current_Art.setTitle(title);

    Parent_Biz.setArticle_id(parent_articleid);
    Parent_Biz.setTitle(parent_title);

   //Parent_Biz = Curr_Parent_Biz;
   //locate node in hashmap
   //get hashmap from session
   java.util.HashMap path = Mem_Path;

   //String path = "GE";

    if(path.isEmpty() || !path.containsKey(defaultArt)){// dosent exist -
    // create two parent child objects
     path.put(defaultArt,defaultArt);
    return path;
    }



   if(path.containsKey(Parent_Biz)){

   old_Biz= (com.rancard.cms.ArticleVO)path.get(Parent_Biz);

        // check if to see node has a child
     if(old_Biz.equals(Parent_Biz)){
     // then it has no children
     // if yes replace the old child
   path.put(Parent_Biz,current_Art);
      //set the new node
   path.put(current_Art,current_Art);
     }else{
        // node exists but has children

        //find all children of old child and remove them.
     com.rancard.cms.ArticleVO child_biz  =null;
       while(path.values().iterator().hasNext()){
       if(!path.containsKey(old_Biz)){
       break;
       }
       child_biz = (com.rancard.cms.ArticleVO)path.get(old_Biz);
        //old_Art_id  //old_child of current parent
        path.remove(old_Biz);
        old_Biz= child_biz;
       //path.values().iterator().next();
        }

     path.put(Parent_Biz,current_Art);
        //set the new node
     path.put(current_Art,current_Art);
    }
   }
        return path;
   }

      public static ArticleVO getParent(ArticleVO biz)
      {
   Iterator i = pathMap.keySet().iterator();
   ArticleVO b;

   while(i.hasNext())
   {
    b = (ArticleVO)i.next();
    if(biz.equals(pathMap.get(b)))
      return b;
   }
   return null;
      }

      /**
    *
    * @param bizID
    * @param bizName
    * @return
    */
   /* public static String[] getPath(String bizID, String bizName)
    {
       ArticleVO biz = new ArticleVO();
       biz.setTitle(bizName);
       biz.setArticle_id(bizID);

       LinkedList l = new LinkedList();

       do
       {
     l.add(biz);
     biz = getParent(biz);
       }while(biz != null);

       return (String[])l.toArray();
    }

    public String[] getPath(java.util.HashMap mem_path){
    String articleid = "BIZ000";
    String [] path =  new String [mem_path.size()];

      for (int i=0;i<mem_path.size();i++){
        path[i] = articleid;
       articleid = (String)mem_path.get(articleid);

       }
    return path;
    }
    /*
        public com.rancard.cms.ArticleVO[]  getBusinessPath(java.util.HashMap mem_path,String articleid){
        com.rancard.cms.ArticleVO BP = new com.rancard.cms.ArticleVO();
        BP.setTitle("GE");
        BP.setArticle_id("BIZ000");

        com.rancard.cms.ArticleVO[] path =  new com.rancard.cms.ArticleVO [mem_path.size()];
        com.rancard.cms.ArticleVO ROOTNODE = new com.rancard.cms.ArticleVO();

        ROOTNODE.setTitle("GE");
        ROOTNODE.setArticle_id("BIZ000");
        ROOTNODE.sethas_children(new Boolean(true));

        java.util.LinkedList pathToBiz = new java.util.LinkedList();
        com.rancard.cms.ArticleVO artPath = new com.rancard.cms.ArticleVO();
        // check if node exist
         if(!mem_path.isEmpty()&&mem_path.containsKey(articleid)){
       // recursively find parent
       artPath = (com.rancard.cms.ArticleVO)mem_path.get(articleid);
       while(artPath!=null){
       pathToBiz.add(artPath);
        // till no parent
       if(artPath.getParent_id()!=null){
      artPath= (com.rancard.cms.ArticleVO)mem_path.get(artPath.getParent_id());
       }else{
        // add ge
      artPath= ROOTNODE;
      pathToBiz.add(artPath);
      break;
      }
      }
          }else{
       artPath= ROOTNODE;
       pathToBiz.add(artPath);
       }
        java.util.Collections.reverse(pathToBiz);
     com.rancard.cms.ArticleVO[] a = new com.rancard.cms.ArticleVO[pathToBiz.size()];
        return (com.rancard.cms.ArticleVO[])pathToBiz.toArray(a);
        }
     */

    public java.util.HashMap getPathMap() {
      return pathMap;
    }

  public void setPathMap(java.util.HashMap pathMap) {
    this.pathMap = pathMap;
  }

  public String getParent_article_id() {
    return Parent_article_id;
  }

  public void setParent_article_id(String Parent_article_id) {
    this.Parent_article_id = Parent_article_id;
  }

  public void setParent_title(String parent_title) {
    this.parent_title = parent_title;
  }

  public String getParent_title() {
    return parent_title;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getArticle_id() {
    return article_id;
  }

  public void setArticle_id(String article_id) {
    this.article_id = article_id;
  }
}
