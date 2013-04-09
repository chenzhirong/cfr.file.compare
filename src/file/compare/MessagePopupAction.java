package file.compare;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.compare.BufferedContent;
import org.eclipse.compare.CompareConfiguration;
import org.eclipse.compare.CompareEditorInput;
import org.eclipse.compare.CompareUI;
import org.eclipse.compare.IEditableContent;
import org.eclipse.compare.IModificationDate;
import org.eclipse.compare.ITypedElement;
import org.eclipse.compare.structuremergeviewer.DiffNode;
import org.eclipse.compare.structuremergeviewer.Differencer;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.IWorkbenchWindow;

public class MessagePopupAction extends Action {

    private final IWorkbenchWindow window;

    MessagePopupAction(String text, IWorkbenchWindow window) {
        super(text);
        this.window = window;
        // The id is used to refer to the action in a menu or toolbar 
        setId(ICommandIds.CMD_OPEN_MESSAGE);
        // Associate the action with a pre-defined command, to allow key bindings.
        setActionDefinitionId(ICommandIds.CMD_OPEN_MESSAGE);
        //setImageDescriptor(cn.iwoo.rcp.Activator.getImageDescriptor("/icons/sample3.gif"));
        setImageDescriptor(file.compare.Activator.getImageDescriptor("/icons/sample3.gif"));
    }

    public void run() {
        CompareConfiguration config = new CompareConfiguration();
        config.setProperty(CompareConfiguration.SHOW_PSEUDO_CONFLICTS, Boolean.FALSE);

        // left
        config.setLeftEditable(true);
        config.setLeftLabel("Left");

        // right
        config.setRightEditable(true);
        config.setRightLabel("Right");

        CompareEditorInput editorInput = new CompareEditorInput(config) {
            CompareItem left = new CompareItem("F:/.java");
            CompareItem right = new CompareItem("F:/.java");
            
            @Override
            protected Object prepareInput(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
                return new DiffNode(null, Differencer.CONFLICTING, null, left, right);
            }

            @Override
            public void saveChanges(IProgressMonitor pm) throws CoreException {
                super.saveChanges(pm);

                left.writeFile();
                right.writeFile();
            }
        };

        editorInput.setTitle("文件比较");
       // CompareUI.openCompareEditor(editorInput);
      //  CompareUI.openCompareDialog(editorInput);
    }

    class CompareItem extends BufferedContent implements ITypedElement, IModificationDate, IEditableContent {
        private String fileName;
        private long time;

        CompareItem(String fileName) {
            this.fileName = fileName;
            this.time = System.currentTimeMillis();
        }

        /**
         * @see org.eclipse.compare.BufferedContent#createStream()
         */
        protected InputStream createStream() throws CoreException {
            try {
                return new FileInputStream(new File(fileName));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            return new ByteArrayInputStream(new byte[0]);
        }

        /**
         * @see org.eclipse.compare.IModificationDate#getModificationDate()
         */
        public long getModificationDate() {
            return time;
        }

        /**
         * @see org.eclipse.compare.ITypedElement#getImage()
         */
        public Image getImage() {
            return CompareUI.DESC_CTOOL_NEXT.createImage();
        }

        /**
         * @see org.eclipse.compare.ITypedElement#getName()
         */
        public String getName() {
            return fileName;
        }

        /**
         * @see org.eclipse.compare.ITypedElement#getType()
         */
        public String getType() {
            return ITypedElement.TEXT_TYPE;
        }

        /**
         * @see org.eclipse.compare.IEditableContent#isEditable()
         */
        public boolean isEditable() {
            return true;
        }

        /**
         * @see org.eclipse.compare.IEditableContent#replace(org.eclipse.compare.ITypedElement, org.eclipse.compare.ITypedElement)
         */
        public ITypedElement replace(ITypedElement dest, ITypedElement src) {
            return null;
        }

        public void writeFile() {
            this.writeFile(this.fileName, this.getContent());
        }

        private void writeFile(String fileName, byte[] newContent) {
            FileOutputStream fos = null;
            try {
                File file = new File(fileName);
                if (file.exists()) {
                    file.delete();
                }

                file.createNewFile();

                fos = new FileOutputStream(file);
                fos.write(newContent);
                fos.flush();

            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                fos = null;
            }
        }
    }
}
