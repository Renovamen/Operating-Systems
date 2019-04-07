package File;

import javax.swing.JOptionPane;

public class Block
{
	String property;
	String data;
	int index;

	void setProperty(FCB fcb)
	{
		property="文件类型:"+fcb.whoAmI+'\n';
		property=property+"文件名:"+fcb.name+'\n';
		property=property+"地址:"+fcb.fatherAddress+'\n';
		property=property+"创建时间:"+fcb.createTime+'\n';
		property=property+"最近访问:"+fcb.visitTime+'\n';
		property=property+"最近修改:"+fcb.modifiTime+'\n';
		property=property+"块号:"+new Integer(index).toString()+'\n';
		property=property+"是否为隐藏文件:"+new Boolean(fcb.isHide).toString();
	}

	void setData(String str)
	{
		data=str;
	}

	void setData(ContentPanel content)
	{
		data="";
		for (int i=0; i<content.folderList.size(); i++) data=data+content.folderList.get(i).block.index+'\n';

		data=data+"NULL\n";
		for (int i=0; i<content.fileList.size(); i++) data=data+content.fileList.get(i).block.index+'\n';
	}
}