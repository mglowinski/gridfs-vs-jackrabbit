package com.example.gridsfsvsjackrabbit.service;

import com.example.gridsfsvsjackrabbit.model.RetrievedFile;
import com.example.gridsfsvsjackrabbit.model.SavedFile;

import javax.jcr.RepositoryException;
import java.io.IOException;

public interface StorageService {

	SavedFile saveFile(String filename, String contentType, byte[] file) throws RepositoryException;

	RetrievedFile getFile(String id) throws RepositoryException, IOException;
}
