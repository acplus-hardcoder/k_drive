package k_drive.service;

public class FolderBean {
	String name = null;	
	String path = null;
	int parent_num = 0;
	int this_num = 0;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public int getParent_num() {
		return parent_num;
	}
	public void setParent_num(int parent_num) {
		this.parent_num = parent_num;
	}
	public int getThis_num() {
		return this_num;
	}
	public void setThis_num(int this_num) {
		this.this_num = this_num;
	}	
}
