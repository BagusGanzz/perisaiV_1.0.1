package com.studioninja.locker.ui;

public class Sdcard_Itembean {

	public String Foldername, Filepath, Directoryname;
	boolean FolderSelection;

	public Sdcard_Itembean(String foldername, String filepath, boolean folderselection, String directoryname) {

		this.Foldername = foldername;
		this.Filepath = filepath;
		this.FolderSelection = folderselection;
		this.Directoryname = directoryname;
	}

	public String getDirectoryname() {
		return Directoryname;
	}

	public void setDirectoryname(String directoryname) {
		Directoryname = directoryname;
	}

	public boolean isFolderSelection() {
		return FolderSelection;
	}

	public void setFolderSelection(boolean folderSelection) {
		FolderSelection = folderSelection;
	}

	public String getFoldername() {
		return Foldername;
	}

	public void setFoldername(String foldername) {
		Foldername = foldername;
	}

	public String getFilepath() {
		return Filepath;
	}

	public void setFilepath(String filepath) {
		Filepath = filepath;
	}
}