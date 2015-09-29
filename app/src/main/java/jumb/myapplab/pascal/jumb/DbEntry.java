package jumb.myapplab.pascal.jumb;

public class DbEntry {

	  private long id;
	  private String title;
	  private String content;
	  private String date;

	  public long getId() {
	    return id;
	  }

	  public void setId(long id) {
	    this.id = id;
	  }

	  public String getTitle() {
	    return title;
	  }

	  public void setTitle(String title) {
	    this.title = title;
	  }

	  public String getContent() {
		    return content;
		  }

	  public void setContent(String content) {
		this.content = content;
	  }
		  
	  public String getDate() {
		  	return date;
	  }

	  public void setDate(String date) {
		 this.date = date;
	  }
	  
	  // Will be used by the ArrayAdapter in the ListView
	  //@Override
	  //public String toString() {
	  //  return comment;
	  //}
	} 