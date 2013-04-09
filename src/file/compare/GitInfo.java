package file.compare;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;

public class GitInfo {
//public static String URL="D:/cfrManage/ConfigFile/设备配置文件管理/思科/192.168.0.248";
public static String URL="d:/cfrmanage/configfile";
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		byte[] leftByte=read("ce2110fa2cf47c60868e1b0284c62968892e6d20");
		byte[] rigthByte=getParentContent("ce2110fa2cf47c60868e1b0284c62968892e6d20");
		createFile(leftByte,rigthByte);
		//System.out.println(new String(leftByte));
		
		try {
			System.out.println(new FileInputStream(new File("D:/test/cfr.file.compare/MessagePopupAction.java")));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public static void createFile(byte[] fileByte,byte[] rigthByte){
		//ByteArrayInputStream byteFile=new ByteArrayInputStream(fileByte);
		FileOutputStream leftOut=null;
		FileOutputStream rigthOut=null;
		File leftFile=new File("left.txt");
		File rigthFile=new File("rigth.txt");
		if(leftFile.exists()){
			leftFile.delete();
			System.out.println("有新文件 ，已经删除");
		}
		if(rigthFile.exists()){
			rigthFile.delete();
			System.out.println("有新文件 ，已经删除");
		}
		try {
			leftOut = new FileOutputStream("left.txt");
			leftOut.write(fileByte);
			rigthOut = new FileOutputStream("rigth.txt");
			rigthOut.write(rigthByte);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				leftOut.flush();
				leftOut.close();
				rigthOut.flush();
				rigthOut.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		
	}
	// 获取文本内容
		public static byte[] read(String revision) {
			OutputStream out = null;
			Repository repository = null;
			Git git = null;
			try {
				if(git==null){
					File gitDir=new File(URL);
					try {
						git=Git.open(gitDir);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			//	git=ConfigCopy.getGit();
				repository = git.getRepository();
				RevWalk walk = new RevWalk(repository);
				ObjectId objId = repository.resolve(revision);
				RevCommit revCommit = walk.parseCommit(objId);
				RevTree revTree = revCommit.getTree();

				TreeWalk treeWalk = TreeWalk.forPath(repository,
						"设备配置文件管理/思科/192.168.0.248/192.168.0.248config.txt", revTree);
				if (treeWalk == null)
					return null;
				ObjectId blobId = treeWalk.getObjectId(0);
				ObjectLoader loader = repository.open(blobId);
				byte[] bytes = loader.getBytes();
				if (bytes != null)
					return bytes;
			} catch (IOException e) {
			} catch (JGitInternalException e) {
			} finally {
				if (repository != null)
					repository.close();
			}
			return null;
		}
		/**
	     * @throws IOException
	     */
	    public static byte[] getParentContent(String revision) {
	        Git git;
	        byte[] perContent =null;
			try {
				git = Git.open(new File(URL));
		        Repository repository = git.getRepository();
		        RevWalk walk = new RevWalk(repository);
		        ObjectId objId = repository.resolve(revision);
		        RevCommit revCommit = walk.parseCommit(objId);
		        String preVision = revCommit.getParent(0).getName();
		        ObjectId preId= repository.resolve(preVision);
		        RevCommit revTree = walk.parseCommit(preId);
		        RevTree tree= revTree.getTree();
		        ObjectId  objectId=  tree.getId();
		        TreeWalk treeWalk=TreeWalk.forPath(repository, "设备配置文件管理/思科/192.168.0.248/192.168.0.248config.txt",objectId);
		       ObjectId treeObj= treeWalk.getObjectId(0);
		       perContent =repository.open(treeObj).getBytes();
		        repository.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	        return perContent;
	    }

}
