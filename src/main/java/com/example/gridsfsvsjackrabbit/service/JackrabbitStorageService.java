package com.example.gridsfsvsjackrabbit.service;

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

	private final Node fileNode;

	@Override
	public SavedFile saveFile(String filename, String contentType, byte[] file) throws RepositoryException {
		Node docNode = fileNode.addNode(filename, NT_FILE);
		Node contentNode = docNode.addNode(JCR_CONTENT, NT_RESOURCE);
		Binary binary =
				fileNode.getSession().getValueFactory().createBinary(
						new ByteArrayInputStream(file));
		contentNode.setProperty(JCR_DATA, binary);
		contentNode.setProperty(JCR_MIME_TYPE, contentType);
		contentNode.setProperty(JCR_LAST_MODIFIED, Calendar.getInstance());
		fileNode.getSession().save();
		return new SavedFile(docNode.getPath());
	}

	@Override
	public byte[] getFile(String id) throws RepositoryException, IOException {
		if (!fileNode.getSession().itemExists(id)) {
			throw new FileNotFoundException(id);
		}
		Node docNode = fileNode.getSession().getNode(id);
		Node contentNode = docNode.getNode(JCR_CONTENT);
		Value value = contentNode.getProperty(JCR_DATA).getValue();
		InputStream stream = value.getBinary().getStream();
		return IOUtils.toByteArray(stream);
	}

}
