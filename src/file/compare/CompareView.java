package file.compare;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.BufferedContent;
import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.EditorInputTransfer.EditorInputData;
import org.eclipse.ui.part.ViewPart;

public class CompareView extends ViewPart {

	public static final String ID = "file.compare.CompareView";

	/**
	 * The text control that's displaying the content of the email message.
	 */
	private static CompareView compareView;

	public CompareView() {
	}

	public void createPartControl(Composite parent) {

		CompareConfiguration config = new CompareConfiguration();
		config.setProperty(CompareConfiguration.SHOW_PSEUDO_CONFLICTS,
				Boolean.FALSE);
		// left
		config.setLeftEditable(true);
		config.setLeftLabel("左");
		// right
		config.setRightEditable(true);
		config.setRightLabel("右");

		CompareEditorInput editorInput = new CompareEditorInput(config) {
			CompareItem left = new CompareItem("left.txt");
			CompareItem right = new CompareItem("right.txt");

			@Override
			protected Object prepareInput(IProgressMonitor monitor)
					throws InvocationTargetException, InterruptedException {
				return new DiffNode(null, Differencer.CONFLICTING, null, left,
						right);
			}

			public void saveChanges(IProgressMonitor pm) throws CoreException {
				super.saveChanges(pm);
				left.writeFile();
				right.writeFile();
			}

		};
		editorInput.setTitle("文件比较");
		CompareUI.openCompareEditor(editorInput);
	}

	@Override
	public void setFocus() {

	}

}
