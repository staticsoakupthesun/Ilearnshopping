 #git总结
 ###1：配置用户名
 #### git config -- global user.name "用户名"
 ###2：配置邮箱
 #### git config --global user.email "你的邮箱"
 ###3：编码配置
 #### 避免git gui中的中文乱码 git config --global gui.encoding utf-8
 #### 避免 git status显示的中文文件名乱码
 #### git config --global core.quotepath off
 ###4：其他
 #### git config --global core.ignorecase false
 ###5：git ssh key pair配置
 ####1：在git bash命令行窗口中输入：ssh-keygen -t rsa -C "你的邮箱"
 ####2：,然后一路回车，不要输入任何密码之类，生成ssh key pair
 ####3：在用户目录下生成.ssh文件夹，找到公钥和私钥id_rsa id_rsa.pub
 ####4：将公钥的内容复制
 ####5：，进入github网站，将公钥添加进去
 ## git中常用的命令
 #### git init创建本地仓库
 #### git status 检查工作区文件状态
 #### git add 文件名  添加到暂存区
 #### git commit -m "描述"  提交到本地仓库
 #### git log 查看提交committed
 #### git reset --hard committid 版本回退
 #### git branch 查看分支
 #### git checkout -b dev 创建并切换到dev分支
 #### 切换分支：git checkout 分支名
 #### 拉取: git pull
 #### 提交: git push -u origin master
 #### 分支合并: git merge branchname
 #### git remote add origin "远程仓库地址"  关联
 #### git fetch 远程分支拉取
 #### 将分支推送到远程 git push origin HEAD -u
 ### git 验证
 #### 执行git --version，出现版本信息，安装成功。
 ###关联步骤
 #### git remote add origin "远程仓库的位置" 关联
 #### git push -u -f origin master 第一次向远程仓库推送
 #### git push origin master 以后提交到远程
 ### 合并分支
 #### 1：先切换至主分支上 git checkout master
 #### 2：操作要合并的分支名 git merge 名字

