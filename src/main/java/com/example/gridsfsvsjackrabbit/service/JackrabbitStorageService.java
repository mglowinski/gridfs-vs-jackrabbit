package com.example.gridsfsvsjackrabbit.service;

import com.example.gridsfsvsjackrabbit.model.RetrievedFile;
import com.example.gridsfsvsjackrabbit.model.SavedFile;
import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import javax.jcr.Binary;
import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Value;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

@Service
@RequiredArgsConstructor
public class JackrabbitStorageService implements StorageService {

	private static final String JCR_CONTENT = "jcr:content";
	private static final String JCR_DATA = "jcr:data";
	private static final String JCR_MIME_TYPE = "jcr:mimeType";
	private static final String JCR_LAST_MODIFIED = "jcr:lastModified";
	private static final String NT_RESOURCE = "nt:resource";
	private static final String NT_FILE = "nt:file";
	private static final String PATH_TO_FILES = "/files/";

	private final Node fileNode;

	@Override
	public SavedFile saveFile(String filename, String contentType, byte[] file) throws RepositoryException {
		String uniqueFilename = String.valueOf(System.currentTimeMillis());
		Node docNode = fileNode.addNode(uniqueFilename, NT_FILE);
		Node contentNode = docNode.addNode(JCR_CONTENT, NT_RESOURCE);
		Binary binary =
				fileNode.getSession().getValueFactory().createBinary(
						new ByteArrayInputStream(file));
		contentNode.setProperty(JCR_DATA, binary);
		contentNode.setProperty(JCR_MIME_TYPE, contentType);
		contentNode.setProperty(JCR_LAST_MODIFIED, Calendar.getInstance());
		fileNode.getSession().save();
		return new SavedFile(uniqueFilename);
	}

	@Override
	public RetrievedFile getFile(String id) throws RepositoryException, IOException {
		if (!fileNode.getSession().itemExists(PATH_TO_FILES + id)) {
			throw new FileNotFoundException(id);
		}
		Node docNode = fileNode.getSession().getNode(PATH_TO_FILES + id);
		Node contentNode = docNode.getNode(JCR_CONTENT);
		Value fileValue = contentNode.getProperty(JCR_DATA).getValue();
		Value contentTypeValue = contentNode.getProperty(JCR_MIME_TYPE).getValue();
		InputStream stream = fileValue.getBinary().getStream();
		return new RetrievedFile(IOUtils.toByteArray(stream), contentTypeValue.getString());
	}

}
