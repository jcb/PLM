package jlm.ui;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;

import jlm.lesson.ISourceFileListener;
import jlm.lesson.SourceFile;


/*
 * Responsibility: synchronize content between Document instance
 * used by a JEditorPane and the body field of a SourceFile instance.
 * 
 */

public class SourceFileDocumentSynchronizer implements DocumentListener, ISourceFileListener {

	private Document document;

	private SourceFile sourceFile;

	private EditorKit editorKit;

	private boolean propagationInProgress = false;

	public SourceFileDocumentSynchronizer(EditorKit kit) {
		this.editorKit = kit;
	}

	public void clear() {
		this.document = null;
		this.sourceFile = null;
	}

	public void setDocument(Document doc) {
		this.document = doc;
	}

	public void setSourceFile(SourceFile srcFile) {
		this.sourceFile = srcFile;
	}

	private void copyDocumentContentToSourceFileBody() {
		if (this.propagationInProgress)
			return ;

		this.propagationInProgress = true;
		try {
			this.sourceFile.setBody(this.document.getText(0, this.document.getLength()));
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		} finally {
			this.propagationInProgress = false;
		}
	}

	private void copySourceFileBodyToDocumentContent() {
		if (this.propagationInProgress)
			return ;
		
		this.propagationInProgress = true;
		try {
			this.document.remove(0, this.document.getLength());
			String body = this.sourceFile.getBody();
			if (body == null || body.equals("")) {
				return;
			}
			Reader reader = new StringReader(body);
			this.editorKit.read(reader, this.document, 0);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BadLocationException e) {
			e.printStackTrace();
		} finally {
			this.propagationInProgress = false;
		}
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		copyDocumentContentToSourceFileBody();
	}

	@Override
	public void insertUpdate(DocumentEvent e) {
		copyDocumentContentToSourceFileBody();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		copyDocumentContentToSourceFileBody();
	}

	@Override
	public void sourceFileContentHasChanged() {
		copySourceFileBodyToDocumentContent();
	}

}