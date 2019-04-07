package File;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JTextField;

public abstract class FCB
{
	JPanel viewPanel=new JPanel();
	JTextField nameField=new JTextField();
	ImageIcon viewImg;

	ContentPanel contentPanel;			//文件夹所拥有的面板  文件没有这个属性
	ContentPanel fatherContentPanel;	//父文件夹
	String whoAmI;						//文件/文件夹
	String fatherAddress;				//父文件夹地址
	String name;						//绝对地址
	String createTime;					//创建时间
	String visitTime;					//访问时间
	String modifiTime;					//修改时间
	Block block;
	JFrame propertyFrame;
	boolean isHide=false;				//是否为隐藏文件


	//public abstract void create();
	//public abstract void resetName();
	public abstract	boolean delete(boolean isRootPanel);
	public abstract void open();

	public void setFatherAddress(String str)
	{
		fatherAddress=str;
	}

	public String getSystemTime()	//获取系统时间
	{
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return  formatter.format(currentTime);
	}

	public void setCreateTime()
	{
		createTime=getSystemTime();
		visitTime=createTime;
		modifiTime=createTime;

	}

	public void setVisitTime()
	{
		visitTime=getSystemTime();

	}

	public void setModifiTime()
	{
		modifiTime=getSystemTime();

	}

	public void showProperty()	//属性显示
	{
		propertyFrame=new JFrame();
		propertyFrame.setTitle(name+"属性");
		propertyFrame.setSize(300, 450);
		propertyFrame.setResizable(false);
		propertyFrame.setVisible(true);
		propertyFrame.setLocationRelativeTo(Disk.mainFrame);
		propertyFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

		JPanel panel=new JPanel();
		panel.setBackground(Color.white);
		panel.setLayout(null);
		propertyFrame.add(panel);

		JLabel viewLabel=new JLabel(viewImg);
		viewLabel.setBounds(10, 10, 70, 70);
		panel.add(viewLabel);

		JLabel nameLabel=new JLabel(name,JLabel.LEFT);
		nameLabel.setBounds(100, 60, 200, 20);
		panel.add(nameLabel);


		JLabel type=new JLabel("类型:",JLabel.LEFT);
		type.setBounds(20, 90, 50, 20);
		panel.add(type);

		JLabel typeLabel;
		typeLabel=new JLabel(whoAmI,JLabel.LEFT);
		typeLabel.setBounds(100, 90, 200, 20);
		panel.add(typeLabel);

		JLabel address=new JLabel("位置:",JLabel.LEFT);
		address.setBounds(20, 130, 50, 20);
		panel.add(address);

		JLabel addressLabel=new JLabel(fatherAddress,JLabel.LEFT);
		addressLabel.setBounds(100, 130, 200, 20);
		panel.add(addressLabel);

		JLabel blockNum=new JLabel("块号:",JLabel.LEFT);
		blockNum.setBounds(20, 170, 50, 20);
		panel.add(blockNum);

		JLabel fileBlock=new JLabel(new Integer(block.index).toString(),JLabel.LEFT);
		fileBlock.setBounds(100, 170, 200, 20);
		panel.add(fileBlock);

		if (whoAmI.equals("文件夹"))
		{
			JLabel include=new JLabel("包含:",JLabel.LEFT);
			include.setBounds(20, 210, 50, 20);
			panel.add(include);

			JLabel includeFile=new JLabel("文件夹:"+contentPanel.folderList.size()+"  文件:"+contentPanel.fileList.size(),JLabel.LEFT);
			includeFile.setBounds(100,210,200,20);
			panel.add(includeFile);
		}
		else
		{
			JLabel size=new JLabel("大小:",JLabel.LEFT);
			size.setBounds(20, 210, 50, 20);
			panel.add(size);

			int index;
			for (index=0; index<fatherContentPanel.fileList.size(); index++)
				if (fatherContentPanel.fileList.get(index)==this) break;
			JLabel fileSize=new JLabel(new Integer(fatherContentPanel.fileList.get(index).text.length()).toString(),JLabel.LEFT);
			fileSize.setBounds(100, 210, 200, 20);
			panel.add(fileSize);
		}

		JLabel create=new JLabel("创建:",JLabel.LEFT);
		create.setBounds(20, 250, 50, 20);
		panel.add(create);

		JLabel time1=new JLabel(createTime,JLabel.LEFT);
		time1.setBounds(100, 250, 200, 20);
		panel.add(time1);

		JLabel visit=new JLabel("访问:",JLabel.LEFT);
		visit.setBounds(20, 290, 50, 20);
		panel.add(visit);

		JLabel time2=new JLabel(visitTime,JLabel.LEFT);
		time2.setBounds(100, 290, 200, 20);
		panel.add(time2);

		JLabel modifi=new JLabel("修改:",JLabel.LEFT);
		modifi.setBounds(20, 330, 50,20);
		panel.add(modifi);

		JLabel time3=new JLabel(modifiTime,JLabel.LEFT);
		time3.setBounds(100, 330, 200, 20);
		panel.add(time3);

		JLabel hideLabel=new JLabel("属性:",JLabel.LEFT);
		hideLabel.setBounds(20, 370, 50, 20);
		panel.add(hideLabel);

		ActionListener noHide=new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (!isHide) return ;
				isHide=false;
				fatherContentPanel.refresh();
			}
		};
		ActionListener yesHide=new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				if (isHide) return ;
				isHide=true;
				fatherContentPanel.refresh();
			}
		};

		ButtonGroup hideProperty=new  ButtonGroup();
		JRadioButtonMenuItem hide=new JRadioButtonMenuItem("隐藏");
		hide.setBorderPainted(false);
		hide.addActionListener(yesHide);
		hide.setBackground(Color.white);
		hide.setBounds(100, 370, 50, 20);
		panel.add(hide);

		JRadioButtonMenuItem see=new JRadioButtonMenuItem("可见");
		see.setBorderPainted(false);
		see.addActionListener(noHide);
		see.setBackground(Color.white);
		see.setBounds(150, 370, 50, 20);
		panel.add(see);

		if (isHide) hide.setSelected(true);
		else see.setSelected(true);
		hideProperty.add(see);
		hideProperty.add(hide);
	}

	public void getProperty(Block block)
	{
		this.block=block;

		String str=block.property;
		int begin;
		int end;

		begin=str.indexOf("文件名:");
		str=str.substring(begin+4);
		end=str.indexOf('\n');
		name=str.substring(0, end);
		str=str.substring(end+1);

		begin=str.indexOf("地址:");
		str=str.substring(begin+3);
		end=str.indexOf('\n');
		fatherAddress=str.substring(0,end);
		str=str.substring(end+1);

		begin=str.indexOf("创建时间:");
		str=str.substring(begin+5);
		end=str.indexOf('\n');
		createTime=str.substring(0,end);
		str=str.substring(end+1);

		begin=str.indexOf("最近访问:");
		str=str.substring(begin+5);
		end=str.indexOf('\n');
		visitTime=str.substring(0,end);
		str=str.substring(end+1);

		begin=str.indexOf("最近修改:");
		str=str.substring(begin+5);
		end=str.indexOf('\n');
		modifiTime=str.substring(0,end);
		str=str.substring(end+1);

		begin=str.indexOf("是否为隐藏文件:");
		str=str.substring(begin+8);
		if (str.equals("false"))	isHide=false;
		else isHide=true;
	}

}
