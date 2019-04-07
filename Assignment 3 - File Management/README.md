# File Management

文件管理，预览：运行 `File.jar` 。



## 开发环境

语言：`Java`

IDE：`Intellij IDEA`



## 项目结构

```
├── File                        
│   ├── Block.java         // 物理块
│   ├── ContentPanel.java  // 文件夹打开后的面板
│   ├── Disk.java          // 磁盘管理
│   ├── FCB.java           // 文件控制块
│   ├── Folder.java        // 文件夹图标
│   ├── FolderToolBar.java // 工具栏
├── Main.java
```



### FCB类

定义了文件控制块，提供一些函数的接口以及文件属性。



#### 成员变量

|名称|说明|
|------|------|
|ContentPanel contentPanel|文件夹所拥有的面板（文件没有这个属性）|
|ContentPanel fatherContentPanel|父文件夹|
|String whoAmI|文件 / 文件夹|
|String fatherAddress|父文件夹地址|
|String name|绝对地址|
|String createTime|创建时间|
|String visitTime|访问时间|
|String modifiTime|修改时间|
|boolean isHide|是否为隐藏文件|



#### 函数

|名称|说明|
|------|------|
|setFatherAddress( )|设置父文件夹地址|
|getSystemTime( )|获取系统时间|
|setCreateTime( )|设置创建时间|
|setVisitTime( )|设置访问时间|
|setModifiTime( )|设置修改时间|
|showProperty( )|属性显示框|
|getProperty( )|获取属性|



### Folder类

继承自FCB类，在fatherContentPanel中显示文件夹图标，并提供双击打开和通过右键进行的一些操作。

#### 函数

|名称|说明|
|------|------|
|open( )|打开文件夹|
|delete( )|删除文件夹|
|checkName( )|判断是否重名|
|resetName( )|重命名|
|hideFolder( )|隐藏文件夹|
|getAddress( )|获取绝对地址|



### FolderToolBar类

工具栏，显示工具栏界面，并提供搜索、后退、输入绝对地址打开文件或文件夹的功能。



### MyFile类

继承自FCB类，在fatherContentPanel中显示记事本图标，提供提供双击打开和通过右键进行的一些操作，提供打开记事本后进行的修改、保存等操作。



### Block类

定义了物理块，可以从真实的电脑上存取数据。块中有一些信息，包括文件或文件夹的一些属性、文件或文件夹的数据、该block在Disk中的序号。



### ContentPanel类

文件夹打开后的面板。



#### 成员变量

|名称|说明|
|------|------|
|Vector folderList|该文件夹下的文件夹|
|Vector fileList|该文件夹下的文件|
|ContentPanel fatherContentPanel|父文件夹面板|
|static boolean isShowAll|是否显示所有文件|
|static ContentPanel showingPanel|正在显示哪个文件夹|



#### 函数

|名称|说明|
|------|------|
|refresh( )|刷新|
|createFolder( )|创建文件夹|
|createFile( )|创建文件|
|delete( )|是否删除|
|getFolder( )|获取选中文件夹的信息|



### Disk类

对磁盘的位图进行管理，对Block进行申请、释放，对磁盘进行读写，提供双击进入磁盘面板和右键进行格式化等操作。



## 技术细节

### 存储空间管理：显式链接（FAT）
在每一个盘区都开一个文件夹用来存储当前盘区所有存储块的使用情况，并记录盘区内所有文件所占用内存块的情况。

优点：不存在内存碎片，最大程度的利用了盘区空间。



### 空闲空间管理方式：位图

使用一串0和1来标识内存块的状态，0为未使用，1为已占用。创建文件时从最前方开始检索空闲块，确定使用后将0置为1。



## 功能实现

### 创建文件或文件夹
向Disk申请Block，如果Block申请成功，则分配相应的Block给文件夹，并进行初始化处理。如果Block申请失败，则告知用户内存已用完。



### 删除

#### 删除文件
判断文件是否正在被使用，如果正在被使用，则删除失败，并警告用户，否则从Block中移除该文件并返回删除成功的信息，然后回收Block。

#### 删除文件夹
扫描该Folder的vector，利用删除文件的方法将每一个文件删除。如果要删除的文件正在被使用，则删除失败。如果所有文件都删除成功，则返回删除成功的信息。

删除完文件后，扫描该Folder的vector，利用递归的方法删除文件夹。如果所有文件夹都删除成功，则返回删除成功的信息。

如果该Folder下所有的文件和文件夹都删除成功，则删除该Folder并释放资源。否则删除失败告知用户有文件正在被使用。



### 重命名

#### 重命名文件
判断重命名是否规范，若规范则修改成功，否则自动帮助修改。然后修改Block中的property。

#### 重命名文件夹
还需要修改该文件夹下所有FCB的fatherAddress。



### 搜索

使用深度优先搜索在多级树形目录中查找文件或文件夹，如果查找成功则显示名字符合的文件和文件夹。



### 搜索地址

根据树形目录去查找该地址，如果查找成功则显示该地址对应的文件或文件夹，否则提示用户查找失败。
